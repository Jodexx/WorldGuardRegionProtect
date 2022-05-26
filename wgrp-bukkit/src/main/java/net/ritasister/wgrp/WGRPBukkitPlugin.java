package net.ritasister.wgrp;

import net.ritasister.wgrp.rslibs.util.Metrics;
import net.ritasister.wgrp.rslibs.util.UpdateChecker;
import net.ritasister.wgrp.rslibs.datasource.Storage;
import net.ritasister.wgrp.util.config.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class WGRPBukkitPlugin extends JavaPlugin {

    private WorldGuardRegionProtect wgRegionProtect;

    @Override
    public void onEnable() {
        wgRegionProtect = new WorldGuardRegionProtect(this);
        this.getWgRegionProtect().load();
    }

    @Override
    public void onDisable() {
        getWgRegionProtect().getUtilConfig().getConfig().saveConfig();
    }

    public void notifyPreBuild() {
        if(this.getWgRegionProtect().getPluginVersion().contains("pre")) {
            getWgRegionProtect().getRsApi().getLogger().warn(
                    """
                        This is a test build. This building may be unstable!
                        When reporting a bug:
                        Use the issue tracker! Don't report bugs in the reviews.
                        Please search for duplicates before reporting a new https://github.com/RSTeamCore/WorldGuardRegionProtect/issues!
                        Provide as much information as possible.
                        Provide your WorldGuardRegionProtect version and Spigot/Paper version.
                        Provide any stack traces or "errors" using pastebin.""");
        } else {
            getWgRegionProtect().getRsApi().getLogger().info("This is the latest stable building.");
        }
        getWgRegionProtect().getRsApi().getLogger().info(
                String.format("""
                        Using %s language version %s
                        Author of this localization - %s
                        """,
                        Message.getLangProperties().getLanguage(),
                        Message.getLangProperties().getVersion(),
                        Message.getLangProperties().getAuthor()));
    }

    public void checkUpdate() {
        new UpdateChecker(this, 81321).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getWgRegionProtect().getRsApi().getLogger().info("&6==============================================");
                getWgRegionProtect().getRsApi().getLogger().info("&2Current version: &b<pl_ver>".replace("<pl_ver>", getWgRegionProtect().getPluginVersion()));
                getWgRegionProtect().getRsApi().getLogger().info("&2This is the latest version of plugin.");
                getWgRegionProtect().getRsApi().getLogger().info("&6==============================================");
            }else{
                getWgRegionProtect().getRsApi().getLogger().info("&6==============================================");
                getWgRegionProtect().getRsApi().getLogger().info("&eThere is a new version update available.");
                getWgRegionProtect().getRsApi().getLogger().info("&cCurrent version: &4<pl_ver>".replace("<pl_ver>", getWgRegionProtect().getPluginVersion()));
                getWgRegionProtect().getRsApi().getLogger().info("&3New version: &b<new_pl_ver>".replace("<new_pl_ver>", version));
                getWgRegionProtect().getRsApi().getLogger().info("&ePlease, download new version here:");
                getWgRegionProtect().getRsApi().getLogger().info("&ehttps://www.spigotmc.org/resources/81321/");
                getWgRegionProtect().getRsApi().getLogger().info("&6==============================================");
            }
        });
    }

    public void checkUpdateNotifyPlayer(Player player) {
        new UpdateChecker(this, 81321).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                player.sendMessage(String.format(wgRegionProtect.getChatApi().getColorCode("""
                &e======&8[&cWorldGuardRegionProtect&8]&e======
                          &6Current version: &b%s
                       &6You are using the latest version.
                &e===================================
                """), version));
            }else{
                player.sendMessage(String.format(wgRegionProtect.getChatApi().getColorCode("""
                &e========&8[&cWorldGuardRegionProtect&8]&e========
                &6 There is a new version update available.
                &c        Current version: &4%s
                &3          New version: &b%s
                &6   Please, download new version here
                &6 https://www.spigotmc.org/resources/81321/
                &e=======================================
                """), getWgRegionProtect().getPluginVersion(), version));
            }
        });
    }

    public void loadDataBase() {
        if(this.getWgRegionProtect().getUtilConfig().getConfig().getDataBaseEnable()) {
            final long duration_time_start = System.currentTimeMillis();
            this.getWgRegionProtect().getRsStorage().dbLogsSource = new Storage(this.getWgRegionProtect());
            this.getWgRegionProtect().getRsStorage().dbLogs.clear();
            if (this.getWgRegionProtect().getRsStorage().dbLogsSource.load()) {
                getWgRegionProtect(). getRsApi().getLogger().info("[DataBase] The player base is loaded.");
                this.postEnable();
                getWgRegionProtect().getRsApi().getLogger().info("[DataBase] Startup duration: <TIME> мс.".replace("<TIME>", String.valueOf(System.currentTimeMillis() - duration_time_start)));
            }
        }
    }

    public void postEnable() {
        getServer().getScheduler().cancelTasks(this);
        if (this.getWgRegionProtect().getUtilConfig().getConfig().getMySQLSettings().getIntervalReload() > 0) {
            getWgRegionProtect().getRsStorage().dbLogsSource.loadAsync();
            getWgRegionProtect().getRsApi().getLogger().info("[DataBase] The base is loaded asynchronously.");
        }
    }

    public void loadMetrics() {
        int pluginId = 12975;
        new Metrics(this, pluginId);
    }

    private WorldGuardRegionProtect getWgRegionProtect() {
        return this.wgRegionProtect;
    }
}