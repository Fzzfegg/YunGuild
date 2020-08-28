package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class sign {

    public static void level_sign(YunGuild plugin,Player player, String guild_name){
        try {
            ResultSet mysql = MySQLManager.get().findGuild(player, guild_name);
            mysql.last();

            int level = mysql.getInt("guild_level");
            int getOlineTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60;
            int delayTime = plugin.signCon.getInt("Sign.getTime");
            int QGTime = plugin.signCon.getInt("Sign.time");
            Date oldTime = null;

            int[] count = {1};

            Date date = new Date();
            Timestamp sqlDate = new Timestamp(date.getTime());

            ResultSet sign_mysql = MySQLManager.get().findSign(player, player.getName());
            sign_mysql.last();

            try {
                oldTime = sign_mysql.getTimestamp("oldTime");

                count[0] = sign_mysql.getInt("count");
                int hour = (int) ((date.getTime() - oldTime.getTime()) / (1000 * 60 * 60));
                //大于CD时间 可签到
                if(hour>QGTime){
                    //超过24小时 中断连续签到
                    if(hour>QGTime+24){
                        MySQLManager.get().update_time(player,sqlDate,1,player.getName(),getOlineTime);
                    }else {
                        count[0]=count[0]+1;
                        if (count[0] > plugin.signCon.getInt("Sign.Count." + level + ".max")) {
                            count[0] = 1;
                        }
                        MySQLManager.get().update_time(player,sqlDate, count[0], player.getName(),getOlineTime);
                        player.sendMessage(plugin.msg.get("signlq").toString().replace("&","§").replace("%count%", String.valueOf(count[0])));
                    }
                    player.sendMessage(plugin.msg.get("signwait").toString().replace("&","§").replace("%time%",String.valueOf(delayTime)));

                    Runnable(plugin,player,count,level,guild_name,delayTime);

                }else{
                    player.sendMessage(plugin.msg.get("signxc").toString().replace("&","§").replace("%time%",String.valueOf(QGTime-hour)));
                }
            }catch (NullPointerException|SQLException bc){
                count[0]=count[0]+1;
                if (count[0] > plugin.signCon.getInt("Sign.Count." + level + ".max")) {
                    count[0] = 1;
                }
                MySQLManager.get().insertTime(player.getName(),sqlDate,guild_name,player,getOlineTime);

                player.sendMessage(plugin.msg.get("signwait").toString().replace("&","§")
                        .replace("%time%",String.valueOf(delayTime)));

                Runnable(plugin,player,count,level,guild_name,delayTime);
            }
        }catch (SQLException bc){}
    }


    private static void Runnable(YunGuild plugin,Player player,int[] count,int level,String guild_name,int delayTime) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    for(String command : plugin.signCon.getStringList("Sign.Level."+level)){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("&","§")
                                .replace("%player%",player.getName()).replace("%guild_name%",guild_name));
                    }
                    try {
                        for (String command : plugin.signCon.getStringList("Sign.Count." + level + "." + count[0])) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("&", "§")
                                    .replace("%player%", player.getName()).replace("%guild_name%", guild_name));
                        }
                    } catch (NullPointerException bc) {}

                    player.sendMessage(plugin.msg.get("signsucceed").toString().replace("&", "§")
                            .replace("%sign_info%", plugin.signCon.getString("Sign.Msg." + level).replace("&", "§")));

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, delayTime*20*60, 6000);
    }
}
