/*
 *
 *  KranitelCommand.java
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

package it.ilgrandeanonimo.kranitel;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.william278.uniform.CommandUser;
import net.william278.uniform.Permission;
import net.william278.uniform.annotations.CommandNode;
import net.william278.uniform.annotations.PermissionNode;
import net.william278.uniform.annotations.Syntax;
import org.bukkit.command.CommandSender;

@CommandNode(
        value = "kranitel",
        description = "The main and unique kranitel's command.",
        permission = @PermissionNode(
                value = "kranitel.operator",
                defaultValue = Permission.Default.IF_OP
        )
)
@SuppressWarnings("unused")
public final class KranitelCommand {
        private static final Kranitel plugin = Kranitel.getInstance();
        @Syntax
        public void execute(CommandUser user) {
                CommandSender sender = (CommandSender) user.getAudience();
                sender.sendRichMessage("""
                                <#1D6AFF><bold>Kranitel</bold>
                                    <white>Version:</white> <green><version></green>
                                    <white>Author:</white> <green>IlGrandeAnonimo</green>
                                """,
                        Placeholder.parsed("version", plugin
                                .getPluginMeta().getVersion())
                );
        }

        @CommandNode(value = "reload")
        public static final class Reload {
                @Syntax
                public void execute(CommandUser user) {
                        plugin.loadConfiguration();
                        plugin.loadMessages();
                        plugin.reapplyRules();
                        CommandSender sender = (CommandSender) user.getAudience();
                        sender.sendRichMessage(
                                plugin.getMessages().getReloaded()
                        );
                }
        }
}
