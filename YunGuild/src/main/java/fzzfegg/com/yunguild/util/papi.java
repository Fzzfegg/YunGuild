package fzzfegg.com.yunguild.util;

import fzzfegg.com.yunguild.YunGuild;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class papi extends PlaceholderExpansion {

    private YunGuild plugin;

    public papi(YunGuild plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "guild";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("jsgw")) {
            return "A";
        }

        return null;
    }


}
