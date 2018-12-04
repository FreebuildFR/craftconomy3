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
package com.greatmancode.craftconomy3.storage;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.storage.sql.H2Engine;
import com.greatmancode.craftconomy3.storage.sql.MySQLEngine;
import com.greatmancode.craftconomy3.storage.sql.SQLiteEngine;

/**
 * Created by greatman on 2014-07-13.
 */
public class StorageHandler {

    private final StorageEngine engine;

    public StorageHandler() {
        switch (Common.getInstance().getMainConfig().getString("System.Database.Type")) {
            case "h2":
                engine = new H2Engine();
                break;
            case "mysql":
                engine = new MySQLEngine();
                break;
            case "sqlite":
                engine = new SQLiteEngine();
                Common.getInstance().getLogger().severe("SQLite is now deprecated! Only supported method is retrieving the configuration for the format converter. It should be automaticly changed to h2 after the converter.");
                break;
            default:
                engine = null;
                Common.getInstance().getLogger().severe("Storage engine not supported!");
                break;
        }
    }

    /**
     * Retrieve the storage engine currently loaded.
     * @return The storage engine.
     */
    public StorageEngine getStorageEngine() {
        return engine;
    }

    /**
     * Disable the storage engine.
     */
    public void disable() {
        engine.disable();
    }


}
