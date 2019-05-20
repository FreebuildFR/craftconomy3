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
package com.greatmancode.craftconomy3.commands.money;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.tools.commands.CommandSender;

import java.sql.Timestamp;

class LogCommandThread implements Runnable {

    class LogCommandThreadEnd implements Runnable {
        private final CommandSender sender;
        private final String ret;

        public LogCommandThreadEnd(CommandSender sender, String ret) {
            this.sender = sender;
            this.ret = ret;
        }

        @Override
        public void run() {
            if (sender.getUuid() != null)
                Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender.getUuid(), ret);
            else
                Common.getInstance().getServerCaller().getPlayerCaller().sendMessage(sender.getName(), ret);
        }
    }

    private final CommandSender sender;
    private final int page;
    private final Account user;

    public LogCommandThread(CommandSender sender, int page, Account user) {
        this.sender = sender;
        this.page = page;
        this.user = user;
    }

    @Override
    public void run() {
        String ret = Common.getInstance().getLanguageManager().parse("money_log_header", page, user.getAccountName()) + "\n";
        for (LogCommand.LogEntry entry : Common.getInstance().getStorageHandler().getStorageEngine().getLog(user, page)) {
            ret += "{{DARK_GREEN}}Time: {{WHITE}}" + entry.timestamp + " {{DARK_GREEN}}Type: {{WHITE}}" + entry.type + " {{DARK_GREEN}} Amount: {{WHITE}}" + Common.getInstance().format(entry.worldName, entry.currency, entry.amount) + " {{DARK_GREEN}}Cause: {{WHITE}}" + entry.cause;
            if (entry.causeReason != null) {
                ret += " {{DARK_GREEN}}Reason: {{WHITE}}" + entry.causeReason;
            }
            ret += "\n";
        }
        Common.getInstance().getServerCaller().getSchedulerCaller().delay(new LogCommandThreadEnd(sender, ret), 0, true);
    }
}

public class LogCommand extends AbstractCommand {
    public LogCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int page = 1;
        if (args.length >= 1) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("invalid_page"));
                return;
            }
        }
        Account user = Common.getInstance().getAccountManager().getAccount(sender.getName(), false);
        if (args.length == 2 && Common.getInstance().getServerCaller().getPlayerCaller().checkPermission(sender.getUuid(), "craftconomy.money.log.others")) {
            if (Common.getInstance().getAccountManager().exist(args[1], false)) {
                user = Common.getInstance().getAccountManager().getAccount(args[1], false);
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("account_null"));
                return;
            }
        }

        Common.getInstance().getServerCaller().getSchedulerCaller().delay(new LogCommandThread(sender, page, user),
                0, false);
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("money_log_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 2;
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
        return "craftconomy.money.log";
    }

    public static class LogEntry {
        public Timestamp timestamp;
        public String type, worldName, cause, causeReason;
        public Currency currency;
        public double amount;

        public LogEntry(Timestamp timestamp, String type, String worldName, String cause, String causeReason, Currency currency, double amount) {
            this.timestamp = timestamp;
            this.type = type;
            this.worldName = worldName;
            this.cause = cause;
            this.causeReason = causeReason;
            this.currency = currency;
            this.amount = amount;
        }

    }
}
