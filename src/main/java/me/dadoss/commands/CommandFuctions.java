package me.dadoss.commands;

import me.dadoss.Main;
import me.dadoss.Permissions;
import me.dadoss.filemanager.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandFuctions {

    private Main main = Main.getInstance();


    public CommandFuctions() {
    }


    boolean hasPermission(String namePlayer, Permissions permissions) {
        if (Bukkit.getPlayerExact(namePlayer).hasPermission(permissions.toString())) {
            return true;
        } else {
            Bukkit.getPlayerExact(namePlayer).sendMessage(Permissions.PERMISSIONS_DENIED.toString());
            return false;
        }
    }


    boolean isSet(String zone) {
        if (new FileManager("location", "locations").getConfig().getString(zone.toUpperCase()+".world") != null) {
            return true;
        } else {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+zone.toUpperCase()+" location isn't set. (Please contact a admin /ch)");
            return false;
        }
    }


    String getString(String path) {
        return main.getConfig().getString(path);
    }


    boolean isPlayer(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_PURPLE + "Only players can execute this command");
            return false;
        }
    }


    boolean foundPlayer(CommandSender commandSender, String namePlayer) {
        if (Bukkit.getPlayerExact(namePlayer) != null) {
            return true;
        } else {
            commandSender.sendMessage(main.format(getString("player-not-found").replace("%player%", namePlayer)));
            return false;
        }
    }


    boolean checkYourself(CommandSender commandSender, String namePlayer) {
        if (!main.getConfig().getBoolean("check-yourself.enabled")) {
            if (commandSender.getName().equals(namePlayer)) {
                commandSender.sendMessage(main.format(getString("check-yourself.message")));
                return false;
            }
        }
        return true;
    }


    boolean alreadyInCheck(CommandSender commandSender, String targetName) {
        if (main.getInCheck().containsKey(targetName)) {
            commandSender.sendMessage(main.format(getString("is-already-in-check").replace("%player%", targetName)));
            return true;
        }
        return false;
    }


    public Location getZone(String nameZone) {
        String nameZoneUpper = nameZone.toUpperCase();
        FileConfiguration fileConfiguration = new FileManager("location", "locations").getConfig();
        return new Location(Bukkit.getWorld(fileConfiguration.getString(nameZoneUpper+".world")), fileConfiguration.getDouble(nameZoneUpper+".x"),
                fileConfiguration.getDouble(nameZoneUpper+".y"), fileConfiguration.getDouble(nameZoneUpper+".z"),
                (float) fileConfiguration.getDouble(nameZoneUpper+".yaw"), (float) fileConfiguration.getDouble(nameZoneUpper+".pitch"));
    }


    public void finishControl(Player target, CommandSender commandSender) {
        if (main.getConfig().getBoolean("old-finish") || !(commandSender instanceof Player)) {

            if (main.rightVersion()) {
                main.getTitle().sendTitle(target, "a", 1, 1, 1);
            }

            try {
                target.teleport(getZone("end"));
            } catch (NullPointerException e) {
                target.teleport(target.getWorld().getSpawnLocation());
            }


            target.setAllowFlight(false);


            target.sendMessage(main.format(main.getConfig().getString("finish-cheater-message")));
            commandSender.sendMessage(main.format(main.getConfig().getString("finish-checker-message").replaceAll("%player%", target.getName())));
        } else {

            if (main.rightVersion()) {

                try {
                    main.getTitle().sendTitle(target, "a", 1, 1, 1);
                } catch (NullPointerException ignored) {}
            }


            if (target.isOnline())
                main.getFinishGUI().openGui((Player) commandSender);
        }
        target.closeInventory();
    }

}