package ApiInterface;

import org.apache.spark.api.java.JavaRDD;

import java.util.Map;

/**
 * Created by jiangjingping on 2018/7/25.
 */
public interface Appanalysis {

    public void analysis_log(JavaRDD<String>  lines);

}
