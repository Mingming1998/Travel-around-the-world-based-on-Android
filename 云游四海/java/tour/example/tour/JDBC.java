package tour.example.tour;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBC {
    protected static String dbClassName = "com.mysql.jdbc.Driver";//数据库连接驱动类
    protected static String dbUrl = "jdbc:mysql://106.15.176.162:3306/t1?characterEncoding=utf8";//数据库连接URL
    protected static String dbUser = "jinyuan";				//数据库用户名
    protected static String dbPwd = "R66NvD60G9UzBHEh";			//数据库密码
    private static Connection conn = null;//数据库连接对象

    private JDBC() {										//默认构造函数
        try {
            if (conn == null) {							//如果连接对象为空
                Class.forName(dbClassName);				//加载驱动类
                conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);//获得连接对象
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
    public static ResultSet executeQuery(String sql) {	//查询方法
        try {
            if(conn==null)  new JDBC();  //如果连接对象为空，则重新调用构造方法
//            Statement statement=conn.createStatement();
//            ResultSet s=statement.executeQuery(sql);
            return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery(sql);//执行查询
//            return s;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;				//返回null值
        } finally {
        }
    }
    public static int executeUpdate(String sql) {		//更新方法
        try {
            if(conn==null)  new JDBC();	//如果连接对象为空，则重新调用构造方法
            return conn.createStatement().executeUpdate(sql);//执行更新
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
        }
    }
    public static void close() {//关闭方法
        try {
            conn.close();//关闭连接对象
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            conn = null;	//设置连接对象为null值
        }
    }
}
