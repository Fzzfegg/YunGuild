package fzzfegg.com.yunguild.Sql;

import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class timeMysql {

    public static void sch(YunGuild plugin){
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MySQLManager.get().listData(null,"ping");
                }catch (NullPointerException bc){}
            }
        }.runTaskLater(plugin,1200);

    }

}
