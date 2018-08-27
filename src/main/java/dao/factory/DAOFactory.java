package dao.factory;
import dao.log_write_db_DAO;
import impl.log_write_db_DAOImpl;

/**
 * Created by jiangjinping on 2018/5/30.
 * 工厂类为了对日志信息插入数据库进行封装操作
 */
public class DAOFactory {
    /*
* 获取日志信息数据库访问对象（DAO）
* */
    public static log_write_db_DAO  get_log_write_db_DAO(){
        return new log_write_db_DAOImpl();
    }


}
