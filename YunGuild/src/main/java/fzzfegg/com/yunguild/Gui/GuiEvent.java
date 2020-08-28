package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import fzzfegg.com.yunguild.util.sign;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class GuiEvent implements Listener {

    YunGuild plugin = YunGuild.instance;

    //主页
    @EventHandler(priority = EventPriority.HIGHEST)
    public void InventoryClick(InventoryClickEvent e) throws SQLException {

        String NoneGui_title = plugin.guiCon.getString("NoneGui.title").replace("&","§");
        String Select_title = plugin.guiCon.getString("SelectGuild.title").replace("&","§");
        String Guild_title = plugin.guiCon.getString("GuildGui.title").replace("&","§");

        String ydktitle = e.getWhoClicked().getOpenInventory().getTitle();
        Player player = (Player)e.getWhoClicked();
        try {
        if(ydktitle.equals(NoneGui_title)){
            e.setCancelled(true);
            switch (e.getRawSlot()){
                case 11:
                    plugin.inPlayerInput.add(player.getName());
                    player.closeInventory();
                    player.sendMessage(plugin.msg.get("inputguildname").toString()
                            .replace("&", "§"));
                    break;
                case 15:
                    try {
                        ListGui.Gui_listGuild(plugin,player,1);
                    }catch (SQLException bc){bc.printStackTrace();}
                    break;
                default:
            }
        }else if(ydktitle.equals(Select_title)){
            e.setCancelled(true);
            if(e.getRawSlot() == 47 || e.getRawSlot() == 51) {
                if (e.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE) {
                    ListGui.Gui_listGuild(plugin, player,
                            (Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .split("第")[1].split("页")[0]) - 1) * 21 + 1);
                }
            }else{
                try {
                    String guildname = e.getCurrentItem().getItemMeta().getDisplayName()
                            .split(plugin.fg[0])[1]
                            .split(plugin.fg[1])[0];
                    try {
                        ResultSet my = MySQLManager.get().findname(player,player.getName());
                        my.last();
                        if(my.getRow() == 0) {
                            ResultSet mysql = MySQLManager.get().listApplyPlayer(player, player.getName());
                            Set<String> list = new HashSet<>();
                            while (mysql.next()) {
                                list.add(mysql.getString("guild_name"));
                            }
                            if (!list.contains(guildname)) {
                                add(plugin, guildname, player);
                            }
                        }
                    }catch (SQLException|NullPointerException bc){
                        add(plugin,guildname,player);
                    }
                }catch (NullPointerException bc){}
            }
        }else if(ydktitle.equals(Guild_title.replace("&","§").replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")))){
            e.setCancelled(true);
            String guild_name = plugin.player_gui.get(player.getName()+".guild_name");
            String admin_name = plugin.player_gui.get(player.getName()+".admin_name");

            switch (e.getRawSlot()){
                case 10:
                    if(e.getClick() == ClickType.LEFT) {
                        assitGui.playerList_Gui(plugin, admin_name, player, guild_name, 1);
                    }else if(e.getClick() == ClickType.RIGHT && player.getName().equals(admin_name)){
                        assitGui.ApplyPlayerList_Gui(plugin,player,guild_name,1);
                    }
                    break;
                case 12:
                    flagGui.flag_Gui(plugin,player,guild_name);
                    break;
                case 14:
                    //升级
                    assitGui.levelGui(plugin,player);
                    break;
                case 16:
                    //公会家
                    ResultSet mysql = MySQLManager.get().findGuild(player, guild_name);
                    mysql.last();
                    try {
                        String world = mysql.getString("tp").split(",")[0];
                        Double x = Double.valueOf(mysql.getString("tp").split(",")[1]);
                        Double y = Double.valueOf(mysql.getString("tp").split(",")[2]);
                        Double z = Double.valueOf(mysql.getString("tp").split(",")[3]);
                        Float yaw = Float.valueOf(mysql.getString("tp").split(",")[4]);
                        Float pitch = Float.valueOf(mysql.getString("tp").split(",")[5]);
                        Location location = new Location(Bukkit.getWorld(world),x,y,z,yaw,pitch);
                        player.sendMessage(plugin.msg.get("zjl_cd").toString().replace("&","§").replace("%time%",String.valueOf(plugin.getConfig().getInt("Guild.issuzTime"))));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.teleport(location);
                                player.sendMessage(plugin.msg.get("guildtp").toString().replace("&","§"));
                            }
                        }.runTaskLater(plugin,plugin.getConfig().getInt("Guild.issuzTime")*20);
                    }catch (NullPointerException | SQLException bc){
                        player.sendMessage(plugin.msg.get("guildnotp").toString().replace("&","§"));
                    }
                    break;
                case 28:
                    //仓库
                    break;
                case 30:
                    //打卡
                    if(plugin.player_cd.contains(player.getName())){
                        player.sendMessage(plugin.msg.get("joinguildsign").toString().replace("&","§"));
                    }else {
                        sign.level_sign(plugin, player, guild_name);
                    }
                    break;
                case 32:
                    //召集令
                    if(plugin.getConfig().getList("noTpWorld").contains(player.getWorld().getName())){
                        player.sendMessage(plugin.msg.get("zjl_no_tp").toString().replace("&","§"));
                    }else {
                        int price = plugin.getConfig().getInt("Guild.issueZhaojiling");
                        if (plugin.econa.has(player, price)) {
                            plugin.ycs.remove(player.getName() + "@" + plugin.ranStr);
                            plugin.econa.withdrawPlayer(player, price);
                            String msg = plugin.msg.get("zjl_issue_msg").toString().replace("&", "§").replace("%player%", player.getName());
                            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild " + player.getName() + "@" + plugin.ranStr);
                            HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(plugin.msg.get("zjl_show_text").toString().replace("&", "§").replace("%player%", player.getName())).create());

                            ResultSet data = MySQLManager.get().listData(player, guild_name);
                            while (data.next()) {
                                Player byq_player = Bukkit.getPlayer(data.getString("player_name"));
                                byq_player.sendMessage(new ComponentBuilder(msg.replace("&", "§")).event(clickEvent).event(hoverEvent).create());
                            }
                        } else {
                            player.sendMessage(plugin.msg.get("nomoney").toString().replace("&", "§").replace("%bz_money%", String.valueOf(price)));
                        }
                    }
                    break;
                case 34:
                    //退出
                    assitGui.conGui(plugin,player);
                    break;
                default:
                    break;
            }

        }

        }catch (NullPointerException bc){}

        if(e.getRawSlot()<0 || e.getRawSlot()>e.getInventory().getSize() || e.getInventory()==null) {
            return;
        }

    }

    private static void add(YunGuild plugin,String guildname,Player player) throws SQLException {
        ResultSet mysql2 = MySQLManager.get().findGuild(player, guildname);
        mysql2.last();
        String admin_name = mysql2.getString("admin_name");
        MySQLManager.get().insertData3(guildname, player.getName(), player);
        player.sendMessage(plugin.msg.get("applyguild").toString()
                .replace("&", "§").replace("%guild_name%", guildname));

        Bukkit.getPlayer(admin_name).sendMessage(plugin.msg.get("applyadmin").toString().replace("&", "§").replace("%player%", player.getName()));
        player.closeInventory();
    }

}
