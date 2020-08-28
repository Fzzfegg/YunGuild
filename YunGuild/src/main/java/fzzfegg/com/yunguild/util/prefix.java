package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.entity.Player;

public class prefix {

    //称号
    public static void setPrefix(YunGuild plugin, Player player, String prefix){
        plugin.chat.setPlayerPrefix(player,prefix);
        player.sendMessage(plugin.msg.get("playerprefix").toString().replace("&","§").replace("%prefix%",prefix));
    }

}
