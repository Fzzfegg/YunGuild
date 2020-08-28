package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;


public class flagGui {


    //队员列表GUI
    public static void flag_Gui(YunGuild plugin, Player player,String guild_name) throws SQLException {

        ResultSet mysql = MySQLManager.get().findGuild(player,guild_name);
        mysql.last();

        String title = plugin.guiCon.getString("GuildFlag.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 3 * 9, title);

        String pvp_flag;

        if(mysql.getString("flag_pvp").equals("true")){
            pvp_flag = "开启";
        }else{
            pvp_flag = "关闭";
        }

        ItemStack item_pvp = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("GuildFlag.pvp.type")));
        ItemMeta itemMeta = item_pvp.getItemMeta();
        itemMeta.setDisplayName(plugin.guiCon.getString("GuildFlag.pvp.name")
                .replace("&","§").replace("%boolean%",pvp_flag));
        item_pvp.setItemMeta(itemMeta);
        inv.setItem(10,item_pvp);

        ItemStack item_settp = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("GuildFlag.settp.type")));
        ItemMeta item_settp_1 = item_settp.getItemMeta();
        item_settp_1.setDisplayName(plugin.guiCon.getString("GuildFlag.settp.name")
                .replace("&","§"));
        item_settp.setItemMeta(item_settp_1);
        inv.setItem(12,item_settp);

        ItemStack item_prefix = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("GuildFlag.prefix.type")));
        ItemMeta item_prefix_1 = item_settp.getItemMeta();
        item_prefix_1.setDisplayName(plugin.guiCon.getString("GuildFlag.prefix.name")
                .replace("&","§"));
        item_prefix.setItemMeta(item_prefix_1);
        inv.setItem(14,item_prefix);

        ItemStack item_editinfo = new ItemStack(Material.matchMaterial(plugin.guiCon.getString("GuildFlag.editinfo.type")));
        ItemMeta item_editinfo_meta = item_editinfo.getItemMeta();
        item_editinfo_meta.setDisplayName(plugin.guiCon.getString("GuildFlag.editinfo.name")
                .replace("&","§"));
        item_editinfo.setItemMeta(item_editinfo_meta);
        inv.setItem(16,item_editinfo);

        player.openInventory(inv);

    }


}
