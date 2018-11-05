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
package com.greatmancode.craftconomy3.commands.money;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.account.Balance;
import com.greatmancode.tools.commands.CommandSender;
import com.greatmancode.tools.commands.interfaces.CommandExecutor;

public class BalanceCommand extends CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Common.getInstance().getCommandManager().getCommand("money").execute("", sender, args);
            return;
        }
        if (Common.getInstance().getAccountManager().exist(args[0], false)) {
            Account account = Common.getInstance().getAccountManager().getAccount(args[0], false);
            for (Balance bl : account.getAllBalance()) {
                Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender.getUuid(), Common.getInstance().getLanguageManager().getString("money_all_title") + " " + args[0] + ": §6" + Common.getInstance().format(bl.getWorld(), bl.getCurrency(), bl.getBalance()));
            }
        } else {
            Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender.getUuid(), Common.getInstance().getLanguageManager().getString("account_not_exist"));
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("money_balance_cmd_help");
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
        return "craftconomy.money.balance.others";
    }
}
