/*
 *
 *  Config.java
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

package it.pboglione.configuration;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import it.pboglione.configuration.records.CommandRule;
import it.pboglione.configuration.records.PermissionRule;
import lombok.Getter;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;

@Getter
@Configuration
@SuppressWarnings("FieldMayBeFinal")
public final class Config {
    @Comment("Kranitel warns all the operators when the server is reloaded.")
    private boolean sendReloadAlert = true;

    @Comment({
            "By default if Kranitel detect an invalid messages.yml file and merge it",
            "with the original one."
    })
    private boolean mergeNewMessages = true;

    @Comment("Customizie commands permissions.")
    private Map<String, CommandRule> commmands = Map.of(
            "about", new CommandRule("bukkit","kranitel.op"),
            "version", new CommandRule("bukkit","kranitel.op"),
            "plugins", new CommandRule("bukkit","kranitel.op"),
            "help", new CommandRule("bukkit","kranitel.op"),
            "callback", new CommandRule("paper","kranitel.op")
    );

    @Comment("You should decare here the permissions that you use with commands")
    private Map<String, PermissionRule> permissions = Map.of(
            "kranitel.op",
            new PermissionRule(
                    PermissionDefault.OP,
                    "Allows the player to use some administrative commands."
            ),
            "kranitel.op.super",
            new PermissionRule(
                    PermissionDefault.OP,
                    "Allows the player to ban, pardon and kick other players"
            )
    );
}
