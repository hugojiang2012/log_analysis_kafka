package conf;
import org.apache.spark.sql.sources.In;
import scala.tools.cmd.gen.AnyVals;
import java.io.InputStream;
import java.util.Properties;


/**
 * Created by jiangjinping on 2018/5/28.
 * spark系统初始化的时候加载配置
 * 配置管理组件
 */
public class ConfigurationManager {
    private  static Properties prop = new Properties();
    /*
    * 静态代码块
    * */
    static{
        try{
            InputStream in = ConfigurationManager.class
                    .getClassLoader().getResourceAsStream("my.properties");
            prop.load(in);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    * 获取指定key对应的value
    * */
    public static String getProperty(String key){ return  prop.getProperty(key);}

    public static Integer getInteger (String key){
        String value = getProperty(key);
        try{
            return Integer.valueOf(value);
        } catch (NumberFormatException e){
            e.printStackTrace();
        }
        return 0;
    }
    /*
    *获取布尔类型的配置项
    * */
    public static Boolean getBoolean(String key){
        String value = getProperty(key);
        try{
            return Boolean.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
