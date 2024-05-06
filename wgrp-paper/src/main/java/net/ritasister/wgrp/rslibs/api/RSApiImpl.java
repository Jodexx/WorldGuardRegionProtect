package net.ritasister.wgrp.rslibs.api;

import net.ritasister.wgrp.WorldGuardRegionProtectBukkitPlugin;
import net.ritasister.wgrp.api.RSApi;
import net.ritasister.wgrp.core.RegionActionImpl;
import net.ritasister.wgrp.core.api.config.Container;
import net.ritasister.wgrp.rslibs.checker.entity.EntityCheckType;
import net.ritasister.wgrp.rslibs.checker.entity.EntityCheckTypeProvider;
import net.ritasister.wgrp.rslibs.permissions.UtilPermissions;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RSApiImpl implements RSApi<Entity, Player, Cancellable> {

    private final WorldGuardRegionProtectBukkitPlugin wgrpBukkitPlugin;

    private final EntityCheckTypeProvider entityCheckTypeProvider;

    private final Container messages;

    public RSApiImpl(final @NotNull WorldGuardRegionProtectBukkitPlugin wgrpBukkitPlugin) {
        this.wgrpBukkitPlugin = wgrpBukkitPlugin;
        this.messages = wgrpBukkitPlugin.getConfigLoader().getMessages();
        this.entityCheckTypeProvider = new EntityCheckTypeProvider(wgrpBukkitPlugin);
    }

    public boolean isPlayerListenerPermission(@NotNull Player player, @NotNull UtilPermissions perm) {
        return !player.hasPermission(perm.getPermissionName());
    }

    public boolean isEntityListenerPermission(@NotNull Entity entity, @NotNull UtilPermissions perm) {
        return !entity.hasPermission(perm.getPermissionName());
    }

    /**
     * Initializes all used NMS classes, constructors, fields and methods.
     * Returns {@code true} if everything went successfully and version marked as compatible,
     * {@code false} if anything went wrong or version not marked as compatible.
     *
     * @return {@code true} if server version compatible, {@code false} if not
     */
    public boolean isVersionSupported() {
        List<String> supportedVersions = List.of("v1_20_R4");
        String supportedVersionRange = "1.20.5 - 1.20.6";
        String serverPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            long time = System.currentTimeMillis();
            if (supportedVersions.contains(serverPackage)) {
                wgrpBukkitPlugin.getPluginLogger().info("Loaded NMS hook in " + (System.currentTimeMillis() - time) + "ms");
                return true;
            } else {
                wgrpBukkitPlugin.getPluginLogger().info(
                        "No compatibility issue was found, but this plugin version does not claim to support your server package (" + serverPackage + ").");
            }
        } catch (Exception ex) {
            if (supportedVersions.contains(serverPackage)) {
                wgrpBukkitPlugin.getPluginLogger().severe(
                        "Your server version is marked as compatible, but a compatibility issue was found. Please report the error below (include your server version & fork too)");
            } else {
                wgrpBukkitPlugin.getPluginLogger().severe(
                        "Your server version is completely unsupported. This plugin version only " +
                        "supports " + supportedVersionRange + ". Disabling.");
            }
        }
        return false;
    }

    /**
     * Send notification to admin.
     *
     * @param player        player object.
     * @param playerName    player name.
     * @param senderCommand name command if player attempt to use in a region.
     * @param regionName    the region name, if player attempts to use command in a region.
     */
    public void notify(Player player, String playerName, String senderCommand, String regionName) {
        if (regionName == null) {
            return;
        }
        if (wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandNotifyAdminEnable() && this.isPlayerListenerPermission(
                player,
                UtilPermissions.REGION_PROTECT_NOTIFY_ADMIN)) {
            String cmd = wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandList().toString();
            if (cmd.contains(senderCommand.toLowerCase()) && wgrpBukkitPlugin
                    .getConfigLoader()
                    .getConfig()
                    .getSpyCommandNotifyAdminPlaySoundEnable()) {
                player.playSound(
                        player.getLocation(),
                        wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandNotifyAdminPlaySound().toLowerCase(),
                        1,
                        1
                );
                messages.get("messages.Notify.sendAdminInfoIfUsedCommandInRG")
                        .replace("<player>", playerName)
                        .replace("<cmd>", cmd)
                        .replace("<region>", regionName).send(player);
            }
        }
    }

    /**
     * Send notify to admin.
     *
     * @param playerName    player name.
     * @param senderCommand name command if Player attempt to use in a region.
     * @param regionName    region name, if Player attempts to use command in a region.
     */
    public void notify(String playerName, String senderCommand, String regionName) {
        if (regionName == null) {
            return;
        }
        if (wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandNotifyConsoleEnable()) {
            String cmd = wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandList().toString();
            if(cmd.contains(senderCommand.toLowerCase())) {
                ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
                messages.get("messages.Notify.sendAdminInfoIfUsedCommandInRG")
                        .replace("<player>", playerName)
                        .replace("<cmd>", cmd)
                        .replace("<region>", regionName).send(consoleSender);
            }
        }
    }

    /**
     * Send a notification to the administrator if Player attempts to interact with a region from WorldGuard.
     *
     * @param admin         message for an admin who destroys a region.
     * @param suspectPlayer object player for method.
     * @param suspectName   player name who interacting with a region.
     * @param action        get the actions.
     * @param regionName    region name.
     * @param x             position of block.
     * @param y             position of block.
     * @param z             position of block.
     * @param world         position of block in world.
     */
    public void notifyIfActionInRegion(
            Player admin,
            Player suspectPlayer,
            String suspectName,
            RegionActionImpl action,
            String regionName,
            double x,
            double y,
            double z,
            String world) {
        if (this.isPlayerListenerPermission(suspectPlayer, UtilPermissions.SPY_INSPECT_FOR_SUSPECT)
                && wgrpBukkitPlugin.getConfigLoader().getConfig().getSpyCommandNotifyAdminEnable()) {
            messages.get("messages.Notify.sendAdminInfoIfActionInRegion")
                    .replace("<player>", suspectName)
                    .replace("<action>", action.getAction())
                    .replace("<region>", regionName)
                    .replace("<x>", String.valueOf(x))
                    .replace("<y>", String.valueOf(y))
                    .replace("<z>", String.valueOf(z))
                    .replace("<world>", world).send(admin);
        }
    }

    public void entityCheck(Cancellable cancellable, Entity entity, @NotNull Entity checkEntity) {
        if (wgrpBukkitPlugin.getRegionAdapter().checkStandingRegion(
                checkEntity.getLocation(),
                wgrpBukkitPlugin.getConfigLoader().getConfig().getRegionProtectMap()
        )
                && wgrpBukkitPlugin.getRsApi().isEntityListenerPermission(entity, UtilPermissions.REGION_PROTECT)) {
            EntityCheckType entityCheckType = entityCheckTypeProvider.getCheck(checkEntity);
            if(entityCheckType.check(checkEntity)) {
                cancellable.setCancelled(true);
            }
        }
    }

}
