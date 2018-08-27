
import java.util.*;

import domain.Log_Analysis_Tbl;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.api.java.function.VoidFunction;
import com.google.common.base.Optional;
import dao.log_write_db_DAO;
import dao.factory.DAOFactory;
import scala.Tuple2;
import org.apache.spark.streaming.kafka.HasOffsetRanges;
import org.apache.spark.streaming.kafka.KafkaCluster;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.kafka.OffsetRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kafka.common.TopicAndPartition;
import kafka.message.MessageAndMetadata;
import scala.Predef;
import scala.collection.JavaConversions;
import kafka.serializer.StringDecoder;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;


/**
 * Created by jiangjinping on 2018/5/21.
 * 从sparkstreaming流中获取日志信息，然后过滤出来错误的日志信息
 * 将其保存到MySQL中
 * 日志格式信息如下：
 * 2017-11-03 10:07:44.170|32050|32050|msg_dispatch_init|282|INFO|msg dispatch has initialized [2]!
 * 2017-11-03 10:07:44.170|32050|32050|msg_dispatch_init|282|ERROR|msg dispatch has initialized [3]!
 *2017-11-03 10:07:44.170|32050|32050|msg_dispatch_init|282|DEBUG|msg dispatch has initialized [4]!
 *2017-11-03 10:07:44.170|32050|32050|msg_dispatch_init|282|ERROR|msg dispatch has initialized [5]!
 *
 */
public class log_analysis {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {

        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("log_analysis");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));


        // 创建kafkaStream
        Set<String> topicSet = new HashSet<String>();
        topicSet.add("loganalysis");

        HashMap<String, String> kafkaParam = new HashMap<String, String>();
        kafkaParam.put("metadata.broker.list", "master:9092,slave1:9092,slave2:9092");
        kafkaParam.put("auto.offset.reset", "smallest");

        JavaPairInputDStream<String, String>  message = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParam,
                topicSet);

        JavaDStream<String> LogDStream = message.map(new Function<Tuple2<String, String>, String>() {
            public String call(Tuple2<String, String> v1) throws Exception {
                return v1._2();
            }
        });
        LogDStream.print();

        //首先从sparkstreaming rdd 中过滤出错误信息的RDD
         /*1.生成白名单，即streaming rdd  信息字段是ERROR就保留，其他级别就过滤掉*/

        List<Tuple2<String, Boolean>> whitelist = new ArrayList<Tuple2<String, Boolean>>();
        whitelist.add(new Tuple2<String, Boolean>("ERROR", true));
        final JavaPairRDD<String, Boolean> whitelistRDD = jssc.sc().parallelizePairs(whitelist);

        // 所以，要先对输入的数据，进行一下转换操作，变成(level, time pid tid function line level information)
        // 以便于，后面对每个batch RDD，与定义好的白名单RDD进行join操作
        JavaPairDStream<String, String> levelLogDStream = LogDStream.mapToPair(

                new PairFunction<String, String, String>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String, String> call(String Log)
                            throws Exception {
                        return new Tuple2<String, String>(
                                Log.split("\\|")[5], Log);
                    }

                });

        // 然后，就可以执行transform操作了，将每个batch的RDD，与白名单RDD进行join、filter、map等操作
        // 实时进行白名单过滤，只有是白名单的数据才进行保留
        JavaDStream<String> validLogDStream = levelLogDStream.transform(

                new Function<JavaPairRDD<String,String>, JavaRDD<String>>() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public JavaRDD<String> call(JavaPairRDD<String, String> userAdsClickLogRDD)
                            throws Exception {
                         //要用左外连接
                        JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> joinedRDD =
                                userAdsClickLogRDD.leftOuterJoin(whitelistRDD);

                        // 连接之后，执行filter算子
                        JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> filteredRDD =
                                joinedRDD.filter(

                                        new Function<Tuple2<String,
                                                Tuple2<String,Optional<Boolean>>>, Boolean>() {

                                            private static final long serialVersionUID = 1L;

                                            @Override
                                            public Boolean call(
                                                    Tuple2<String,
                                                            Tuple2<String, Optional<Boolean>>> tuple)
                                                    throws Exception {
                                                // 这里的tuple，就是每个用户，对应的访问日志，和在白名单中
                                                // 的状态
                                                if(tuple._2._2().isPresent() &&
                                                        tuple._2._2.get()) {
                                                    return true;
                                                }
                                                return false;
                                            }

                                        });

                        // 此时，filteredRDD中，就只剩下含有错误信息的日志了
                        // 进行map操作，转换成我们想要的格式
                        JavaRDD<String> validLogRDD = filteredRDD.map(

                                new Function<Tuple2<String,Tuple2<String,Optional<Boolean>>>, String>() {

                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public String call(
                                            Tuple2<String, Tuple2<String, Optional<Boolean>>> tuple)
                                            throws Exception {
                                        return tuple._2._1;
                                    }

                                });

                        return validLogRDD;
                    }

                });

        //将sparkstreaming日志信息存储到mySQL中
        processDStream(validLogDStream);

        // 打印含有错误的日志信息
        validLogDStream.print();
        jssc.start();
        jssc.awaitTermination();
        jssc.close();
    }

    //返回Log_Analysis_Tbl格式的rdd，然后写入数据库中
    private static void processDStream(JavaDStream<String> dStream) {

        log_write_db_DAO logwritedbDAO  = DAOFactory.get_log_write_db_DAO();
        dStream.foreachRDD(new VoidFunction<JavaRDD<String>>() {

            @Override
            public void call(JavaRDD<String> rdd) throws Exception {

                if (rdd.isEmpty()) {
                    return;
                }
                List<String> errorList = rdd.collect();
                for (String line : errorList){
                    Log_Analysis_Tbl logLine = LogParser.parseLog(line);
                    //将数据写入MySQL中
                    logwritedbDAO.insert(logLine);
                }
            }
        });
    }



}
