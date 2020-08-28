package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.YunGuild;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;


public class NoneGuild {

    public static void Gui_createOrlist(YunGuild plugin,Player p) {
        String title = plugin.guiCon.getString("NoneGui.title").replace("&","§");
        Inventory inv = Bukkit.createInventory(p, 3 * 9, title);

        ItemStack createGuild = new ItemStack(
                Material.matchMaterial(plugin.guiCon.get("NoneGui.createGuild.type").toString())
        );
        ItemMeta createMeta = createGuild.getItemMeta();
        createMeta.setDisplayName(plugin.guiCon.get("NoneGui.createGuild.name").toString().replace("&","§"));
        List newLore = new ArrayList<>();
        for(String eachlore : plugin.guiCon.getStringList("NoneGui.createGuild.lore")){
            newLore.add(eachlore.replace("&","§"));
        }
        createMeta.setLore(newLore);
        createGuild.setItemMeta(createMeta);
        inv.setItem(11,createGuild);

        ItemStack listGuild = new ItemStack(
                Material.matchMaterial(plugin.guiCon.get("NoneGui.listGuild.type").toString())
        );
        ItemMeta listMeta = listGuild.getItemMeta();
        listMeta.setDisplayName(plugin.guiCon.get("NoneGui.listGuild.name").toString().replace("&","§"));
        newLore = new ArrayList<>();
        for(String eachlore : plugin.guiCon.getStringList("NoneGui.listGuild.lore")){
            newLore.add(eachlore.replace("&","§"));
        }
        listMeta.setLore(newLore);
        listGuild.setItemMeta(listMeta);
        inv.setItem(15,listGuild);

        p.openInventory(inv);

    }



}
