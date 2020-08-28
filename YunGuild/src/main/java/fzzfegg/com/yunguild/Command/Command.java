package fzzfegg.com.yunguild.Command;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fzzfegg.com.yunguild.Gui.*;
import fzzfegg.com.yunguild.util.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor {

    YunGuild plugin = YunGuild.instance;

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender | sender.isOp()){
            switch (args[0]){
                case "reload":
                    reload.reloada(plugin);
                    sender.sendMessage(plugin.msg.get("reload").toString().replace("&","§"));

                    return true;
                case "setprice":
                    plugin.guiCon.set("NoneGui.createGuild.price",Integer.parseInt(args[1]));
                    try {plugin.guiCon.save(plugin.guiFile);} catch (IOException e) {e.printStackTrace();}

                    sender.sendMessage(plugin.msg.get("setprice").toString()
                            .replace("&","§").replace("%price%",args[1]));
                    return true;
                case "setitem":
                    Player player = (Player) sender;
                    ItemStack wp = player.getInventory().getItemInMainHand();
                    plugin.guiCon.set("NoneGui.createGuild.item",wp);
                    try {plugin.guiCon.save(plugin.guiFile);} catch (IOException e) {e.printStackTrace();}
                    sender.sendMessage(plugin.msg.get("setitem").toString()
                            .replace("&","§").replace("%item_name%",wp.getItemMeta().getDisplayName()));
                    return true;
                case "setlevel":
                    Player player1 = (Player) sender;
                    if(args[1].equalsIgnoreCase("item")) {
                        ItemStack wp1 = player1.getInventory().getItemInMainHand();
                        plugin.levelCon.set("Level."+Integer.parseInt(args[2])+".item", wp1);
                        try {plugin.levelCon.save(plugin.levelFile);} catch (IOException e) {e.printStackTrace();}
                        sender.sendMessage(plugin.msg.get("setitem").toString()
                                .replace("&", "§").replace("%item_name%", wp1.getItemMeta().getDisplayName()));
                    }else if(args[1].equalsIgnoreCase("price")){
                        plugin.levelCon.set("Level."+Integer.parseInt(args[2])+".price",Integer.valueOf(args[3]));
                        try {plugin.levelCon.save(plugin.levelFile);} catch (IOException e) {e.printStackTrace();}
                        sender.sendMessage(plugin.msg.get("setprice").toString().replace("&", "§").replace("%price%",args[3]));
                    }
                    return true;
                default:
            }
        }

        if(sender instanceof Player){
            Player player = ((Player) sender).getPlayer();

            if(args[0].contains(plugin.ranStr)){
                Player by_player = Bukkit.getPlayer(args[0].split("@")[0]);

                try {
                    if(plugin.ycs.get(args[0]).contains(player.getName())) {
                        return true;
                    }
                }catch (NullPointerException bc){}
                List list = new ArrayList();
                if (plugin.ycs.containsKey(args[0])) {
                    list = plugin.ycs.get(args[0]);
                    list.add(player.getName());
                } else {
                    list.add(player.getName());
                }
                plugin.ycs.put(args[0], list);
                player.sendMessage(plugin.msg.get("zjl_cd").toString().replace("&","§").replace("%time%",String.valueOf(plugin.getConfig().getInt("Guild.issuzTime"))));
                plugin.cd.add(player.getName());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(plugin.cd.contains(player.getName())) {
                            player.teleport(by_player);
                            plugin.cd.remove(player.getName());
                        }
                    }
                }.runTaskLater(plugin,plugin.getConfig().getInt("Guild.issuzTime")*20);

                return true;
            }
            switch (args[0]){
                case "open":
                    try {
                        open(plugin,sender,player);
                    } catch (SQLException | NullPointerException e) {
                        try {
                            open(plugin,sender,player);
                        }catch (SQLException | NullPointerException bc){
                            bc.printStackTrace();
                            NoneGuild.Gui_createOrlist(plugin, player);
                        }
                    }
                    return true;
                default:
                    break;
            }
        }

        return false;
    }

    public static void open(YunGuild plugin,CommandSender sender,Player player) throws SQLException{

        ResultSet mysql = MySQLManager.get().findname(sender,player.getName());
        mysql.last();
        if(mysql.getRow() == 0){
            NoneGuild.Gui_createOrlist(plugin, player);
        }else{
            String guild_name = mysql.getString("guild_name");
            ResultSet mysql1 = MySQLManager.get().findGuild(sender,guild_name);
            mysql1.last();
            //存储玩家对应的GUI
            plugin.player_gui.put(player.getName()+".guild_name",guild_name);
            plugin.player_gui.put(player.getName()+".admin_name",mysql1.getString("admin_name"));
            int max_player = mysql1.getInt("max_limit");
            int guild_level = mysql1.getInt("guild_level");
            int guild_chest = mysql1.getInt("guild_chest");
            GuildinfoGui.Gui_infoGuild(plugin,player,guild_name,max_player,guild_level,guild_chest);
        }
    }

}

