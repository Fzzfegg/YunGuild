package fzzfegg.com.yunguild.Sql;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.UUID;

public class SQLite {

    //创建+链接数据库 CONNECT
    public static Connection getConnection() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);
        SQLiteDataSource ds = new SQLiteDataSource(config);
        //⭐你可以命名"jdbc:sqlite:"后面的数据库文件名称，程序运行时若无此文件，会自动创建
        String url = System.getProperty("user.dir"); // 获取工作目录
        ds.setUrl("jdbc:sqlite:"+url+"/plugins/YunGuild/"+"guild.db");
        return ds.getConnection();
    }

    //创建库操作
    public static void createDataBase(Connection con)throws SQLException {
        String sql = "create database guild; ";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);

        //创建表
        createTable(con);

    }

    //创建表操作 CREATE TABLE
    public static void createTable(Connection con)throws SQLException {
        //⭐这里需要自定义数据类型和数据数量
        String sql = "DROP TABLE IF EXISTS guild ;create table guild (english String, chinese String); ";
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);

    }
/*

    //新增操作 INSERT
    public static void insert(Connection con, String english, String chinese)throws SQLException {
        String sql = "insert into NUMBERS (english, chinese) values(?,?)";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1;
        pst.setString(idx++, uuid.toString());
        pst.setString(idx++, sqlite);
        pst.executeUpdate();

    }

    //修改操作 UPDATE
    public static void update(Connection con, String english, String chinese)throws SQLException {
        String sql = "update NUMBERS set chinese = ? where english = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1 ;
        pst.setString(idx++, sqlite);
        pst.setString(idx++, uuid.toString());
        pst.executeUpdate();
    }

    //刪除操作 DELETE
    public static void delete(Connection con, String english)throws SQLException {
        String sql = "delete from NUMBERS where english = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1 ;
        pst.setString(idx++, uuid.toString());
        pst.executeUpdate();
    }

    //查找操作 SELECT
    public static void selectAll(Connection con)throws SQLException {
        String sql = "select * from NUMBERS";
        Statement stat = null;
        ResultSet rs = null;
        stat = con.createStatement();
        rs = stat.executeQuery(sql);
        while(rs.next())
        {
            String uuid = rs.getString("english");
            String sqlite = rs.getString("chinese");
            System.out.println(rs.getString("english")+"\t"+rs.getString("chinese"));
        }
    }
*/

}
