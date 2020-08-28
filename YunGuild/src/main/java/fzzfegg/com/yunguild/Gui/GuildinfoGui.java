package fzzfegg.com.yunguild.Gui;

import fzzfegg.com.yunguild.Sql.MySQLManager;
import fzzfegg.com.yunguild.YunGuild;
import fzzfegg.com.yunguild.util.ItemStackTr;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildinfoGui {

    public static void Gui_infoGuild(YunGuild plugin, Player player,String guild_name,int max_player,int guild_level,int guild_chest) throws SQLException {

        FileConfiguration yml = plugin.guiCon;

        ResultSet mysql = MySQLManager.get().findGuild(player,guild_name);
        ResultSet listData = MySQLManager.get().listData(player,guild_name);

        listData.last();
        mysql.first();

        //是否是会长
        Boolean isHuizhang = mysql.getString("admin_name").equals(player.getName());

        String title;
        try {
            title = mysql.getString("guild_title")
                    .replace("guild_name",guild_name).replace("&","§");
        }catch (NullPointerException | SQLException bc){
            title = yml.getString("GuildGui.title").replace("%guild_name%",guild_name).replace("&","§");;
        }

        Inventory inv = Bukkit.createInventory(player, 5 * 9, title);

        Map<String,String> kv = new HashMap<>();
        kv.put("&","§");
        kv.put("%guild_name%",guild_name);
        kv.put("%player_name%",player.getName());
        kv.put("%player_count%",String.valueOf(listData.getRow()));
        kv.put("%max_player_count%",String.valueOf(max_player));
        kv.put("%guild_level%",String.valueOf(guild_level));
        kv.put("%guild_chest%",String.valueOf(guild_chest));
        kv.put("%issue_price%",String.valueOf(plugin.getConfig().getInt("Guild.issueZhaojiling")));
        kv.put("%sign_info%",plugin.signCon.getString("Sign.Msg."+guild_level).replace("&","§"));

        if(isHuizhang){
            kv.put("%str%","解散");
            kv.put("%catAccept%",plugin.msg.get("catAccept").toString().split(plugin.pluginPrefix)[1]);
        }else{
            kv.put("%str%","退出");
            kv.put("%catAccept%","");
        }

        String list_type = yml.getString("GuildGui.List.type");
        String list_name = yml.getString("GuildGui.List.name");
        List list_lore = yml.getList("GuildGui.List.lore");
        ItemStack list = ItemStackTr.getItemStack(list_type,list_name,list_lore,kv);
        inv.setItem(10,list);

        String flag_type = yml.getString("GuildGui.Flag.type");
        String flag_name = yml.getString("GuildGui.Flag.name");
        List flag_lore = yml.getList("GuildGui.Flag.lore");
        ItemStack flag = ItemStackTr.getItemStack(flag_type,flag_name,flag_lore,kv);
        inv.setItem(12,flag);

        String info_type = yml.getString("GuildGui.Info.type");
        String info_name = yml.getString("GuildGui.Info.name");
        List info_lore = yml.getList("GuildGui.Info.lore");
        ItemStack info = ItemStackTr.getItemStack(info_type,info_name,info_lore,kv);
        inv.setItem(14,info);

        String home_type = yml.getString("GuildGui.Home.type");
        String home_name = yml.getString("GuildGui.Home.name");
        List home_lore = yml.getList("GuildGui.Home.lore");
        ItemStack home = ItemStackTr.getItemStack(home_type,home_name,home_lore,kv);
        inv.setItem(16,home);

        String chest_type = yml.getString("GuildGui.Chest.type");
        String chest_name = yml.getString("GuildGui.Chest.name");
        List chest_lore = yml.getList("GuildGui.Chest.lore");
        ItemStack chest = ItemStackTr.getItemStack(chest_type,chest_name,chest_lore,kv);
        inv.setItem(28,chest);

        String sign_type = yml.getString("GuildGui.Sign.type");
        String sign_name = yml.getString("GuildGui.Sign.name");
        List sign_lore = yml.getList("GuildGui.Sign.lore");
        ItemStack sign = ItemStackTr.getItemStack(sign_type,sign_name,sign_lore,kv);
        inv.setItem(30,sign);

        String tpall_type = yml.getString("GuildGui.Tpall.type");
        String tpall_name = yml.getString("GuildGui.Tpall.name");
        List tpall_lore = yml.getList("GuildGui.Tpall.lore");
        ItemStack tpall = ItemStackTr.getItemStack(tpall_type,tpall_name,tpall_lore,kv);
        inv.setItem(32,tpall);

        String leave_type = yml.getString("GuildGui.Leave.type");
        String leave_name = yml.getString("GuildGui.Leave.name");
        List leave_lore = yml.getList("GuildGui.Leave.lore");
        ItemStack leave = ItemStackTr.getItemStack(leave_type,leave_name,leave_lore,kv);
        inv.setItem(34,leave);

        String player_type = yml.getString("GuildGui.Player.type");
        String player_name = yml.getString("GuildGui.Player.name");
        List player_lore = yml.getList("GuildGui.Player.lore");
        ItemStack playerwp = ItemStackTr.getItemStack(player_type,player_name,player_lore,kv);
        inv.setItem(22,playerwp);

        player.openInventory(inv);




    }


}
