package net.ritasister.wgrp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.ritasister.wgrp.loader.WGRPInitialization;
import net.ritasister.wgrp.loader.WGRPLoaderCommandsAndListeners;
import net.ritasister.wgrp.loader.impl.InitializationImpl;
import net.ritasister.wgrp.loader.impl.LoaderCommandsAndListenersImpl;
import org.bukkit.Bukkit;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WorldGuardRegionProtect {

    private final WGRPBukkitPlugin wgrpBukkitPlugin;

    @Getter
    private WGRPContainer wgrpContainer;

    public void load() {
        this.wgrpContainer = new WGRPContainer(this);

        InitializationImpl<WorldGuardRegionProtect> wgrpInitialization = new WGRPInitialization();
        wgrpInitialization.wgrpInit(this);

        LoaderCommandsAndListenersImpl<WorldGuardRegionProtect> loader = new WGRPLoaderCommandsAndListeners();
        loader.loadCommands(this);
        loader.loadListeners(this);
    }

    public void unLoad() {
        if (getWgrpContainer().getConfigLoader() == null) {
            try {
                getWgrpContainer().getConfigLoader().getConfig().saveConfig();
            } catch (NullPointerException ignored) {
                Bukkit.getLogger().info("Cannot save config, because config is not loaded!");
            }
        }
    }

    public WGRPBukkitPlugin getWGRPBukkitPlugin() {
        return this.wgrpBukkitPlugin;
    }

}
