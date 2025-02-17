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

package it.ilgrandeanonimo.kranitel.configuration;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import it.ilgrandeanonimo.kranitel.configuration.records.CommandRule;
import it.ilgrandeanonimo.kranitel.configuration.records.PermissionRule;
import lombok.Getter;
import org.bukkit.permissions.PermissionDefault;

import java.util.Map;

@Getter
@Configuration
@SuppressWarnings("FieldMayBeFinal")
public final class Settings {
    @Comment("Customizie commands permissions.")
    private Map<String, CommandRule> commmands = Map.of(
            "about", new CommandRule("bukkit","kranitel.admin"),
            "version", new CommandRule("bukkit","kranitel.admin"),
            "plugins", new CommandRule("bukkit","kranitel.admin"),
            "help", new CommandRule("bukkit","kranitel.admin"),
            "callback", new CommandRule("paper","kranitel.admin")
    );

    @Comment("You should decare here the permissions that you use with commands")
    private Map<String, PermissionRule> permissions = Map.of(
            "kranitel.admin",
            new PermissionRule(
                    PermissionDefault.OP,
                    "Allows the player to use some administrative commands."
            ),
            "kranitel.op",
            new PermissionRule(
                    PermissionDefault.OP,
                    "Allows the player to ban, pardon and kick other players"
            )
    );

    public static final String HEADER = """
            Kranitel Configuration
            """;
}
