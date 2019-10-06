/*
 * This file is part of Craftconomy3.
 *
 * Copyright (c) 2011-2016, Greatman <http://github.com/greatman/>
 * Copyright (c) 2016-2017, Aztorius <http://github.com/Aztorius/>
 * Copyright (c) 2018-2019, Pavog <http://github.com/pavog/>
 *
 * Craftconomy3 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftconomy3 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftconomy3. If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.craftconomy3.commands.setup;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.DisplayFormat;
import com.greatmancode.craftconomy3.NewSetupWizard;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.utils.Tools;

public class NewSetupBasicCommand extends AbstractCommand {
    public NewSetupBasicCommand(String name) {
        super(name);
    }

    private enum INTERNALSTEP {
        DEFAULT_MONEY,
        BANK_PRICE,
        FORMAT,
        START
    }

    private INTERNALSTEP step = INTERNALSTEP.START;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (NewSetupWizard.getState().equals(NewSetupWizard.BASIC_STEP)) {
            if (step.equals(INTERNALSTEP.START)) {
                start(sender);
            } else if (step.equals(INTERNALSTEP.DEFAULT_MONEY)) {
                defaultMoney(sender, args);
            } else if (step.equals(INTERNALSTEP.BANK_PRICE)) {
                bankMoney(sender, args);
            } else if (step.equals(INTERNALSTEP.FORMAT)) {
                format(sender, args);
            }
        }
    }

    @Override
    public String help() {
        return "/ccsetup basic - Basic command of the setup wizard.";
    }

    @Override
    public int maxArgs() {
        return 1;
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

    private void format(CommandSender sender, String[] args) {
        if (args.length == 1) {
            try {
                DisplayFormat format = DisplayFormat.valueOf(args[0].toUpperCase());
                Common.getInstance().getStorageHandler().getStorageEngine().setConfigEntry("longmode", format.toString());
                NewSetupWizard.setState(NewSetupWizard.CONVERT_STEP);
                Common.getInstance().loadDefaultSettings();
                Common.getInstance().startUp();
                sendMessage(sender, "{{DARK_GREEN}}Only 1 step left! Do you want to convert from another system? Type {{WHITE}} /ccsetup convert yes {{DARK_GREEN}}or {{WHITE}}/ccsetup convert no");
            } catch (IllegalArgumentException e) {
                sendMessage(sender, "{{DARK_RED}}This display format doesn't exist! Please type {{WHITE}}/ccsetup basic <format>");
            }
        }
    }

    private void bankMoney(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (Tools.isValidDouble(args[0])) {
                Common.getInstance().getStorageHandler().getStorageEngine().setConfigEntry("bankprice", args[0]);
                step = INTERNALSTEP.FORMAT;
                sendMessage(sender, "{{DARK_GREEN}}Now, let's select the display format you want the balance to be shown. Craftconomy have {{WHITE}}4 {{DARK_GREEN}} display formats");
                sendMessage(sender, "{{WHITE}}Long{{DARK_GREEN}}: {{WHITE}}40 Dollars 1 Coin");
                sendMessage(sender, "{{WHITE}}Small{{DARK_GREEN}}: {{WHITE}} 40.1 Dollars");
                sendMessage(sender, "{{WHITE}}Sign{{DARK_GREEN}}: {{WHITE}} $40.1");
                sendMessage(sender, "{{WHITE}}Signfront{{DARK_GREEN}}: {{WHITE}} 40.1$");
                sendMessage(sender, "{{WHITE}}MajorOnly{{DARK_GREEN}}: {{WHITE}}40 Dollars");
                sendMessage(sender, "{{DARK_GREEN}}Please type {{WHITE}}/ccsetup basic <format>");
            } else {
                sendMessage(sender, "{{DARK_RED}}Invalid amount! Please type {{WHITE}}/ccsetup basic <amount>");
            }
        } else {
            sendMessage(sender, "{{DARK_RED}}You need to enter a amount of money! Please type {{WHITE}}/ccsetup basic <amount>");
        }
    }

    private void defaultMoney(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (Tools.isValidDouble(args[0])) {
                Common.getInstance().getStorageHandler().getStorageEngine().setConfigEntry("holdings", args[0]);
                step = INTERNALSTEP.BANK_PRICE;
                sendMessage(sender, "{{DARK_GREEN}}How much do you want your players to pay for a {{WHITE}}bank account{{DARK_GREEN}}? Please type {{WHITE}}/ccsetup basic <amount>");
            } else {
                sendMessage(sender, "{{DARK_RED}}Invalid amount! Please type {{WHITE}}/ccsetup basic <amount>");
            }
        } else {
            sendMessage(sender, "{{DARK_RED}}You need to enter a amount of money! Please type {{WHITE}}/ccsetup basic <amount>");
        }
    }

    private void start(CommandSender sender) {
        sendMessage(sender, "{{DARK_GREEN}}Basic setup. In this step, you will configure the basic settings of Craftconomy.");
        sendMessage(sender, "{{DARK_GREEN}}How much money you want your players to have initially? Please type {{WHITE}}/ccsetup basic <amount>");
        step = INTERNALSTEP.DEFAULT_MONEY;
    }
}
