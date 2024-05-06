package wgrp.loader;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import wgrp.WorldGuardRegionProtectBukkitPlugin;
import wgrp.rslibs.api.RSStorage;
import wgrp.rslibs.datasource.Storage;
import wgrp.util.config.Config;

public class WGRPLoadDataBase {

    private final WorldGuardRegionProtectBukkitPlugin wgrpBukkitPlugin;
    private final Config config;

    public WGRPLoadDataBase(final @NotNull WorldGuardRegionProtectBukkitPlugin wgrpBukkitPlugin) {
        this.wgrpBukkitPlugin = wgrpBukkitPlugin;
        this.config = wgrpBukkitPlugin.getConfigLoader().getConfig();

    }

    public void loadDataBase() {
        RSStorage rsStorage = wgrpBukkitPlugin.getRsStorage();
        if (config.getDataBaseEnable()) {
            final long durationTimeStart = System.currentTimeMillis();
            rsStorage.dbLogsSource = new Storage(wgrpBukkitPlugin);
            rsStorage.dbLogs.clear();
            if (rsStorage.dbLogsSource.load()) {
                Bukkit.getLogger().info("[DataBase] The database is loaded.");
                this.postEnable();
                Bukkit.getLogger().info(String.format(
                        "[DataBase] Startup duration: %s ms.", System.currentTimeMillis() - durationTimeStart));
            }
        }
    }

    public void postEnable() {
        Bukkit.getServer().getScheduler().cancelTasks(wgrpBukkitPlugin.getWgrpBukkitBase());
        if (config.getMySQLSettings().getIntervalReload() > 0) {
            wgrpBukkitPlugin.getRsStorage().dbLogsSource.loadAsync();
            Bukkit.getLogger().info("[DataBase] The database is loaded asynchronously.");
        }
    }

}