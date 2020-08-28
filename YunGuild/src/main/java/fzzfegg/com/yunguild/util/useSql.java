package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.Sql.SQLite;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;

public class useSql {

    public static void useSql(YunGuild plugin) throws SQLException {
        String sql = (String) plugin.dabaseCon.get("Database.database");
        if(sql.equalsIgnoreCase("mysql")){

            new BukkitRunnable() {

                @Override
                public void run() {
                    MySQLManager.get().enableMySQL(plugin);
                }
            }.runTaskAsynchronously(plugin);
            //利用BukkitRunnable创建新线程，防止使用SQL而堵塞主线程

        }else if(sql.equalsIgnoreCase("sqlite")){
            Connection sqlConnection = SQLite.getConnection();
            SQLite.createDataBase(sqlConnection);
        }


    }


}
