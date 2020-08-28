package fzzfegg.com.yunguild.Sql;


import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.command.CommandSender;

import java.sql.*;

public class MySQLManager {
    private String ip;
    private String databaseName;
    private String userName;
    private String userPassword;
    private Connection connection;
    private int port;
    public static MySQLManager instance = null;

    public static MySQLManager get() {
        return instance == null ? instance = new MySQLManager() : instance;
    }

    public void enableMySQL(YunGuild plugin) {
        ip = (String) plugin.dabaseCon.get("Database.mysql.ip");
        databaseName = (String)plugin.dabaseCon.get("Database.mysql.databasename");
        userName = (String)plugin.dabaseCon.get("Database.mysql.username");
        userPassword = (String)plugin.dabaseCon.get("Database.mysql.password");
        port = plugin.dabaseCon.getInt("Database.mysql.port");
        connectMySQL();
        String cmd = SQLCommand.CREATE_TABLE1.commandToString();
        try {
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
        cmd = SQLCommand.CREATE_TABLE2.commandToString();
        try {
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
        cmd = SQLCommand.CREATE_TABLE3.commandToString();
        try {
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
        cmd = SQLCommand.CREATE_TABLE4.commandToString();
        try {
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.executeUpdate();
        } catch (SQLException e) {e.printStackTrace();}
    }


    private void connectMySQL() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?autoReconnect=true&useSSL=true", userName, userPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void doCommand(PreparedStatement ps, CommandSender sender) {
        try {
            ps.executeUpdate();
        } catch (SQLException e) {sender.sendMessage("执行指令失败，以下为错误提示");e.printStackTrace(); }
    }

    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException e) {e.printStackTrace();}
    }

    public boolean insertData1(String guildname, String playername, CommandSender sender, int maxCount) {
        try {
            PreparedStatement ps = null;
            String s = null;
            s = SQLCommand.ADD_DATA1.commandToString();
            ps = connection.prepareStatement(s);
            ps.setString(1, guildname);
            ps.setString(2, playername);
            ps.setInt(3, maxCount);
            doCommand(ps, sender);
        } catch (SQLException|NullPointerException e) {e.printStackTrace();return false;}
        return true;
    }
    public boolean insertData2(String guildname, String playername, CommandSender sender,String identity) {
        try {
            PreparedStatement ps = null;
            String s = null;
            s = SQLCommand.ADD_DATA2.commandToString();
            ps = connection.prepareStatement(s);
            ps.setString(1, playername);
            ps.setString(2, identity);
            ps.setString(3, guildname);
            doCommand(ps, sender);
        } catch (SQLException|NullPointerException e) {e.printStackTrace();return false;}
        return true;
    }
    public boolean insertData3(String guildname, String playername, CommandSender sender) {
        try {
            PreparedStatement ps = null;
            String s = null;
            s = SQLCommand.ADD_DATA3.commandToString();
            ps = connection.prepareStatement(s);
            ps.setString(1, playername);
            ps.setString(2, guildname);
            doCommand(ps, sender);
        } catch (SQLException|NullPointerException e) {e.printStackTrace();return false;}
        return true;
    }
    public boolean insertTime(String playername, Timestamp time, String guildname, CommandSender sender,int onlineTime) {
        try {
            PreparedStatement ps = null;
            String s = null;
            s = SQLCommand.ADD_TIME.commandToString();
            ps = connection.prepareStatement(s);
            ps.setString(1, playername);
            ps.setTimestamp(2, time);
            ps.setString(3, guildname);
            ps.setInt(4, onlineTime);
            doCommand(ps, sender);
        } catch (SQLException|NullPointerException e) {e.printStackTrace();return false;}
        return true;
    }
    public ResultSet findname(CommandSender sender,String name) {
        try {
            String s = SQLCommand.FIND_NAME.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, name);
            return ps.executeQuery();
        } catch (SQLException e) {e.printStackTrace();}return null;
    }
    public ResultSet findGuild(CommandSender sender,String name) {
        try {
            String s = SQLCommand.FIND_GUILD.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {e.printStackTrace();} return null;
    }
    public ResultSet findSign(CommandSender sender,String name) {
        try {
            String s = SQLCommand.FIND_SIGN.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {e.printStackTrace();} return null;
    }
    public void update_admin(CommandSender sender, String value,String guild_name) {
        try {
            String s = SQLCommand.UPDATE_GUILD_ADMIN.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.setString(2, guild_name);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_tp(CommandSender sender, String value,String guild_name) {
        try {
            String s = SQLCommand.UPDATE_TP.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.setString(2, guild_name);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_pvp(CommandSender sender, String value,String guild_name) {
        try {
            String s = SQLCommand.UPDATE_PVP.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.setString(2, guild_name);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_level(CommandSender sender, int value,String guild_name,int max_limit) {
        try {
            String s = SQLCommand.UPDATE_LEVEL.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setInt(1, value);
            ps.setInt(2, max_limit);
            ps.setString(3, guild_name);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_identity(CommandSender sender,String player, String value) {
        try {
            String s = SQLCommand.UPDATE_IDENTITY.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.setString(2, player);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_info(CommandSender sender,String guild_name, String value) {
        try {
            String s = SQLCommand.UPDATE_INFO.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, value);
            ps.setString(2, guild_name);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void update_time(CommandSender sender,Timestamp time,int count,String player,int onlineTime) {
        try {
            String s = SQLCommand.UPDATE_TIME.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setTimestamp(1, time);
            ps.setInt(2, count);
            ps.setInt(3, onlineTime);
            ps.setString(4, player);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public ResultSet listsData(CommandSender sender) {
        try {
            String s = SQLCommand.LISTS_GUILD.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {e.printStackTrace();} return null;
    }
    public ResultSet listData(CommandSender sender,String str) {
        try {
            String s = SQLCommand.LIST_GUILD.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) { e.printStackTrace(); }return null;
    }
    public ResultSet listApplyGuild(CommandSender sender,String str) {
        try {
            String s = SQLCommand.LIST_APPLY_GUILD.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) { e.printStackTrace(); }return null;
    }
    public ResultSet listApplyPlayer(CommandSender sender,String str) {
        try {
            String s = SQLCommand.LIST_APPLY_NAME.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) { e.printStackTrace(); }return null;
    }
    public void deleteApplyPlayer(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_APPLY.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuildPlayer(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_PLAYER.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuild(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_GUILD.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuild_All_Player(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_GUILD_ALL_PLAYER.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuild_All_Apply(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_GUILD_ALL_APPLY.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuild_DELETE_SIGN(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_SIGN.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
    public void deleteGuild_DELETE_ALL_SIGN(CommandSender sender,String str) {
        try {
            String s = SQLCommand.DELETE_ALL_SIGN.commandToString();
            PreparedStatement ps = connection.prepareStatement(s);
            ps.setString(1, str);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
}