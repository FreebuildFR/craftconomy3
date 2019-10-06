/**
 * This file is part of Craftconomy3.
 * <p>
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018, Pavog <http://github.com/pavog/>
 * <p>
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.setup;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.NewSetupWizard;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.utils.Tools;

import java.util.HashMap;
import java.util.Map;

public class NewSetupDatabaseCommand extends AbstractCommand {

    private static final Map<String, String> VALUES = new HashMap<>();
    private static final String ERROR_MESSAGE = "{{DARK_RED}}A error occured. The error is: {{WHITE}}%s";
    private static final String CONFIG_NODE = "System.Database.Type";
    private INTERNALSTEP step = INTERNALSTEP.START;

    public NewSetupDatabaseCommand(String name) {
        super(name);
    }

    private enum INTERNALSTEP {
        START,
        SQLITE,
        MYSQL,
        H2
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (NewSetupWizard.getState().equals(NewSetupWizard.DATABASE_STEP)) {
            if (step.equals(INTERNALSTEP.START)) {
                start(sender, args);
            } else if (step.equals(INTERNALSTEP.MYSQL)) {
                mysql(sender, args);
            }
        }
    }

    @Override
    public String help() {
        return "/ccsetup database - Database step for setup wizard.";
    }

    @Override
    public int maxArgs() {
        return 3;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.setup";
    }

    private void start(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if ("mysql".equalsIgnoreCase(args[0])) {
                step = INTERNALSTEP.MYSQL;
                Common.getInstance().getMainConfig().setValue(CONFIG_NODE, "mysql");
                sendMessage(sender, "{{DARK_GREEN}}You selected {{WHITE}}MySQL{{DARK_GREEN}}. Please type {{WHITE}}/ccsetup database address <Your host>");
            } else if ("h2".equalsIgnoreCase(args[0])) {
                step = INTERNALSTEP.H2;
                h2(sender);
            } else {
                sendMessage(sender, "{{DARK_RED}}Invalid value!");
                sendMessage(sender, "{{DARK_GREEN}}Please type {{WHITE}}/ccsetup database <mysql/h2>");
            }
        }
    }

    private void h2(CommandSender sender) {
        Common.getInstance().getMainConfig().setValue(CONFIG_NODE, "h2");
        Common.getInstance().initialiseDatabase();
        done(sender);
    }

    private void mysql(CommandSender sender, String[] args) {
        if (args.length == 2) {
            if ("address".equalsIgnoreCase(args[0])) {
                VALUES.put("address", args[1]);
                Common.getInstance().getMainConfig().setValue("System.Database.Address", args[1]);
                sendMessage(sender, "{{DARK_GREEN}}Alright! Please type {{WHITE}}/ccsetup database port <Your port> {{DARK_GREEN}}to set your MySQL port (Usually 3306)");
            } else if ("port".equalsIgnoreCase(args[0])) {
                if (Tools.isInteger(args[1])) {
                    int port = Integer.parseInt(args[1]);
                    VALUES.put("port", args[1]);
                    Common.getInstance().getMainConfig().setValue("System.Database.Port", port);
                    sendMessage(sender, "{{DARK_GREEN}}Saved! Please type {{WHITE}}/ccsetup database username <Username> {{DARK_GREEN}}to set your MySQL username");
                } else {
                    sendMessage(sender, "{{DARK_RED}}Invalid port!");
                }
            } else if ("username".equalsIgnoreCase(args[0])) {
                VALUES.put("username", args[1]);
                Common.getInstance().getMainConfig().setValue("System.Database.Username", args[1]);
                sendMessage(sender, "{{DARK_GREEN}}Saved! Please type {{WHITE}}/ccsetup database password <Password> {{DARK_GREEN}}to set your MySQL password (enter \"\" for none)");
            } else if ("password".equalsIgnoreCase(args[0])) {
                if (args[1].equals("''") || args[1].equals("\"\"")) {
                    VALUES.put("password", "");
                    Common.getInstance().getMainConfig().setValue("System.Database.Password", "");
                } else {
                    VALUES.put("password", args[1]);
                    Common.getInstance().getMainConfig().setValue("System.Database.Password", args[1]);
                }
                sendMessage(sender, "{{DARK_GREEN}}Saved! Please type {{WHITE}}/ccsetup database db <Database Name> {{DARK_GREEN}}to set your MySQL database.");
            } else if ("db".equalsIgnoreCase(args[0])) {
                VALUES.put("db", args[1]);
                Common.getInstance().getMainConfig().setValue("System.Database.Db", args[1]);
                sendMessage(sender, "{{DARK_GREEN}}Saved! Please type {{WHITE}}/ccsetup database prefix <Prefix> {{DARK_GREEN}}to set your table prefix (If not sure, put cc3_).");
            } else if ("prefix".equalsIgnoreCase(args[0])) {
                VALUES.put("prefix", args[1]);
                Common.getInstance().getMainConfig().setValue("System.Database.Prefix", args[1]);
                sendMessage(sender, "{{DARK_GREEN}}Done! Please wait while the database is initializing.");
            }
        }

        if (VALUES.size() == 6) {
            Common.getInstance().initialiseDatabase();
            done(sender);
            if (!Common.getInstance().getStorageHandler().getStorageEngine().isConnected()) {
                // TODO Send error & try again message
                VALUES.clear();
            }
        }
    }

    private void done(CommandSender sender) {
        Common.getInstance().initializeCurrency();
        sendMessage(sender, "{{DARK_GREEN}}Alright! Welcome to Craftconomy! We use a Multi-Currency system. I need you to write the settings for the default currency.");
        sendMessage(sender, "{{DARK_GREEN}}First, let's configure the {{WHITE}}main currency name {{DARK_GREEN}}(Ex: {{WHITE}}Dollar{{DARK_GREEN}}). Type {{WHITE}}/ccsetup currency name <Name>");
        NewSetupWizard.setState(NewSetupWizard.CURRENCY_STEP);
    }
}
