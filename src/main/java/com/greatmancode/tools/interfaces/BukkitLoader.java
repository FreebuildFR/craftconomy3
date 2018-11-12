/**
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.tools.interfaces;

import com.greatmancode.tools.ServerType;
import com.greatmancode.tools.caller.bukkit.BukkitServerCaller;
import com.greatmancode.tools.commands.interfaces.CommandReceiver;
import com.greatmancode.tools.configuration.bukkit.BukkitConfig;
import com.greatmancode.tools.events.EventManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.io.InputStreamReader;

public class BukkitLoader extends JavaPlugin implements Loader {
    private Common common;
    private EventManager eventManager;
    @Getter
    @Setter
    private CommandReceiver commandReceiver;

    @Override
    public void onEnable() {
        BukkitServerCaller bukkitCaller = new BukkitServerCaller(this);
        eventManager = new EventManager(bukkitCaller);
        InputStreamReader readerLoader = new InputStreamReader(this.getClass().getResourceAsStream("/loader.yml"));
        BukkitConfig bukkitConfig = new BukkitConfig(readerLoader, bukkitCaller);
        String mainClass = bukkitConfig.getString("main-class");
        try {
            Class<?> clazz = Class.forName(mainClass);
            if (Common.class.isAssignableFrom(clazz)) {
                common = (Common) clazz.newInstance();
                common.onEnable(bukkitCaller, getLogger());
            } else {
                getLogger().severe("The class " + mainClass + " is invalid!");
                this.getServer().getPluginManager().disablePlugin(this);
            }
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.SEVERE, "Unable to load the main class!", e);
            this.getServer().getPluginManager().disablePlugin(this);
        } catch (InstantiationException e) {
            getLogger().log(Level.SEVERE, "Unable to load the main class!", e);
            this.getServer().getPluginManager().disablePlugin(this);
        } catch (IllegalAccessException e) {
            getLogger().log(Level.SEVERE, "Unable to load the main class!", e);
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        common.onDisable();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUKKIT;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public Common getCommon() {
        return common;
    }
}