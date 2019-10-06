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
package com.greatmancode.craftconomy3.commands.group;

import com.greatmancode.craftconomy3.Common;
import com.greatmancode.craftconomy3.commands.AbstractCommand;
import com.greatmancode.craftconomy3.tools.commands.CommandSender;

public class GroupAddWorldCommand extends AbstractCommand {
    public GroupAddWorldCommand(String name) {
        super(name);
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (Common.getInstance().getWorldGroupManager().worldGroupExist(args[0])) {
            if (Common.getInstance().getServerCaller().worldExist(args[1])) {
                if (!"default".equals(Common.getInstance().getWorldGroupManager().getWorldGroupName(args[1]))) {
                    sendMessage(sender, Common.getInstance().getLanguageManager().parse("world_already_in_group", args[1]));
                    return;
                }
                Common.getInstance().getWorldGroupManager().addWorldToGroup(args[0], args[1]);
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("group_world_added"));
            } else {
                sendMessage(sender, Common.getInstance().getLanguageManager().getString("world_not_exist"));
            }
        } else {
            sendMessage(sender, Common.getInstance().getLanguageManager().getString("group_not_exist"));
        }
    }

    @Override
    public String help() {
        return Common.getInstance().getLanguageManager().getString("group_addworld_cmd_help");
    }

    @Override
    public int maxArgs() {
        return 2;
    }

    @Override
    public int minArgs() {
        return 2;
    }

    @Override
    public boolean playerOnly() {
        return false;
    }

    @Override
    public String getPermissionNode() {
        return "craftconomy.group.addworld";
    }
}
