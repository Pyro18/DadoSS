package me.dadoss.commands;

import me.dadoss.Main;
import me.dadoss.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandFinish implements CommandExecutor{

    private Main main = Main.getInstance();

    private CommandFuctions commandFuctions = new CommandFuctions();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandFuctions.isPlayer(commandSender)) || !(commandFuctions.hasPermission(commandSender.getName(), Permissions.FINISH))) {
            return false;
        }

        if (!(strings.length > 0)) {
            commandSender.sendMessage(main.format(commandFuctions.getString("usage-finish")));
            return false;
        }


        if (!commandFuctions.foundPlayer(commandSender, strings[0])) {
            return false;
        }

        if (!commandFuctions.isSet("end")) {
//            return false;
        }


        if (!main.getInCheck().containsKey(strings[0])) {
            commandSender.sendMessage(main.format(commandFuctions.getString("is-not-in-check").replaceAll("%player%", strings[0])));
            return false;
        }

        commandFuctions.finishControl(Bukkit.getPlayerExact(strings[0]), (Player) commandSender);
        return true;
    }
}
