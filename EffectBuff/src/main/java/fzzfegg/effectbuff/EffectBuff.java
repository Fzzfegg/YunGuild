package fzzfegg.effectbuff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class EffectBuff extends JavaPlugin {

    public static EffectBuff instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents((Listener)new Eccet(), (Plugin)this);
        Bukkit.getPluginCommand("eff").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[0].equalsIgnoreCase("reload")) {
            setEnabled(false);
            reloadConfig();
            setEnabled(true);
            sender.sendMessage("§a配置文件已重载");
        }return true;}

    @Override
    public void onDisable() {
    }
}
