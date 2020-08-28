package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.Command.Command;
import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;


public class sonGuiEvent implements Listener {

    YunGuild plugin = YunGuild.instance;

    //次级监听
    @EventHandler(priority = EventPriority.HIGH)
    public void InventoryClick(InventoryClickEvent e) throws SQLException {
        String ydktitle = e.getWhoClicked().getOpenInventory().getTitle();
        String ListPlayerGui = plugin.guiCon.getString("ListPlayerGui.title").replace("&","§");
        String GuildFlagGui = plugin.guiCon.getString("GuildFlag.title").replace("&","§");
        String ApplyListGui = plugin.guiCon.getString("ApplyPlayerGui.title").replace("&","§");
        String confirmGui = plugin.guiCon.getString("confirmGui.title").replace("&","§");
        String transferconGui = plugin.guiCon.getString("transferconGui.title")
                .replace("&","§").replace("%player%","");
        String identityGui = plugin.guiCon.getString("identityGui.title").replace("&","§");
        String levelGui = plugin.levelCon.getString("Level.title").replace("&","§");

        Player player = (Player)e.getWhoClicked();

        if(e.getRawSlot()<0 || e.getRawSlot()>e.getInventory().getSize() || e.getInventory()==null) {
            return;
        }
        if(ydktitle.equals(ListPlayerGui)){
            e.setCancelled(true);
            if(e.getRawSlot() == 47 || e.getRawSlot() == 51) {
                if (e.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE) {
                    assitGui.playerList_Gui(plugin,plugin.player_gui.get(player.getName()+".admin_name"), player,plugin.player_gui.get(player.getName()+".guild_name")
                            ,(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .split("第")[1].split("页")[0]) - 1) * 21 + 1);
                }
            }
            if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()
                    && e.getClick() == ClickType.RIGHT && plugin.player_gui.get(player.getName()+".admin_name").equals(player.getName())){
                String by_player = e.getCurrentItem().getItemMeta().getDisplayName().split("\\: ")[1];
                if(!plugin.player_gui.get(player.getName()+".admin_name").equals(by_player)) {
                    plugin.player_gui.put(player.getName()+".identityGui", by_player);
                    assitGui.identityGui(plugin, player,by_player);
                }
            }
        }else if(ydktitle.equals(GuildFlagGui)){
            e.setCancelled(true);
            ResultSet sql_A = MySQLManager.get().findname(player, player.getName());
            sql_A.last();
            String guild_name = sql_A.getString("guild_name");
            String identity = sql_A.getString("identity");
            String prefix = plugin.msg.get("identity."+identity).toString().replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")).replace("&","§");

            ResultSet sql = MySQLManager.get().findGuild(player, guild_name);
            sql.last();
            String admin_name = sql.getString("admin_name");
            if(player.getName().equals(admin_name)) {
                switch (e.getRawSlot()) {
                    case 10:
                        String qf;
                        try {
                            qf = String.valueOf(!sql.getBoolean("flag_pvp"));
                        } catch (NullPointerException | SQLException bc) {
                            qf = "true";
                        }
                        MySQLManager.get().update_pvp(player, qf,guild_name);
                        flagGui.flag_Gui(plugin, player, guild_name);
                        break;
                    case 12:
                        try {
                            if(plugin.getConfig().getList("noTpWorld").contains(player.getWorld().getName())){
                                player.sendMessage(plugin.msg.get("guildnosettp").toString().replace("&","§"));
                            }else {
                                String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," +
                                        player.getLocation().getY() + "," + player.getLocation().getZ() + "," +
                                        player.getLocation().getYaw() + "," + player.getLocation().getPitch();

                                MySQLManager.get().update_tp(player, location, guild_name);

                                player.sendMessage(plugin.msg.get("guildsettp").toString().replace("&", "§"));
                            }
                        } catch (NullPointerException ignored) {}
                        break;
                    case 16:
                        plugin.inPlayerInput.add(player.getName()+".editinfo");
                        player.sendMessage(plugin.msg.get("editinfo").toString().replace("&","§"));
                        player.closeInventory();
                    default:
                }
            }
            switch (e.getRawSlot()){
                case 14:
                    fzzfegg.com.yunguild.util.prefix.setPrefix(plugin,player,prefix);
                    break;
                default:
            }
        }else if(ydktitle.equals(ApplyListGui)){
            e.setCancelled(true);
            if(e.getRawSlot() == 47 || e.getRawSlot() == 51) {
                if (e.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE) {
                    assitGui.ApplyPlayerList_Gui(plugin, player,plugin.player_gui.get(player.getName()+".guild_name")
                            ,(Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName()
                                    .split("第")[1].split("页")[0]) - 1) * 21 + 1);
                }
            }
            try {
                if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()){
                    String by_player = e.getCurrentItem().getItemMeta().getDisplayName().split("\\: ")[1];
                    try {
                        ResultSet mysql = MySQLManager.get().listApplyPlayer(player,by_player);
                        mysql.last();
                        String guild_name;
                        if(mysql.getRow() != 0){
                            guild_name = mysql.getString("guild_name");
                            ResultSet mysql2 = MySQLManager.get().findGuild(player, guild_name);
                            mysql2.last();
                            String admin_name = mysql2.getString("admin_name");
                            if(e.getClick() == ClickType.LEFT) {
                                int Max_Count = mysql2.getInt("max_limit");
                                ResultSet mysql3 = MySQLManager.get().listData(player, guild_name);
                                mysql3.last();
                                int countPlayer = mysql3.getRow();
                                if(countPlayer != Max_Count){
                                    //增加玩家到公会
                                    MySQLManager.get().insertData2(guild_name,by_player,player,"default");
                                    //删除apply里的玩家
                                    MySQLManager.get().deleteApplyPlayer(player,by_player);
                                    ResultSet list = MySQLManager.get().listData(player,guild_name);
                                    while (list.next()){
                                        Player fs_player = Bukkit.getPlayer(list.getString("player_name"));
                                        fs_player.sendMessage(plugin.msg.get("playerjoinguild").toString().replace("&","§")
                                                .replace("%player%",by_player).replace("%guild_name%",guild_name));
                                    }
                                }else{
                                    player.sendMessage(plugin.msg.get("maxplayer").toString().replace("&","§")
                                            .replace("%count%",String.valueOf(countPlayer)).replace("%max_count%",String.valueOf(Max_Count)));
                                }
                            }else if(e.getClick() == ClickType.RIGHT){
                                Bukkit.getPlayer(by_player).sendMessage(plugin.msg.get("denyguild").toString().replace("&","§").replace("%guild_name%",guild_name));
                                MySQLManager.get().deleteApplyPlayer(player,by_player);
                            }
                        }else{
                            ResultSet mysql2 = MySQLManager.get().findname(player,player.getName());
                            mysql2.last();
                            guild_name = mysql2.getString("guild_name");
                            player.sendMessage(plugin.msg.get("yjrguild").toString().replace("&","§").replace("%guild_name%",guild_name));
                            MySQLManager.get().deleteApplyPlayer(player,by_player);
                        }
                        assitGui.ApplyPlayerList_Gui(plugin,player,guild_name,1);
                    }catch (SQLException bc){}
                }
            }catch (NullPointerException|ArrayIndexOutOfBoundsException bc){}
        }else if(ydktitle.equals(confirmGui)){
            e.setCancelled(true);
            switch (e.getRawSlot()){
                case 11:
                    String guild_name = plugin.player_gui.get(player.getName()+".guild_name");
                    String admin_name = plugin.player_gui.get(player.getName()+".admin_name");
                    if(admin_name.equals(player.getName())){
                        MySQLManager.get().deleteGuild(player,guild_name);
                        MySQLManager.get().deleteGuild_All_Player(player,guild_name);
                        MySQLManager.get().deleteGuild_All_Apply(player,guild_name);
                        MySQLManager.get().deleteGuild_DELETE_ALL_SIGN(player,guild_name);
                        player.sendMessage(plugin.msg.get("dissolveguild").toString()
                                .replace("&","§").replace("%player%",player.getName())
                                .replace("%guild_name%",guild_name));
                        player.closeInventory();
                    }else {
                        try {
                            Player admin = Bukkit.getPlayer(admin_name);
                            admin.sendMessage(plugin.msg.get("playerlevelguild").toString()
                                    .replace("&", "§").replace("%player%", player.getName()));
                        } catch (NullPointerException bc) {}
                        MySQLManager.get().deleteGuildPlayer(player, player.getName());
                        MySQLManager.get().deleteGuild_DELETE_SIGN(player, player.getName());

                        plugin.player_cd.add(player.getName());

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                plugin.player_cd.remove(player.getName());
                            }
                        }.runTaskLater(plugin,plugin.signCon.getInt("Sign.time")*20*60*60);


                        player.closeInventory();
                    }
                    break;
                case 15:
                    Command.open(plugin,player,player);
                    break;
                default:

            }
        }else if(ydktitle.contains(transferconGui)){
            e.setCancelled(true);
            switch (e.getRawSlot()){
                case 11:
                    String guild_name = plugin.player_gui.get(player.getName()+".guild_name");
                    String admin_name = plugin.player_gui.get(player.getName()+".admin_name");
                    if(admin_name.equals(player.getName())) {
                        String by_player =  plugin.player_gui.get(player.getName()+".identityGui");
                        MySQLManager.get().update_admin(player, by_player, guild_name);
                        MySQLManager.get().update_identity(player,player.getName(),"default");
                        MySQLManager.get().update_identity(player,by_player,"admin");
                        plugin.player_gui.put(player.getName()+".admin_name",  by_player);
                        Bukkit.broadcastMessage(plugin.msg.get("transfercon").toString().replace("&","§")
                                .replace("%player%",by_player).replace("%guild%",guild_name));
                        player.closeInventory();
                    }
                    break;
                case 15:
                    Command.open(plugin,player,player);
                    break;
                default:

            }
        }else if(ydktitle.equals(identityGui)){
            e.setCancelled(true);
            String admin_name = plugin.player_gui.get(player.getName()+".admin_name");
            String guild_name = plugin.player_gui.get(player.getName()+".guild_name");

            ResultSet mysql = MySQLManager.get().findGuild(player,guild_name);
            mysql.last();
            int oldLevel = mysql.getInt("guild_level");
            int max_elite = plugin.getConfig().getInt("Guild.identityLevel."+oldLevel+".elite");
            int max_grand = plugin.getConfig().getInt("Guild.identityLevel."+oldLevel+".grand");
            int elite_count = 0;
            int grand_count = 0;
            ResultSet mysql1 = MySQLManager.get().listData(player,guild_name);
            while (mysql1.next()){
                if(mysql1.getString("identity").equals("elite")){
                    elite_count++;
                }else if(mysql1.getString("identity").equals("grand")){
                    grand_count++;
                }
            }

            switch (e.getRawSlot()){
                case 11:
                    MySQLManager.get().update_identity(player, plugin.player_gui.get(player.getName() + ".identityGui"), "default");
                    assitGui.playerList_Gui(plugin, admin_name, player, guild_name, 1);
                    break;
                case 13:
                    if(elite_count < max_elite) {
                        MySQLManager.get().update_identity(player,plugin.player_gui.get(player.getName()+".identityGui"),"elite");
                        assitGui.playerList_Gui(plugin, admin_name, player, guild_name, 1);
                    }else{
                        player.sendMessage(plugin.msg.get("maxidentity").toString().replace("&","§")
                                .replace("%count%",String.valueOf(elite_count)).replace("%max_identity%",String.valueOf(max_elite)));
                    }
                    break;
                case 15:
                    if(grand_count < max_grand) {
                        MySQLManager.get().update_identity(player,plugin.player_gui.get(player.getName()+".identityGui"),"grand");
                        assitGui.playerList_Gui(plugin, admin_name, player, guild_name, 1);
                    }else{
                        player.sendMessage(plugin.msg.get("maxidentity").toString().replace("&","§")
                                .replace("%count%",String.valueOf(grand_count)).replace("%max_identity%",String.valueOf(max_grand)));
                    }
                    break;
                case 18:
                    assitGui.transferconGui(plugin,player,plugin.player_gui.get(player.getName()+".identityGui"));
                    break;
                case 26:
                    MySQLManager.get().deleteGuildPlayer(player,plugin.player_gui.get(player.getName()+".identityGui"));
                    player.closeInventory();
                    try {
                        Player byq = Bukkit.getPlayer(plugin.player_gui.get(player.getName()+".identityGui"));
                        byq.sendMessage(plugin.msg.get("kickplayerguild").toString().replace("%admin%",admin_name)
                                .replace("%guild_name%",guild_name).replace("&","§"));
                    }catch (NullPointerException bc){}
                default:
            }
        }else if(ydktitle.equals(levelGui)){
            e.setCancelled(true);
            String admin_name = plugin.player_gui.get(player.getName()+".admin_name");
            String guild_name = plugin.player_gui.get(player.getName()+".guild_name");
            try {
                if(e.getRawSlot() == 15 && e.getCurrentItem().hasItemMeta()) {
                    ResultSet mysql = MySQLManager.get().findGuild(player,guild_name);
                    mysql.last();
                    int oldLevel = mysql.getInt("guild_level");
                    int newLevel = oldLevel+1;
                    int max_limit = plugin.levelCon.getInt("Level."+newLevel+".player_count");

                    ItemStack wp = plugin.levelCon.getItemStack("Level."+newLevel+".item");
                    int price = plugin.levelCon.getInt("Level."+newLevel+".price");

                    if(plugin.econa.has(player,price)){
                        if (player.getInventory().contains(wp)) {
                            plugin.econa.withdrawPlayer(player,price);
                            player.getInventory().removeItem(wp);
                            MySQLManager.get().update_level(player,newLevel,guild_name,max_limit);
                            Bukkit.broadcastMessage(plugin.msg.get("guildlevel").toString().replace("&","§")
                            .replace("%guild_name%",guild_name).replace("%level%",String.valueOf(newLevel)));
                        } else {
                            player.sendMessage(plugin.msg.get("noitem").toString()
                                    .replace("&", "§").replace("%item_name%", wp.getItemMeta().getDisplayName()));
                        }
                    }else{
                        player.sendMessage(plugin.msg.get("nomoney").toString()
                                .replace("&","§").replace("%bz_money%",String.valueOf(price-plugin.econa.getBalance(player))));
                    }
                }
            }catch (NullPointerException | SQLException bc){}
        }
    }
}
