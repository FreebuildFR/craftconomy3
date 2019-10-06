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
package com.greatmancode.craftconomy3.tools.caller.sponge;

import com.greatmancode.craftconomy3.tools.commands.CommandSender;
import com.greatmancode.craftconomy3.tools.commands.ConsoleCommandSender;
import com.greatmancode.craftconomy3.tools.commands.PlayerCommandSender;
import com.greatmancode.craftconomy3.tools.commands.SubCommand;
import com.greatmancode.craftconomy3.tools.entities.Player;
import com.greatmancode.craftconomy3.tools.events.Event;
import com.greatmancode.craftconomy3.tools.interfaces.Common;
import com.greatmancode.craftconomy3.tools.interfaces.SpongeLoader;
import com.greatmancode.craftconomy3.tools.interfaces.caller.ServerCaller;
import com.greatmancode.craftconomy3.tools.utils.ServicePriority;
import com.greatmancode.craftconomy3.tools.utils.VaultEconomy;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpongeServerCaller extends ServerCaller {
    private String name, version;

    public SpongeServerCaller(SpongeLoader spongeLoader, String name, String version) {
        super(spongeLoader);
        addPlayerCaller(new SpongePlayerCaller(this));
        addSchedulerCaller(new SpongeSchedulerCaller(this));
        this.name = name;
        this.version = version;
    }

    @Override
    public void disablePlugin() {
        //TODO Impossible?
    }

    @Override
    public String addColor(String message) {
        return message;
    }

    public Text addColorSponge(String message) {
        message = getCommandPrefix() + message;
        Text.Builder textMain = Text.builder();
        Matcher m = Pattern.compile("(\\{\\{([^\\{\\}]+)\\}\\}|[^\\{\\}]+)").matcher(message);
        TextColor color = null;
        TextStyle.Base style = null;
        while (m.find()) {
            
            String entry = m.group();
            if (entry.contains("{{")) {
                color = null;
                style = null;
                switch (entry) {
                    case "{{BLACK}}":
                        color = TextColors.BLACK;
                        break;
                    case "{{DARK_BLUE}}":
                        color = TextColors.DARK_BLUE;
                        break;
                    case "{{DARK_GREEN}}":
                        color = TextColors.DARK_GREEN;
                        break;
                    case "{{DARK_CYAN}}":
                        color = TextColors.DARK_AQUA;
                        break;
                    case "{{DARK_RED}}":
                        color = TextColors.DARK_RED;
                        break;
                    case "{{PURPLE}}":
                        color = TextColors.DARK_PURPLE;
                        break;
                    case "{{GOLD}}":
                        color = TextColors.GOLD;
                        break;
                    case "{{GRAY}}":
                        color = TextColors.GRAY;
                        break;
                    case "{{DARK_GRAY}}":
                        color = TextColors.DARK_GRAY;
                        break;
                    case "{{BLUE}}":
                        color = TextColors.AQUA; //TODO Wrong color
                        break;
                    case "{{BRIGHT_GREEN}}":
                        color = TextColors.GREEN;
                        break;
                    case "{{CYAN}}":
                        color = TextColors.AQUA;
                        break;
                    case "{{RED}}":
                        color = TextColors.RED;
                        break;
                    case "{{LIGHT_PURPLE}}":
                        color = TextColors.LIGHT_PURPLE;
                        break;
                    case "{{YELLOW}}":
                        color = TextColors.YELLOW;
                        break;
                    case "{{WHITE}}":
                        color = TextColors.WHITE;
                        break;
                    case "{{OBFUSCATED}}":
                        style = TextStyles.OBFUSCATED;
                        break;
                    case "{{BOLD}}":
                        style = TextStyles.BOLD;
                        break;
                    case "{{STRIKETHROUGH}}":
                        style = TextStyles.STRIKETHROUGH;
                        break;
                    case "{{UNDERLINE}}":
                        style = TextStyles.UNDERLINE;
                        break;
                    case "{{ITALIC}}":
                        style = TextStyles.ITALIC;
                        break;
                    case "{{RESET}}":
                        style = TextStyles.RESET;
                        break;
                }
            } else {
                Text.Builder text = Text.builder(entry);

                if (color != null) {
                    text.color(color);
                }
                if (style != null) {
                    text.style(style);
                }
                textMain.append(text.build());
            }
        }
        return textMain.build();
    }

    @Override
    public boolean worldExist(String worldName) {
        return ((SpongeLoader) loader).getGame().getServer().getWorld(worldName).isPresent();
    }

    @Override
    public String getDefaultWorld() {
        return ((SpongeLoader)loader).getGame().getServer().getWorlds().iterator().next().getName();
    }

    @Override
    public File getDataFolder() {
        File data = new File("mods" + File.separator + "Craftconomy3");
        data.mkdirs();
        return data;
    }

    @Override
    public void addCommand(String name, String help, final SubCommand subCommand) {
        CommandCallable command = new CommandCallable() {
            @Override
            public CommandResult process(CommandSource source, String arguments) throws CommandException {
                String subCommandValue = "";
                String[] newArgs;
                String[] args = arguments.split(" ");
                if (args.length <= 1) {
                    newArgs = new String[0];
                    if (args.length != 0) {
                        subCommandValue = args[0];
                    }
                } else {
                    newArgs = new String[args.length - 1];
                    subCommandValue = args[0];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                }
                CommandSender sender = null;
                if (source instanceof ConsoleSource) {
                    ConsoleSource consoleSource = (ConsoleSource) source;
                    sender = new ConsoleCommandSender<>(source.getName(), consoleSource);
                } else if (source instanceof Player) {
                    Player playerSource = (Player) source;
                    sender = new PlayerCommandSender<>(playerSource.getDisplayName(), playerSource.getUuid(), playerSource);
                }
                if(sender != null) {
                    subCommand.execute(subCommandValue, sender, newArgs);
                }else{
                    return CommandResult.empty();
                }
                return CommandResult.success();
            }
    
            @Override
            public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
                List<String> list = new ArrayList<>();
                list.addAll(subCommand.getSubCommandKeys());
                return list;
            }
    
            public List<String> getSuggestions(CommandSource source, String arguments) throws CommandException {
                List<String> list = new ArrayList<>();
                list.addAll(subCommand.getSubCommandKeys());
                return list;
            }

            @Override
            public boolean testPermission(CommandSource source) {
                return true;
            }
    
            @Override
            public Optional<Text> getShortDescription(CommandSource source) {
                return Optional.empty();
            }
    
            @Override
            public Optional<Text> getHelp(CommandSource source) {
                return Optional.empty();
            }

            @Override
            public Text getUsage(CommandSource source) {
                return Text.of();
            }
        };
        ((SpongeLoader)loader).getGame().getCommandManager().register(loader, command, name);
    }

    @Override
    public String getServerVersion() {
        return String.format("%s %s", "Sponge", ((SpongeLoader)loader).getGame().getPlatform().getMinecraftVersion().getName());
    }

    @Override
    public String getPluginVersion() {
        return version;
    }

    @Override
    public String getPluginName() {
        return name;
    }

    @Override
    public void loadLibrary(String path) {

    }

    @Override
    public void registerPermission(String permissionNode) {
       //None
    }

    @Override
    public boolean isOnlineMode() {
        return ((SpongeLoader)loader).getGame().getServer().getOnlineMode();
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger(getPluginName());
    }

    @Override
    public void throwEvent(Event event) {
        //Not used
       // Game game = ((SpongeLoader) loader).getGame();
        //game.getEventManager().post(event);
    }

    @Override
    public Common retrievePlugin(String name) {

        Game game = ((SpongeLoader)loader).getGame();
        return (Common) game.getPluginManager().getPlugin(name).get().getInstance().get();
    }

    @Override
    public boolean isPluginEnabled(String name) {
        Game game = ((SpongeLoader)loader).getGame();
        return game.getPluginManager().isLoaded(name);
    }

    @Override
    public void setVaultEconomyHook(VaultEconomy instance, ServicePriority priority) {

    }
}
