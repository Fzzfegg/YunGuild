package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.ResultSet;
import java.sql.SQLException;


public class chat implements Listener {

    YunGuild plugin = YunGuild.instance;

    @EventHandler (priority = EventPriority.HIGHEST)
    public void inputChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String msg = event.getMessage();
        try {
            if(msg.split(plugin.chatPrefix)[0].equals("")){
                event.setCancelled(true);
                msg = plugin.msgFormat.replace("%msg%",msg.split(plugin.chatPrefix)[1])
                        .replace("&","§").replace("%player%",player.getName());

                String prefix = PlaceholderAPI.setPlaceholders(player, "%vault_prefix%");
                String group = PlaceholderAPI.setPlaceholders(player, "%vault_rank%");

                ResultSet data = MySQLManager.get().findname(player,player.getName());
                data.last();
                String guild_name = data.getString("guild_name");
                String identity = data.getString("identity");
                identity = plugin.msg.get("identity."+identity).toString().replace("%guild_name%","").replace("&","§");


                ComponentBuilder componentBuilder = new ComponentBuilder("");
                for(String each : plugin.chatHover){
                    each = each.replace("%player%",player.getName()).replace("%player_level%",String.valueOf(player.getLevel()))
                            .replace("%player_health%",String.valueOf(player.getHealth())).replace("%player_money%",String.valueOf(plugin.econa.getBalance(player)))
                            .replace("%player_prefix%",prefix).replace("%player_group%",group).replace("%player_world%",player.getWorld().getName())
                            .replace("%identity%",identity).replace("&","§");

                    componentBuilder.append(each+"\n");
                }

                ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa "+player.getName());
                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create());

                ResultSet mysql = MySQLManager.get().listData(player,guild_name);

                while (mysql.next()){
                    try {
                        Player sendPlayer = Bukkit.getPlayer(mysql.getString("player_name"));
                        if(sendPlayer.isOnline()){
                           sendPlayer.sendMessage(new ComponentBuilder(msg.replace("&","§")).event(clickEvent).event(hoverEvent).create());
                        }
                    }catch (NullPointerException bc){}
                }
            }
        }catch (NullPointerException| SQLException|ArrayIndexOutOfBoundsException bc){}

    }



}
