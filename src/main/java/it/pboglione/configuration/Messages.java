/*
 *
 *  Messages.java
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

package it.pboglione.configuration;

import de.exlll.configlib.Configuration;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.HashMap;
import java.util.Set;

@Getter
@Configuration
public final class Messages {
    private HashMap<String, String> messages;

    /**
     *  Get a message as a {@link net.kyori.adventure.text.Component} from the messages.yml file.
     */
    public Component getMessage(String key, TagResolver... resolvers) {
        return MiniMessage.miniMessage()
                .deserialize(messages.getOrDefault(key, "Message not found"), resolvers);
    }

    /**
     *  Get a {@link java.util.Set<String>} of all the messages keys.
     */
    public Set<String> getMessagesList() {
        return messages.keySet();
    }

    /**
     *  Merge two messages files
     */
    public void mergeMessages(Messages messages) {
        final Messages tmpMessages = messages;
        this.messages.forEach((key, message) ->
            tmpMessages.getMessages().put(key, message));
        this.messages = tmpMessages.getMessages();
    }
}
