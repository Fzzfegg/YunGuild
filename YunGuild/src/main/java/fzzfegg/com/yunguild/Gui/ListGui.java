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
import java.util.ArrayList;
import java.util.List;

public class ListGui {


    public static void Gui_listGuild(YunGuild plugin, Player player,int qs) throws SQLException {
        int[] slot = {
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
        };

        //获取lists_guild数据
        ResultSet mysql = MySQLManager.get().listsData(player);
        mysql.last();
        Double MaxData = Double.valueOf(mysql.getRow());

        String title = plugin.guiCon.getString("SelectGuild.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(player, 6 * 9, title);

        //翻页起始
        mysql.absolute(qs-1);
        for(int eachSlot : slot) {
            if (mysql.next()) {
                ResultSet listData = MySQLManager.get().listData(player,mysql.getString("guild_name"));
                listData.last();
                ItemStack createGuild = new ItemStack(
                        Material.matchMaterial(plugin.guiCon.get("SelectGuild.guildInfo.type").toString())
                );
                ItemMeta createMeta = createGuild.getItemMeta();

                createMeta.setDisplayName(plugin.guiCon.get("SelectGuild.guildInfo.name").toString()
                        .replace("&", "§")
                        .replace("%guild%", mysql.getString("guild_name")));
                List newLore = new ArrayList<>();
                for (String eachlore : plugin.guiCon.getStringList("SelectGuild.guildInfo.lore")) {
                    newLore.add(eachlore.replace("&", "§")
                            .replace("%guild_info%", mysql.getString("guild_info"))
                            .replace("%guild_number%", String.valueOf(listData.getRow()))
                            .replace("%guild_max_number%", String.valueOf(mysql.getInt("max_limit")))
                            .replace("%guild_admin%", mysql.getString("admin_name"))
                            .replace("%guild_level%", mysql.getString("guild_level")));
                }
                createMeta.setLore(newLore);
                createGuild.setItemMeta(createMeta);

                inv.setItem(eachSlot, createGuild);
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



}
