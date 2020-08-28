package fzzfegg.com.yunguild;

import fzzfegg.com.yunguild.Api.getGuild;
import fzzfegg.com.yunguild.Gui.sonGuiEvent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import fzzfegg.com.yunguild.Gui.GuiEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import fzzfegg.com.yunguild.Command.*;
import fzzfegg.com.yunguild.util.*;
import fzzfegg.com.yunguild.Sql.*;


import java.io.File;
import java.util.*;

public final class YunGuild extends JavaPlugin implements Listener {

    public static YunGuild instance;
    public Economy econa = null;
    public Chat chat = null;



    public File dabaseFile;
    public FileConfiguration dabaseCon;
    public File msgFile;
    public FileConfiguration msgCon;
    public File guiFile;
    public FileConfiguration guiCon;
    public File levelFile;
    public FileConfiguration levelCon;
    public File signFile;
    public FileConfiguration signCon;

    public List player_cd = new ArrayList<>();
    //player 对应 gui
    public Map<String,String> player_gui = new HashMap();
    //玩家 对应 已经进入过征集令的
    public Map<String, List> ycs = new HashMap();
    //CD冷却 0 取消
    public List cd = new ArrayList();

    public Set inPlayerInput = new HashSet<>();
    public Map msg = new HashMap();
    public String pluginPrefix = null;

    public String chatPrefix;
    public String msgFormat;
    public List<String> chatHover;

    //随机字符串 作为密钥
    public String ranStr = fzzfegg.com.yunguild.util.ranStr.getRanStr();

    public String[] fg;

    public fzzfegg.com.yunguild.Api.getGuild getGuild;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("database.yml", false);
        saveResource("msg.yml", false);
        saveResource("gui.yml", false);
        saveResource("level.yml", false);
        saveResource("sign.yml", false);
        instance = this;

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new papi(this).register();
        }

        getServer().getPluginManager().registerEvents(new GuiEvent(), this);
        getServer().getPluginManager().registerEvents(new listener(), this);
        getServer().getPluginManager().registerEvents(new sonGuiEvent(), this);
        getServer().getPluginManager().registerEvents(new chat(), this);
        Bukkit.getPluginCommand("Guild").setExecutor((CommandExecutor) new Command());

        //重载类
        reload.reloada(instance);
        //防止mysql链接超时
        timeMysql.sch(this);

        getGuild = new getGuild();

        setupEconomy();
        setupChat();

    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econa = rsp.getProvider();
    }
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    @Override
    public void onDisable() {
        MySQLManager.get().shutdown(); //断开连接
    }

}
