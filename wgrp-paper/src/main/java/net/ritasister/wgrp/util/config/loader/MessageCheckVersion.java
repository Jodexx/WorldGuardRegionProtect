package net.ritasister.wgrp.util.config.loader;

import net.ritasister.wgrp.util.config.CheckVersionLang;
import net.ritasister.wgrp.util.config.Config;
import net.ritasister.wgrp.util.config.ParamsVersionCheck;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class MessageCheckVersion implements CheckVersionLang<Plugin> {

    private final ParamsVersionCheck paramsVersionCheck;

    public MessageCheckVersion(ParamsVersionCheck paramsVersionCheck) {
        this.paramsVersionCheck = paramsVersionCheck;
    }

    @Override
    public void checkVersionLang(final @NotNull Plugin wgrpBukkitBase, final @NotNull Config config) {
        String lang = config.getLang();
        File currentLangFile = new File(wgrpBukkitBase.getDataFolder(), "lang/" + lang + ".yml");
        InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(wgrpBukkitBase.getResource("lang/" + lang + ".yml")));
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(inputStreamReader);
        var currentYaml = YamlConfiguration.loadConfiguration(currentLangFile);
        if (currentLangFile.exists() && !paramsVersionCheck.getCurrentVersion(currentYaml).equals(Objects.requireNonNull(paramsVersionCheck.getNewVersion(yamlConfiguration)))) {
            Bukkit.getLogger().info("Found new version of lang file, updating this now...");
            Path renameOldLang = new File(wgrpBukkitBase.getDataFolder(),
                    "lang/" + lang + "-old-" + paramsVersionCheck.getSimpleDateFormat() + ".yml").toPath();
            try {
                Files.move(currentLangFile.toPath(), renameOldLang, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            wgrpBukkitBase.saveResource("lang/" + lang + ".yml", true);
        } else {
            Bukkit.getLogger().info("No update is required for the lang file");
        }
    }

}