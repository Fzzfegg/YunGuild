package fzzfegg.com.yunguild.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemStackTr {

    public static ItemStack getItemStack(String type, String name, List<String> lore, Map kv){
        ItemStack itemStack = new ItemStack(Material.matchMaterial(type));
        ItemMeta itemMeta = itemStack.getItemMeta();
        name = replaceAll(name,kv);
        itemMeta.setDisplayName(name);
        List<String> Lore = new ArrayList<>();
        for(String each : lore){
            Lore.add(replaceAll(each,kv));
        }
        itemMeta.setLore(Lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private static String replaceAll(String str,Map kv){
        for(Object key : kv.keySet()){
            str = str.replace(key.toString(),kv.get(key).toString());
        }
        return str;
    }

}
