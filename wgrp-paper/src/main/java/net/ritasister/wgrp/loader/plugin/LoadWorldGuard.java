package net.ritasister.wgrp.loader.plugin;

import net.ritasister.wgrp.WorldGuardRegionProtectPaperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Utility class for load WorldGuard by WorldGuardRegionProtect.
 */
public class LoadWorldGuard {

    private final WorldGuardRegionProtectPaperPlugin wgrpPlugin;

    public LoadWorldGuard(final WorldGuardRegionProtectPaperPlugin wgrpPlugin) {
        this.wgrpPlugin = wgrpPlugin;
    }

    /**
     * Load methods for catch WorldGuard.
     */
    public void loadPlugin() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin != null && plugin.isEnabled()) {
            wgrpPlugin.getLogger().info(String.format("Plugin: %s loaded successful!.", plugin.getName()));
        }
    }

}
