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

package it.pboglione.commands;

import it.pboglione.Kranitel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.william278.uniform.CommandUser;
import net.william278.uniform.Permission;
import net.william278.uniform.annotations.CommandNode;
import net.william278.uniform.annotations.PermissionNode;
import net.william278.uniform.annotations.Syntax;

@CommandNode(
        value = "kranitel",
        description = "The main and unique kranitel's command.",
        permission = @PermissionNode(
                value = "kranitel.operator",
                defaultValue = Permission.Default.IF_OP
        )
)
@RequiredArgsConstructor
@SuppressWarnings("unused")
public final class KranitelCommand {
        @Syntax
        public void execute(CommandUser user) {
                user.getAudience().sendMessage(MiniMessage.miniMessage()
                        .deserialize("""
                                <#1D6AFF><bold>Kranitel</bold>
                                    <white>Version:</white> <green><version></green>
                                    <white>Author:</white> <green>IlGrandeAnonimo</green>
                                """,
                                Placeholder.parsed("version", Kranitel.getInstance().getDescription()
                                        .getVersion())
                        )
                );
        }

        @CommandNode(value = "reload")
        public static final class Info {
                @Syntax
                public void execute(CommandUser user) {
                        Kranitel plugin = Kranitel.getInstance();
                        plugin.loadConfiguration();
                        plugin.loadMessages();
                        user.getAudience().sendMessage(
                                plugin.getMessages().getMessage("reload-done"));
                }
        }
}
