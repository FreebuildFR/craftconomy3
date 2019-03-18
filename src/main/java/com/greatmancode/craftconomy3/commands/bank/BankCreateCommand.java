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
package com.greatmancode.craftconomy3.commands.bank;

import com.greatmancode.craftconomy3.Cause;
import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.tools.commands.CommandSender;

public class BankCreateCommand extends AbstractCommand {
    public BankCreateCommand(String name) {
        super(name);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Common.getInstance().getAccountManager().exist(args[0], true)) {
            if (Common.getInstance().getAccountManager().getAccount(sender.getName(), false).hasEnough(Common.getInstance()
                    .getBankPrice(), Common.getInstance().getServerCaller().getPlayerCaller().getPlayerWorld(sender.getUuid()), Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName())) {
                Common.getInstance().getAccountManager().getAccount(sender.getName(), false).withdraw(Common.getInstance()
                        .getBankPrice(), Common.getInstance().getServerCaller().getPlayerCaller().getPlayerWorld(sender.getUuid()), Common.getInstance().getCurrencyManager().getDefaultBankCurrency().getName(), Cause.BANK_CREATION, null);
                Account account = Common.getInstance().getAccountManager().getAccount(args[0], true);
                account.getAccountACL().set(sender.getName(), true, true, true, true, true);
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("bank_account_created"));
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().parse("bank_account_not_enough_money_create", Common.getInstance().format(null, Common.getInstance().getCurrencyManager().getDefaultBankCurrency(), Common.getInstance().getBankPrice())));
            }
        } else {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("account_already_exists"));
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("bank_create_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 1;
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public boolean playerOnly() {
        return true;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.bank.create";
    }
}
