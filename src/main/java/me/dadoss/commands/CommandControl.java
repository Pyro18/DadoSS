package me.dadoss.commands;

import me.dadoss.Main;
import me.dadoss.Permissions;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandControl implements CommandExecutor {

    private Main main = Main.getInstance();

    private CommandFuctions commandFuctions = new CommandFuctions();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandFuctions.isPlayer(commandSender)))
            return false;


        if (!(commandFuctions.hasPermission(commandSender.getName(), Permissions.CONTROL)))
            return false;

        if (!(strings.length > 0)) {
            commandSender.sendMessage(main.format(commandFuctions.getString("usage-control")));
            return false;
        }

        String targetName = strings[0];

        if (!commandFuctions.checkYourself(commandSender, targetName))
            return false;

        if (!commandFuctions.foundPlayer(commandSender, targetName))
            return false;

        if (commandFuctions.alreadyInCheck(commandSender, targetName))
            return false;

        if (!commandFuctions.isSet("cheater") || !commandFuctions.isSet("checker"))
            return false;

        Player target = Bukkit.getPlayerExact(targetName);

        target.teleport(commandFuctions.getZone("cheater"));
        ((Player) commandSender).teleport(commandFuctions.getZone("checker"));

        if (main.rightVersion()) {
            // Title
            main.getTitle().sendTitle(target, commandFuctions.getString("title.title-message"), 2, Integer.MAX_VALUE, 2);
            main.getTitle().sendSubtitle(target, commandFuctions.getString("title.subtitle-message").replace("%player%", commandSender.getName()), 2, 99999, 2);
        }

        for (int i=0; i<150; i++)
            target.sendMessage("");

        commandSender.sendMessage(main.format(commandFuctions.getString("checker-start-message").replace("%player%", strings[0])));

        if (Bukkit.getBukkitVersion().contains("1.7") || Bukkit.getBukkitVersion().contains("1.8")) {
            target.getWorld().playSound(target.getLocation(), Sound.valueOf("LEVEL_UP"), 10, 3);
            target.getWorld().playSound(target.getLocation(), Sound.valueOf("IRONGOLEM_HIT"), 10, 2);
        } else {
            target.getWorld().playSound(target.getLocation(), Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 10, 3);
            target.getWorld().playSound(target.getLocation(), Sound.valueOf("ENTITY_IRONGOLEM_HURT"), 10, 2);
        }


        String effectName;
        if (Bukkit.getBukkitVersion().contains("1.7")){
            effectName = "MOBSPAWNER_FLAMES";
        } else {
            effectName = "FLAME";
        }
        Location targetLocation = target.getLocation();
        World targetWorld = targetLocation.getWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 360; i += 5) {
//					targetLocation.setY(targetLocation.getY() + Math.cos(i)*5);
                    targetWorld.playEffect(targetLocation, Effect.valueOf(effectName), 51);
                }
            }
        }.runTaskAsynchronously(main);


        main.getInCheck().put(target.getName(), commandSender.getName());

        main.getControlGUI().openGui(target);

        return true;
    }


}