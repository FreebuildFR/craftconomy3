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
package com.greatmancode.craftconomy3.commands.bank;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;

public class BankPermCommand extends AbstractCommand {
    public BankPermCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Common.getInstance().getAccountManager().exist(args[0], true)) {
            Account account = Common.getInstance().getAccountManager().getAccount(args[0], true);
            if (account.getAccountACL().canAcl(sender.getName()) || account.getAccountACL().isOwner(sender.getName()) ||
                    Common
                            .getInstance().getServerCaller().getPlayerCaller().checkPermission(sender.getUuid(), "craftconomy.bank.perm.others")) {

                if ("deposit".equalsIgnoreCase(args[1])) {
                    account.getAccountACL().setDeposit(args[2], Boolean.parseBoolean(args[3]));
                } else if ("withdraw".equalsIgnoreCase(args[1])) {
                    account.getAccountACL().setWithdraw(args[2], Boolean.parseBoolean(args[3]));
                } else if ("acl".equalsIgnoreCase(args[1])) {
                    account.getAccountACL().setAcl(args[2], Boolean.parseBoolean(args[3]));
                } else if ("show".equalsIgnoreCase(args[1])) {
                    account.getAccountACL().setShow(args[2], Boolean.parseBoolean(args[3]));
                } else {
                    sendMessage(sender, Common.getInstance().getLanguageManager().getString("invalid_flag"));
                    return;
                }
                sendMessage(sender, Common.getInstance().getLanguageManager().parse("bank_flag_set", args[1], args[2], args[3]));
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("cant_modify_acl"));
            }
        } else {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("account_not_exist!"));
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("bank_perm_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 4;
    }

    @Override
    public int minArgs() {
        return 4;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.bank.perm";
    }
}
