package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class reload {

    public static void reloada(YunGuild plugin){
        plugin.reloadConfig();
        plugin.dabaseFile = new File(plugin.getDataFolder(),"database.yml");
        plugin.dabaseCon = YamlConfiguration.loadConfiguration(plugin.dabaseFile);
        plugin.msgFile = new File(plugin.getDataFolder(),"msg.yml");
        plugin.msgCon = YamlConfiguration.loadConfiguration(plugin.msgFile);
        plugin.msg = plugin.msgCon.getValues(false);
        plugin.pluginPrefix = plugin.msg.get("pluginPrefix").toString().replace("&","§");
        plugin.guiFile = new File(plugin.getDataFolder(), "gui.yml");
        plugin.guiCon = YamlConfiguration.loadConfiguration(plugin.guiFile);
        plugin.levelFile = new File(plugin.getDataFolder(),"level.yml");
        plugin.levelCon = YamlConfiguration.loadConfiguration(plugin.levelFile);
        plugin.signFile = new File(plugin.getDataFolder(),"sign.yml");
        plugin.signCon = YamlConfiguration.loadConfiguration(plugin.signFile);
        plugin.fg = plugin.guiCon.getString("SelectGuild.guildInfo.name").replace("&","§").split("%guild%");

        for(Object each : plugin.msg.keySet()){
            plugin.msg.put(each,plugin.pluginPrefix+plugin.msg.get(each));
        }

        plugin.msg.put("identity.default",plugin.getConfig().getString("Guild.identity.default"));
        plugin.msg.put("identity.elite",plugin.getConfig().getString("Guild.identity.elite"));
        plugin.msg.put("identity.grand",plugin.getConfig().getString("Guild.identity.grand"));
        plugin.msg.put("identity.admin",plugin.getConfig().getString("Guild.identity.admin"));

        //选择数据库
        try {useSql.useSql(plugin);} catch (SQLException e) {e.printStackTrace();}

        reloadchat(plugin);

    }

    private static void reloadchat(YunGuild plugin){

        plugin.chatPrefix = plugin.getConfig().getString("Guild.chat.prefix");
        plugin.msgFormat =  plugin.getConfig().getString("Guild.chat.msgformat");
        plugin.chatHover = (List<String>) plugin.getConfig().getList("Guild.chat.hover");

    }




}
