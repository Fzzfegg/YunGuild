package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class listener implements Listener {

    YunGuild plugin = YunGuild.instance;

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location loc1 = event.getFrom();
        Location loc2 = event.getTo();
        if(loc1.getWorld() == loc2.getWorld() && loc1.getX() == loc2.getX() && loc1.getY() == loc2.getY() && loc1.getZ() == loc2.getZ()){

        }else{
            if(plugin.cd.contains(player.getName())){
                player.sendMessage(plugin.msg.get("zjl_move").toString().replace("&","§"));
                plugin.cd.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void inputChatEvent(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        global:
        if(plugin.inPlayerInput.contains(player.getName())){
            event.setCancelled(true);
            plugin.inPlayerInput.remove(player.getName());
            int Maxlegth = plugin.getConfig().getInt("Guild.maxNameLength");
            int Minlegth = plugin.getConfig().getInt("Guild.minNameLength");
            int MaxCount = plugin.getConfig().getInt("Guild.defaultPlayerCount");
            String guildname = event.getMessage();

            for(Object eachname : plugin.getConfig().getList("noGuildName")){
                if(guildname.contains(eachname.toString())){
                    event.getPlayer().sendMessage(plugin.msg.get("nolegth").toString()
                            .replace("&", "§"));
                    break global;
                }
            }

            if(guildname.length() <= Maxlegth && guildname.length() >= Minlegth && !guildname.contains(" ")){
                String permission = plugin.guiCon.getString("NoneGui.createGuild.permission");
                int price = plugin.guiCon.getInt("NoneGui.createGuild.price");
                ItemStack wp = plugin.guiCon.getItemStack("NoneGui.createGuild.item");
                try {
                    String.valueOf(price).equals("");
                }catch (NullPointerException bc){price=0;}

                if(permission.equalsIgnoreCase("") || player.hasPermission(permission)){
                    if(plugin.econa.has(player,price)){
                        if(wp==null){
                            plugin.econa.withdrawPlayer(player,price);
                            if(MySQLManager.get().insertData1(guildname,player.getName(),player,MaxCount)) {
                                MySQLManager.get().insertData2(guildname,player.getName(),player,"admin");
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        for(String command : plugin.guiCon.getStringList("NoneGui.createGuild.command")){
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("&","§")
                                                    .replace("%player%",player.getName()).replace("%guild_name%",guildname));
                                        }
                                    }
                                }.runTask(plugin);
                            }else {
                                player.sendMessage("§4公会创建出错,请联系管理");
                            }
                            //成功
                        }else {
                            if (player.getInventory().contains(wp)) {
                                plugin.econa.withdrawPlayer(player,price);
                                player.getInventory().removeItem(wp);
                                if(MySQLManager.get().insertData1(guildname,player.getName(),player,MaxCount)) {
                                    MySQLManager.get().insertData2(guildname,player.getName(),player,"admin");

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            for(String command : plugin.guiCon.getStringList("NoneGui.createGuild.command")){
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("&","§")
                                                        .replace("%player%",player.getName()).replace("%guild_name%",guildname));
                                            }
                                        }
                                    }.runTask(plugin);
                                }else {
                                    player.sendMessage("§4公会创建出错,请联系管理");
                                }
                                //成功
                            } else {
                                player.sendMessage(plugin.msg.get("noitem").toString()
                                        .replace("&", "§").replace("%item_name%", wp.getItemMeta().getDisplayName()));
                            }
                        }
                    }else{
                        player.sendMessage(plugin.msg.get("nomoney").toString()
                                .replace("&","§").replace("%bz_money%",String.valueOf(price-plugin.econa.getBalance(player))));
                    }
                }
            }else{
                event.getPlayer().sendMessage(plugin.msg.get("nolegth").toString()
                        .replace("&", "§"));
            }
        }else if(plugin.inPlayerInput.contains(player.getName()+".editinfo")){
            event.setCancelled(true);
            plugin.inPlayerInput.remove(player.getName()+".editinfo");
            String info = event.getMessage();
            ResultSet mysql = MySQLManager.get().findname(player,player.getName());
            mysql.last();
            String guild_name = mysql.getString("guild_name");
            MySQLManager.get().update_info(player,guild_name,info);
            player.sendMessage(plugin.msg.get("editwc").toString().replace("&","§").replace("%info%",info));
        }

    }


    @EventHandler
    public void playerPVP(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player & event.getDamager() instanceof Player){
            Player bd = (Player) event.getEntity();
            Player ac = (Player) event.getDamager();
            ResultSet bd_player = MySQLManager.get().findname(bd,bd.getName());
            ResultSet ac_player = MySQLManager.get().findname(ac,ac.getName());
            try {
                bd_player.last();
                ac_player.last();
                String bd_guild = bd_player.getString("guild_name");
                String ac_guild = ac_player.getString("guild_name");
                if(bd_guild.equals(ac_guild)){
                    ResultSet flag = MySQLManager.get().findGuild(ac,ac_guild);
                    flag.last();
                    if(Boolean.valueOf(flag.getString("flag_pvp"))){
                        event.setCancelled(true);
                    }
                }
            } catch (SQLException | NullPointerException bc) {bc.printStackTrace();}
        }
    }



}
