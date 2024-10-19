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
        }
        plugin.applyRules();
    }
}
