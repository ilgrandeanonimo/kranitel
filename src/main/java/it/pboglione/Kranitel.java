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
import it.pboglione.commands.MainCommand;
import it.pboglione.configuration.Config;
import it.pboglione.configuration.Messages;
import lombok.Getter;
import net.william278.uniform.paper.PaperUniform;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Getter
@SuppressWarnings("unused")
public final class Kranitel extends JavaPlugin {
    @Getter
    private static Kranitel instance;
    private Messages messages;
    private Config configuration;

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
        this.configuration = YamlConfigurations.update(
                new File(getDataFolder(), "config.yml").toPath(),
                Config.class,
                YamlConfigurationProperties.newBuilder()
                        .header(Config.HEADER)
                        .charset(StandardCharsets.UTF_8)
                        .build()
        );
    }

    public void saveConfiguration() {
        YamlConfigurations.save(
                new File(this.getDataFolder(), "config.yml").toPath(),
                Config.class,
                configuration,
                YamlConfigurationProperties.newBuilder()
                        .header(Config.HEADER)
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
        configuration.getPermissions()
                .forEach((name, rule) ->
                        new Permission(name, rule.description(), rule.isDefault())
                );
        final CommandMap commandMap = getServer().getCommandMap();
        configuration.getCommmands()
                .forEach((name, rule) -> {
                    String fullName = String.format("%s:%s", rule.namespace(), name);
                    Command command = commandMap.getCommand(fullName);

                    if(command == null) {
                        getLogger().warning(fullName + " not found");
                        return;
                    }

                    command.setPermission(rule.permission());
                });
    }

    public void registerCommands() {
        PaperUniform.getInstance(this)
                .register(new MainCommand());
    }
}
