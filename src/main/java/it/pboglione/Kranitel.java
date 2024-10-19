/*
 *
 *  Kranitel.java
 *  This file is part of Kranitel by IlGrandeAnonimo
 *  Copyright (C) 2024 Paolo Boglione
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
import it.pboglione.commands.KranitelCommand;
import it.pboglione.configuration.Config;
import it.pboglione.configuration.Messages;
import lombok.Getter;
import net.william278.uniform.paper.PaperUniform;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

@Getter
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
        if(configuration.isMergeNewMessages()) {
            autorepairVerifyMessages();
        } else {
            criticalVerifyMessages();
        }
        /*
         *  The `verifyMessages` function may disable the plugin
         *  stop everithing if this happened
         */
        if(!isEnabled()) {
            return;
        }
        registerCommands();
        getServer().getPluginManager().registerEvents(
                new OnServerLoadEvent(this), this);
    }

    /**
     *  Load the plugin's configuration in the `configuration`
     *  property from the config.yml in the plugin's data folder.
     */
    public void loadConfiguration() {
        this.configuration = YamlConfigurations.update(
                new File(getDataFolder(), "config.yml").toPath(),
                Config.class,
                YamlConfigurationProperties.newBuilder()
                        .header("Kranitel Configuration")
                        .charset(StandardCharsets.UTF_8)
                        .build()
        );
    }

    /**
     *  Save the plugin's configuration from the `configuration`
     *  property to the config.yml in the plugin's data folder.
     */
    public void saveConfiguration() {
        YamlConfigurations.save(
                new File(this.getDataFolder(), "config.yml").toPath(),
                Config.class,
                configuration,
                YamlConfigurationProperties.newBuilder()
                        .header("Kranitel Configuration")
                        .build());
    }

    /**
     *  Load messages from the messages.yml file in the plugin's folder.
     *  messages.yml will be created if not exist.
     */
    public void loadMessages() {
        final File messagesFile = new File(getDataFolder(), "messages.yml");
        try(InputStream stream = getResource("messages.yml")) {
            if(!messagesFile.exists()) {
                assert stream != null;
                this.messages = YamlConfigurations.read(
                        stream,
                        Messages.class
                );
                stream.close();
                saveMessages();
                return;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error", e);
        }
        this.messages = YamlConfigurations.update(
                messagesFile.toPath(),
                Messages.class,
                YamlConfigurationProperties.newBuilder()
                        .header("Kranitel Messages")
                        .charset(StandardCharsets.UTF_8)
                        .build()
        );
    }

    /**
     *  Check if the `messages` HashMap in the messages.yml file in the plugin's folder
     *  has all the keys that has the `messages` HashMap in the messages.yml in the
     *  plugin jar.
     */
    public void criticalVerifyMessages() {
        try(InputStream stream = getResource("messages.yml")) {
            assert stream != null;
            final Messages defaultMessages = YamlConfigurations.read(
                    stream,
                    Messages.class
            );
            stream.close();
            boolean isValid = this.messages.getMessagesList()
                    .containsAll(defaultMessages.getMessagesList());

            if(!isValid) {
                YamlConfigurations.save(
                        new File(this.getDataFolder(), "messages-original.yml").toPath(),
                        Messages.class,
                        defaultMessages,
                        YamlConfigurationProperties.newBuilder()
                                .header("Kranitel Messages")
                                .build());
                getLogger().severe("""
                    Invalid messages.yml! The default messages file has been
                    copied in the plugin's folder. Check that all messages
                    are present in the custom one. Disabling Kranitel...
                    """);
                getServer().getPluginManager().disablePlugin(this);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error", e);
        }
    }

    /**
     *  Check if the `messages` HashMap in the messages.yml file in the plugin's folder
     *  has all the keys that has the `messages` HashMap in the messages.yml in the
     *  plugin jar.
     */
    public void autorepairVerifyMessages() {
        try(InputStream stream = getResource("messages.yml")) {
            assert stream != null;
            final Messages defaultMessages = YamlConfigurations.read(
                    stream,
                    Messages.class
            );
            stream.close();
            boolean isValid = this.messages.getMessagesList()
                    .containsAll(defaultMessages.getMessagesList());
            if(!isValid) {
                this.messages.mergeMessages(defaultMessages);
                saveMessages();
                getLogger().warning("""
                    Invalid messages.yml! The default messages has been merged with
                    yours. Check if everything is correct.
                    """);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error", e);
        }
    }

    /**
     *  Save messages from the `messages` property
     *  to the messages.yml file in the plugin's data folder.
     */
    public void saveMessages() {
        YamlConfigurations.save(
                new File(this.getDataFolder(), "messages.yml").toPath(),
                Messages.class,
                this.messages,
                YamlConfigurationProperties.newBuilder()
                        .header("Kranitel Messages")
                        .build());
    }

    /**
     *  Apply the rules written in the config.yml file.
     *  You must restart the server to reload the rules.
     */
    public void applyRules() {
        try {
            configuration.getPermissions()
                    .forEach((name, rule) ->
                        new Permission(name, rule.description(), rule.isDefault())
                    );
            final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            if (commandMap == null) {
                getLogger().warning("Cannot obtain command map");
                return;
            }
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            getLogger().warning("Unknow error:\n" + e.getLocalizedMessage());
        }
    }

    /**
     *  Register all plugin's commands.
     */
    public void registerCommands() {
        PaperUniform.getInstance(this)
                .register(new KranitelCommand());
    }
}