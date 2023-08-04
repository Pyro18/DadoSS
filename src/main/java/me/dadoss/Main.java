package me.dadoss;

import me.dadoss.title.*;
import me.dadoss.util.ControlGUI;
import me.dadoss.util.FinishGUI;
import me.dadoss.listener.ListenerDadoSS;
import me.dadoss.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    private HashMap<String, String> inCheck = new HashMap<>();

    private static Title title;

    private CommandFuctions commandFuctions = new CommandFuctions();

    private ControlGUI controlGui;

    public ControlGUI getControlGUI() {
        return controlGui;
    }

    private FinishGUI finishGui;

    public FinishGUI getFinishGUI() {
        return finishGui;
    }

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

        instance = this;

        consoleCommandSender.sendMessage(ChatColor.GREEN + "Loading DadoSS...");
        consoleCommandSender.sendMessage(ChatColor.GREEN + "Server version: " + Bukkit.getBukkitVersion());

        setupTitle();

        saveDefaultConfig();

        controlGui = new ControlGUI();
        finishGui = new FinishGUI();


        getServer().getPluginManager().registerEvents(new ListenerDadoSS(), this);

        getCommand("dadoss").setExecutor(new CommandControlDadoSS());
        getCommand("ss").setExecutor(new CommandControl());
        getCommand("unss").setExecutor(new CommandFinish());

        consoleCommandSender.sendMessage(ChatColor.GREEN + "DadoSS Caricato, v" + getDescription().getVersion());
        if (!rightVersion()) {
            consoleCommandSender.sendMessage(ChatColor.RED + "Server in 1.7, titolo disabilitato");
        } else {
            consoleCommandSender.sendMessage(ChatColor.GREEN + "Titolo attivato.");
        }

    }


    @Override
    public void onDisable() {
        for (World worlds : getServer().getWorlds())
            for (Player players : worlds.getPlayers())
                if (getInCheck().containsKey(players.getName())) {
                    if (!getServer().getBukkitVersion().contains("1.7")) {
                        getTitle().sendTitle(players, "a", 1, 1, 1);
                    }


                    players.teleport(commandFuctions.getZone("end"));


                    players.setAllowFlight(false);

                    players.sendMessage(format(getConfig().getString("finish-cheater-message")));
                    Bukkit.getConsoleSender().sendMessage(format(getConfig().getString("finish-checker-message").replaceAll("%player%", players.getName())));
                }
        if (controlGui != null){
            getControlGUI().getCanCloseGui().clear();
        }
    }


    public String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public List<String> colorLore(List<String> lore) {
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(format(line));
        }
        return coloredLore;
    }


    public boolean rightVersion() {
        return !getServer().getBukkitVersion().contains("1.7");
    }


    private boolean setupTitle() {
        if (rightVersion()) {
            String version;
            try {

                version = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            } catch (ArrayIndexOutOfBoundsException exception) {

                return false;
            }

            switch (version) {
                case "v1_12_R1":
                    title = new DadoSS_1_12_R1();
                    break;
                case "v1_11_R1":
                    title = new DadoSS_1_11_R1();
                    break;
                case "v1_10_R1":
                    title = new DadoSS_1_10_R1();
                    break;
                case "v1_9_R2":
                    title = new DadoSS_1_9_R2();
                    break;
                case "v1_9_R1":
                    title = new DadoSS_1_9_R1();
                    break;
                case "v1_8_R1":
                    title = new DadoSS_1_8_R1();
                    break;
                case "v1_8_R2":
                    title = new DadoSS_1_8_R2();
                    break;
                case "v1_8_R3":
                    title = new DadoSS_1_8_R3();
                    break;
            }
            return title != null;
        }
        return false;
    }

    public HashMap<String, String> getInCheck() {
        return inCheck;
    }

    public static Title getTitle() {
        return title;
    }

}

