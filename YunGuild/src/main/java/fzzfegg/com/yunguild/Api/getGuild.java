package fzzfegg.com.yunguild.Api;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;

public class getGuild {

    public ResultSet getGuild_player(CommandSender sender, String player_name){
        ResultSet mysql = MySQLManager.get().findname(sender,player_name);
        return mysql;
    }

    public ResultSet getGuild_info(CommandSender sender, String guild_name){
        ResultSet mysql = MySQLManager.get().findGuild(sender,guild_name);
        return mysql;
    }

    public ResultSet getGuild_listplayer(CommandSender sender, String guild_name){
        ResultSet mysql = MySQLManager.get().listData(sender,guild_name);
        return mysql;
    }
}
