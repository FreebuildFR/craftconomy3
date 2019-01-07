/**
 * This file is part of GreatmancodeTools.
 *
 * Copyright (c) 2013-2016, Greatman <http://github.com/greatman/>
 *
 * GreatmancodeTools is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GreatmancodeTools is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GreatmancodeTools.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.events.bukkit;

import com.greatmancode.tools.caller.bukkit.BukkitPlayerCaller;
import com.greatmancode.tools.entities.Player;
import com.greatmancode.tools.events.EventManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    private BukkitPlayerCaller caller;

    public PlayerJoinEventListener(BukkitPlayerCaller caller) {
        this.caller = caller;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        org.bukkit.entity.Player player = event.getPlayer();
        com.greatmancode.tools.events.playerevent.PlayerJoinEvent pEvent = new com.greatmancode.tools.events
                .playerevent.PlayerJoinEvent(Player.getPlayer(caller,player.getUniqueId()));
        EventManager.getInstance().callEvent(pEvent);
    }
}
