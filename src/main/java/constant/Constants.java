package constant;

/**
 * Created by jiangjinping on 2018/5/29.
 * 常量的接口封装，防止硬编码
 */
public interface Constants {
    /*
    * 项目配置相关的常量
    * */
    String JDBC_DRIVER = "jdbc.driver";
    String JDBC_DATASOURCE_SIZE = "jdbc.datasource.size";
    String JDBC_URL ="jdbc.url";
    String JDBC_USER ="jdbc.user";
    String JDBC_PASSWORD = "jdbc.password";
    String SPARK_LOCAL ="spark.local";
    String ClASS_NAME ="classname";
    String CLASS_ID ="classid";
    String ELEMENT_NUM  = "elemnum";
    String ELEMENT = "element";
    String ELEMENT_ID ="elementid";
    String ELEMENT_TYPE ="elementtype";
    String

    /*
    *spark作业相关的常量
     *  */
    String SPARK_APP_NAME ="log_analysis";
    String  FIELD_TIME_ID ="time";
    String  FIELD_PID_ID ="pid";
    String  FIELD_TID_ID = "tid";
    String FIELD_FUNCTION_ID = "function";
    String FIELD_LINE_ID = "line";
    String FIELD_LEVEL_ID ="level";
    String FIELD_INFORMATION_ID="information";
}
