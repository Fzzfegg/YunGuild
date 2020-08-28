package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import fzzfegg.com.yunguild.util.ItemStackTr;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class assitGui {

    //队员列表GUI
    public static void playerList_Gui(YunGuild plugin,String admin_name, Player player,String guildname, int qs) throws SQLException {
        String[] slot = {
                "10", "11", "12", "13", "14", "15", "16",
                "19", "20", "21", "22", "23", "24", "25",
                "28", "29", "30", "31", "32", "33", "34",
        };
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(slot));

        //获取lists_guild数据
        ResultSet mysql = MySQLManager.get().listData(player,guildname);
        mysql.last();
        Double MaxData = Double.valueOf(mysql.getRow());

        String title = plugin.guiCon.getString("ListPlayerGui.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 6 * 9, title);
        List player_list = new ArrayList();

        //翻页起始
        mysql.absolute(qs-1);

        while (mysql.next()){
            ArrayList<String> list2 = (ArrayList<String>) list.clone();
            for(String eachSlot :list2){
                String guild_player_name = mysql.getString("player_name");

                player_list.add(guild_player_name);

                OfflinePlayer guild_player = Bukkit.getPlayer(guild_player_name);
                boolean isHuizhang = guild_player_name.equals(admin_name);
                Map<String, String> kv = new HashMap<>();
                kv.put("&", "§");
                kv.put("%guild_name%", guildname);
                kv.put("%player_name%", guild_player_name);
                kv.put("%player_money%", String.valueOf(plugin.econa.getBalance(guild_player)).split("\\.")[0]);
                kv.put("%editIdentity%","");
                try {
                    guild_player.isOnline();
                    kv.put("%player_level%", String.valueOf(((Player) guild_player).getLevel()));
                    kv.put("%player_health%", String.valueOf(((Player) guild_player).getHealth()).split("\\.")[0]);
                    kv.put("%player_world%", ((Player) guild_player).getWorld().getName());
                    String perfix = PlaceholderAPI.setPlaceholders(guild_player, "%vault_prefix%");
                    String group = PlaceholderAPI.setPlaceholders(guild_player, "%vault_rank%");
                    kv.put("%player_prefix%", perfix);
                    kv.put("%player_group%", group);
                }catch (NullPointerException bc){
                    kv.put("%player_level%", "离线");
                    kv.put("%player_health%","离线");
                    kv.put("%player_world%", "离线");
                    kv.put("%player_prefix%","离线");
                    kv.put("%player_group%", "离线");
                }
                ResultSet mysql2 = MySQLManager.get().findname(player,guild_player_name);
                mysql2.last();
                String identity = mysql2.getString("identity");
                identity = plugin.msg.get("identity."+identity).toString().replace("%guild_name%","").replace("&","§");

                if (isHuizhang) {
                    kv.put("%str%", plugin.msg.get("identity.admin").toString().replace("%guild_name%","").replace("&","§"));
                } else {
                    if(player.getName().equals(admin_name)) {
                        kv.put("%editIdentity%", plugin.msg.get("editIdentity").toString().split(plugin.pluginPrefix)[1]);
                    }
                    kv.put("%str%", identity);
                }
                String type = plugin.guiCon.get("ListPlayerGui.Player.type").toString();
                String name = plugin.guiCon.get("ListPlayerGui.Player.name").toString();
                List lore = plugin.guiCon.getList("ListPlayerGui.Player.lore");
                ItemStack wp = ItemStackTr.getItemStack(type, name, lore, kv);

                inv.setItem(Integer.parseInt(eachSlot), wp);
                list.remove(eachSlot);
                break;
            }
        }


        inv.setItem(47,page_null());
        inv.setItem(51,page_null());
        int Count = (int) Math.ceil(MaxData/21);
        int old_Count = (int) Math.ceil(MaxData-qs)/21;

        int new_Count = (Count-old_Count);

        if(MaxData != 0 && old_Count != 0) {
            if (new_Count == 1) {
                inv.setItem(47, page_null());
                inv.setItem(51, page_next(2));
            } else if (qs + 21 >= MaxData) {
                inv.setItem(47, page_page(new_Count - 1));
                inv.setItem(51, page_null());
            } else if (new_Count > 1) {
                inv.setItem(47, page_page(new_Count - 1));
                inv.setItem(51, page_next(new_Count + 1));
            }
        }
        player.openInventory(inv);
    }

    //申请Gui
    public static void ApplyPlayerList_Gui(YunGuild plugin, Player player,String guildname, int qs) throws SQLException {
        int[] slot = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
        };
        //获取lists_guild数据
        ResultSet mysql = MySQLManager.get().listApplyGuild(player,guildname);
        mysql.last();
        Double MaxData = Double.valueOf(mysql.getRow());

        String title = plugin.guiCon.getString("ApplyPlayerGui.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 6 * 9, title);

        //翻页起始
        mysql.absolute(qs-1);
        for(int eachSlot : slot) {
            if (mysql.next()) {
                String guild_player_name = mysql.getString("player_name");
                Player guild_player = Bukkit.getPlayer(guild_player_name);
                try{
                    guild_player.isOnline();

                    Map<String, String> kv = new HashMap<>();
                    kv.put("&", "§");
                    kv.put("%guild_name%", guildname);
                    kv.put("%player_name%", guild_player_name);
                    kv.put("%player_level%", String.valueOf(((Player) guild_player).getLevel()));
                    kv.put("%player_health%", String.valueOf(((Player) guild_player).getHealth()).split("\\.")[0]);
                    kv.put("%player_money%", String.valueOf(plugin.econa.getBalance(guild_player)).split("\\.")[0]);

                    String getWorldname = PlaceholderAPI.setPlaceholders(guild_player, "%multiverse_world_name%");
                    String perfix = PlaceholderAPI.setPlaceholders(guild_player, "%cmi_user_prefix%");
                    String group = PlaceholderAPI.setPlaceholders(guild_player, "%cmi_user_group%");
                    kv.put("%player_prefix%", perfix);
                    kv.put("%player_world%", getWorldname);
                    kv.put("%player_group%", group);

                    String type = plugin.guiCon.get("ApplyPlayerGui.Player.type").toString();
                    String name = plugin.guiCon.get("ApplyPlayerGui.Player.name").toString();
                    List lore = plugin.guiCon.getList("ApplyPlayerGui.Player.lore");
                    ItemStack wp = ItemStackTr.getItemStack(type, name, lore, kv);

                    inv.setItem(eachSlot, wp);
                }catch (NullPointerException bc){}
            }
        }
        inv.setItem(47,page_null());
        inv.setItem(51,page_null());
        int Count = (int) Math.ceil(MaxData/21);
        int old_Count = (int) Math.ceil(MaxData-qs)/21;

        int new_Count = (Count-old_Count);

        if(MaxData != 0 && old_Count != 0) {
            if (new_Count == 1) {
                inv.setItem(47, page_null());
                inv.setItem(51, page_next(2));
            } else if (qs + 21 >= MaxData) {
                inv.setItem(47, page_page(new_Count - 1));
                inv.setItem(51, page_null());
            } else if (new_Count > 1) {
                inv.setItem(47, page_page(new_Count - 1));
                inv.setItem(51, page_next(new_Count + 1));
            }
        }

        player.openInventory(inv);

    }



    private static ItemStack page_null(){
        ItemStack w = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = w.getItemMeta();
        itemMeta.setDisplayName("§7暂无");
        w.setItemMeta(itemMeta);
        return w;
    }
    private static ItemStack page_next(int page){
        ItemStack bl = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = bl.getItemMeta();
        itemMeta.setDisplayName("§7第"+page+"页");
        bl.setItemMeta(itemMeta);
        return bl;
    }
    private static ItemStack page_page(int page){
        ItemStack bl = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta itemMeta = bl.getItemMeta();
        itemMeta.setDisplayName("§7第"+page+"页");
        bl.setItemMeta(itemMeta);
        return bl;
    }

    public static void identityGui(YunGuild plugin, Player player,String by_player) throws SQLException {
        String title = plugin.guiCon.getString("identityGui.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 3 * 9, title);

        String identity_default = plugin.msg.get("identity.default").toString().replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")).replace("&","§");
        String identity_elite = plugin.msg.get("identity.elite").toString().replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")).replace("&","§");
        String identity_grand = plugin.msg.get("identity.grand").toString().replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")).replace("&","§");
        String identity_admin = plugin.msg.get("identity.admin").toString().replace("%guild_name%",plugin.player_gui.get(player.getName()+".guild_name")).replace("&","§");

        ItemStack default_ = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("identityGui.default.type")));
        ItemMeta defaultMeta = default_.getItemMeta();
        defaultMeta.setDisplayName(plugin.guiCon.getString("identityGui.default.name")
                .replace("%identity%",identity_default).replace("&","§"));
        default_.setItemMeta(defaultMeta);
        inv.setItem(11,default_);

        ItemStack elite = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("identityGui.elite.type")));
        ItemMeta eliteMeta = elite.getItemMeta();
        eliteMeta.setDisplayName(plugin.guiCon.getString("identityGui.elite.name")
                .replace("%identity%",identity_elite).replace("&","§"));
        elite.setItemMeta(eliteMeta);
        inv.setItem(13,elite);

        ItemStack grand = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("identityGui.grand.type")));
        ItemMeta grandMeta = grand.getItemMeta();
        grandMeta.setDisplayName(plugin.guiCon.getString("identityGui.grand.name")
                .replace("%identity%",identity_grand).replace("&","§"));
        grand.setItemMeta(grandMeta);
        inv.setItem(15,grand);

        ItemStack transfer = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("identityGui.transfer.type")));
        ItemMeta transferMeta = transfer.getItemMeta();
        transferMeta.setDisplayName(plugin.guiCon.getString("identityGui.transfer.name")
              .replace("&","§").replace("%player%",by_player));
        transfer.setItemMeta(transferMeta);
        inv.setItem(18,transfer);

        ItemStack level = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("identityGui.level.type")));
        ItemMeta levelMeta = level.getItemMeta();
        levelMeta.setDisplayName(plugin.guiCon.getString("identityGui.level.name")
                .replace("&","§"));
        level.setItemMeta(levelMeta);
        inv.setItem(26,level);

        player.openInventory(inv);
    }

    public static void conGui(YunGuild plugin, Player player) throws SQLException {
        String title = plugin.guiCon.getString("confirmGui.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 3 * 9, title);

        ItemStack ty = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("confirmGui.ty.type")));
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName(plugin.guiCon.getString("confirmGui.ty.name").replace("&","§"));
        ty.setItemMeta(tyMeta);
        inv.setItem(11,ty);

        ItemStack jj = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("confirmGui.jj.type")));
        ItemMeta jjMeta = jj.getItemMeta();
        jjMeta.setDisplayName(plugin.guiCon.getString("confirmGui.jj.name").replace("&","§"));
        jj.setItemMeta(jjMeta);
        inv.setItem(15,jj);

        player.openInventory(inv);
    }

    public static void transferconGui(YunGuild plugin, Player player,String by_player) throws SQLException {
        String title = plugin.guiCon.getString("transferconGui.title").replace("&","§").replace("%player%",by_player);
        Inventory inv = Bukkit.createInventory(player, 3 * 9, title);

        ItemStack ty = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("confirmGui.ty.type")));
        ItemMeta tyMeta = ty.getItemMeta();
        tyMeta.setDisplayName(plugin.guiCon.getString("confirmGui.ty.name").replace("&","§"));
        ty.setItemMeta(tyMeta);
        inv.setItem(11,ty);

        ItemStack jj = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("confirmGui.jj.type")));
        ItemMeta jjMeta = jj.getItemMeta();
        jjMeta.setDisplayName(plugin.guiCon.getString("confirmGui.jj.name").replace("&","§"));
        jj.setItemMeta(jjMeta);
        inv.setItem(15,jj);

        player.openInventory(inv);
    }

    public static void levelGui(YunGuild plugin, Player player) throws SQLException {
        String title = plugin.levelCon.getString("Level.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 3 * 9, title);
        String admin_name = plugin.player_gui.get(player.getName()+".admin_name");
        String guild_name = plugin.player_gui.get(player.getName()+".guild_name");

        ResultSet mysql = MySQLManager.get().findGuild(player,guild_name);
        ResultSet mysql2 = MySQLManager.get().listData(player,guild_name);
        mysql.last();
        mysql2.last();

        int oldLevel = mysql.getInt("guild_level");
        int newLevel = oldLevel+1;
        int player_count = mysql2.getRow();
        int max_player = mysql.getInt("max_limit");
        int chest_count = mysql.getInt("guild_chest");

        String elite_name = plugin.msg.get("identity.elite").toString().replace("%guild_name%","").replace("&","§");
        String grand_name = plugin.msg.get("identity.grand").toString().replace("%guild_name%","").replace("&","§");

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

        Map<String,String> kv = new HashMap<>();
        kv.put("&","§");
        kv.put("%player_count%",String.valueOf(player_count));
        kv.put("%max_count%",String.valueOf(max_player));
        kv.put("%chest_count%",String.valueOf(chest_count));
        kv.put("%elite_name%",elite_name);
        kv.put("%grand_name%",grand_name);
        kv.put("%elite_count%",String.valueOf(elite_count));
        kv.put("%grand_count%",String.valueOf(grand_count));
        kv.put("%max_elite%",String.valueOf(max_elite));
        kv.put("%max_grand%",String.valueOf(max_grand));

        String type = plugin.levelCon.getString("Level."+oldLevel+".type");
        String name = plugin.levelCon.getString("Level.oldname").replace("%level%",String.valueOf(oldLevel));
        List<String> lore = (List<String>) plugin.levelCon.getList("Level."+oldLevel+".lore");
        List<String> list = new ArrayList<>();
        for(String each: lore){
            if(each.contains("升级需求")){
                break;
            }
            list.add(each);
        }

        ItemStack oldWP = ItemStackTr.getItemStack(type,name,list,kv);
        inv.setItem(11,oldWP);

        try {
            if(plugin.levelCon.getString("Level."+newLevel+".type") != null){
                kv.put("%max_count%",String.valueOf(plugin.levelCon.getInt("Level."+newLevel+".player_count")));
                type = plugin.levelCon.getString("Level."+newLevel+".type");
                name = plugin.levelCon.getString("Level.newname").replace("%level%",String.valueOf(newLevel));
                lore = (List<String>) plugin.levelCon.getList("Level."+newLevel+".lore");
                ItemStack newWP = ItemStackTr.getItemStack(type,name,lore,kv);
                inv.setItem(15,newWP);
            }
        }catch (NullPointerException bc){}

        player.openInventory(inv);
    }

}
