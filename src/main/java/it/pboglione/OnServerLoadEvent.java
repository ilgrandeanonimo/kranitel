/*
 *
 *  OnServerLoadEvent.java
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

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

@RequiredArgsConstructor
public final class OnServerLoadEvent implements Listener {
    final private Kranitel plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(ServerLoadEvent event) {
        if(event.getType().equals(ServerLoadEvent.LoadType.RELOAD)) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if(player.hasPermission("kranitel.operator")) {
                    player.sendMessage(plugin.getMessages()
                            .getMessage("do-not-reload"));
                }
            });
        } else {
            plugin.applyRules();
        }
    }
}
