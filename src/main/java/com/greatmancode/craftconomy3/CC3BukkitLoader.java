/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2012, Greatman <http://github.com/greatman/>
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
package com.greatmancode.craftconomy3;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.greatmancode.craftconomy3.commands.BukkitCommandManager;
import com.greatmancode.craftconomy3.utils.MetricsBukkit;

/**
 * Class used when the plugin is loaded from Craftbukkit
 * @author greatman
 * 
 */
public class CC3BukkitLoader extends JavaPlugin {

	private static CC3BukkitLoader instance = null;
	private MetricsBukkit metrics;

	public void onEnable() {
		instance = this;

		try {
			metrics = new MetricsBukkit(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Common(true, getLogger()).initialize();
		BukkitCommandManager cmdManager = new BukkitCommandManager();
		this.getCommand("money").setExecutor(cmdManager);
		this.getCommand("bank").setExecutor(cmdManager);
		this.getCommand("ccsetup").setExecutor(cmdManager);
		this.getCommand("currency").setExecutor(cmdManager);
		this.getCommand("craftconomy").setExecutor(cmdManager);
		this.getCommand("payday").setExecutor(cmdManager);
	}

	public void onDisable() {
		Common.getInstance().disable();
	}

	public static CC3BukkitLoader getInstance() {
		return instance;
	}

	public MetricsBukkit getMetrics() {
		return metrics;
	}
}