package jdbc;

import java.security.spec.ECField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import  java.util.LinkedList;
import java.util.List;
import conf.ConfigurationManager;
import constant.Constants;
import  scala.collection.immutable.Stream;

/**
 * Created by jiangjinping on 2018/5/29.
 */
public class JDBCHelper {
    //第一步：在静态代码块中，直接加载数据库驱动
    static {
        try{
            String driver = ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
            Class.forName(driver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //实现JDBCHelper的单例化
    private static JDBCHelper instance = null;

    //获取单例
    public static JDBCHelper getInstance(){
        if(instance == null ){
            synchronized (JDBCHelper.class){
                if(instance == null){
                    instance = new JDBCHelper();
                }
            }
        }
        return instance;
    }

    //数据库连接池
    private LinkedList<Connection> datasource = new LinkedList<Connection>();

    /*
    * 实现单例的过程中，创建唯一的数据库连接池
    * 私有化构造方法
    * JDBCHelper在整个程序运行生命周期中，只会创建一次实例
    * 在这一次创建实例的过程中，就会调用JDBCHelper（）的方法
    * 此时，就可以在构造方法中，去创建自己唯一的一个数据库连接池
    * */
    private JDBCHelper(){
        //创建连接池的大小
        int datasourcesSize = ConfigurationManager.getInteger(Constants.JDBC_DATASOURCE_SIZE);

        //然后创建指定数量的数据库连接，并放入数据库连接池中
        for(int i = 0; i < datasourcesSize; i++){
            //要在my.properties中设定jdbc url user password
            String url = ConfigurationManager.getProperty(Constants.JDBC_URL);
            String user = ConfigurationManager.getProperty(Constants.JDBC_USER);
            String password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
            try{
                Connection conn = DriverManager.getConnection(url,user,password);
                datasource.push(conn);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /*
    * 提供数据库连接方法
    * 有可能，获取的时候连接池已经用光了，暂时获取不到数据库连接
    * 所以要写一个简单的等待机制，等待获取到数据库连接
    * synchronized设置多线程并发访问限制
    * */
    public synchronized Connection getConnection(){
        try{
            while (datasource.size() == 0){
                Thread.sleep(10);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return datasource.poll();
    }

    /*
    * 数据增删改查的方法
    * 1.执行增删SQL语句的方法
    * 2.执行查询SQL语句的方法
    * 3.批量执行SQL语句的方法
    * */
    //增删数据库中的数据
    public int executeUpdate(String sql, Object[] params){

        //数据库连接句柄
        int rtn = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            for(int  i = 0; i < params.length; i++){
                pstmt.setObject( i + 1, params[i]);
            }
            rtn = pstmt.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                datasource.push(conn);
            }
        }
        return rtn;

    }
    //执行查询SQL语句
    public  void executeQuery(String sql, Object[] params,
                              QueryCallback callback){
        Connection conn = null;
        PreparedStatement pstmt =  null;
        ResultSet rs =  null;
        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            for(int  i = 0; i < params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            rs = pstmt.executeQuery();
            callback.process(rs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( conn != null){
                datasource.push(conn);
            }
        }

    }
    /*    批量查询SQL
    批量执行SQL语句，是JDBC的一个高级功能
    默认情况下，每次执行一条SQL语句，就会通过网络连接，向MySQL发送一次请求
    但是，如果在短时间内要执行多条结构完全一样的SQL，只是参数不同
    虽然使用PreparedStatment这种方式，可以只编译一次SQL，提高性能
    但是，还是对于每次SQL都要向MySQL发送一次网络请求
    *可以通过批量执行SQL语句的功能优化这个性能
    * 一次性通过PreparedStatement发送多条SQL语句，可以几百几千甚至几万条
    *执行的时候，也仅仅编译一次就可以
    * 这样的批量执行查询数据库能大大提高效率
    *
    */
    public  int[] executeBatch(String sql, List<Object[]> paramsList){
        int[] rtn =  null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try{
            conn = getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for(Object[] params: paramsList){
                for(int i = 0; i < params.length; i++){
                    pstmt.setObject(i+1, params[i]);
                }
                pstmt.addBatch();
                rtn = pstmt.executeBatch();
            }
            conn.commit();

        }catch (Exception e){
            e.printStackTrace();
        }
        return rtn;
    }


    /*
    * 内部类：查询回调接口
    * */
    public static interface QueryCallback{
        //处理查询结果
        void process(ResultSet rs) throws  Exception;
    }

}
