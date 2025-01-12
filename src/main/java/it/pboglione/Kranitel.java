/*
 *
 *  Kranitel.java
 *  This file is part of Kranitel by IlGrandeAnonimo
 *  Copyright (C) 2024 IlGrandeAnonimo <ilgrandeanonimo@icloud.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.pboglione;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import it.pboglione.configuration.Settings;
import it.pboglione.configuration.Messages;
import it.pboglione.configuration.records.DefaultRule;
import lombok.Getter;
import net.william278.uniform.paper.PaperUniform;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
@SuppressWarnings("unused")
public final class Kranitel extends JavaPlugin {
    @Getter
    private static Kranitel instance;
    private Messages messages;
    private Settings settings;
    private List<DefaultRule> defaultRules;

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
        loadMessages();
        registerCommands();
        getServer().getPluginManager().registerEvents(
                new ServerLoadListener(this), this);
    }

    public void loadConfiguration() {
        this.settings = YamlConfigurations.update(
                new File(getDataFolder(), "config.yml").toPath(),
                Settings.class,
                YamlConfigurationProperties.newBuilder()
                        .header(Settings.HEADER)
                        .charset(StandardCharsets.UTF_8)
                        .build()
        );
    }

    public void saveConfiguration() {
        YamlConfigurations.save(
                new File(this.getDataFolder(), "config.yml").toPath(),
                Settings.class,
                settings,
                YamlConfigurationProperties.newBuilder()
                        .header(Settings.HEADER)
                        .build());
    }

    public void loadMessages() {
        final File messagesFile = new File(getDataFolder(), "messages.yml");
        this.messages = YamlConfigurations.update(
                messagesFile.toPath(),
                Messages.class,
                YamlConfigurationProperties.newBuilder()
                        .header(Messages.HEADER)
                        .charset(StandardCharsets.UTF_8)
                        .build()
        );
    }

    public void saveMessages() {
        YamlConfigurations.save(
                new File(this.getDataFolder(), "messages.yml").toPath(),
                Messages.class,
                this.messages,
                YamlConfigurationProperties.newBuilder()
                        .header(Messages.HEADER)
                        .build());
    }

    public void applyRules() {
        settings.getPermissions()
                .forEach((name, rule) ->
                        new Permission(name, rule.description(), rule.isDefault())
                );
        final CommandMap commandMap = getServer().getCommandMap();
        settings.getCommmands()
                .forEach((name, rule) -> {
                    String fullName = String.format("%s:%s", rule.namespace(), name);
                    Command command = commandMap.getCommand(fullName);

                    if(command == null) {
                        getLogger().warning(fullName + " not found");
                        return;
                    }
                    defaultRules.add(
                            new DefaultRule(rule.namespace(), name, command.getPermission())
                    );
                    command.setPermission(rule.permission());
                });
    }

    public void reapplyRules() {
        final CommandMap commandMap = getServer().getCommandMap();
        defaultRules.forEach(rule -> {
            String fullName = String.format("%s:%s", rule.namespace(), rule.command());
            Command command = commandMap.getCommand(fullName);
            if(command != null) {
                command.setPermission(rule.permission());
            }
        });
        defaultRules.clear();
        applyRules();
    }

    public void registerCommands() {
        PaperUniform.getInstance(this)
                .register(new KranitelCommand());
    }
}
