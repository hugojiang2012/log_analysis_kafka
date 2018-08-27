package impl;
import domain.Log_Analysis_Tbl;
import jdbc.JDBCHelper;
import dao.log_write_db_DAO;



/**
 * Created by jiangjinping on 2018/5/29.
 * 将重要的日志信息持久化到数据库中
 */
public class log_write_db_DAOImpl  implements  log_write_db_DAO{
    public void insert (Log_Analysis_Tbl  log_data){
        String sql ="insert into log_analysis values(?,?,?,?,?,?,?)";
        Object[] params = new Object[]{
                log_data.getTime(),
                log_data.getPid(),
                log_data.getTid(),
                log_data.getFunction(),
                log_data.getLine(),
                log_data.getLevel(),
                log_data.getInformation()
        };
        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}
