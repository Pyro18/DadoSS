package me.dadoss.util;

import me.dadoss.Main;
import me.dadoss.listener.ListenerDadoSS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ControlGUI {

    private Main main = Main.getInstance();

    // Contains item slot with item config section name
    private HashMap<Integer, String> slots = new HashMap<>();

    private String guiTitle = main.format(main.getConfig().getString("cheater-control-gui.title"));
    private Inventory controlGui;


    private List<Player> canCloseGui = new ArrayList<>();

    public ControlGUI() {
        try {
            FileConfiguration config = main.getConfig();
            controlGui = Bukkit.createInventory(null, config.getInt("cheater-control-gui.size"), guiTitle);

            for (String path : config.getConfigurationSection("cheater-control-gui.items").getKeys(false)) {
                int slot = config.getInt("cheater-control-gui.items." + path + ".slot");
                controlGui.setItem(slot, createItem(path));
                slots.put(slot, path);
            }
        } catch (Exception e) {
            ConsoleCommandSender consoleCommandSender = main.getServer().getConsoleSender();
            consoleCommandSender.sendMessage(ChatColor.RED + getChatSeparator('=', 50));
            consoleCommandSender.sendMessage(ChatColor.RED+"There is something wrong with your \"cheater-control-gui\" configuration");
            consoleCommandSender.sendMessage(ChatColor.RED+"This is a fatal error!");
            consoleCommandSender.sendMessage(ChatColor.RED+"Disabling plugin...");
            consoleCommandSender.sendMessage(ChatColor.RED + getChatSeparator('=', 50));
            main.getServer().getPluginManager().disablePlugin(main);
        }
    }

    private ItemStack createItem(String itemSectionName) {
        String configPath = "cheater-control-gui.items." + itemSectionName;

        ItemStack item = new ItemStack(Material.GLASS, 1, (short) main.getConfig().getInt(configPath + ".clayColor"));
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(main.format(main.getConfig().getString(configPath + ".display-name")));
        itemMeta.setLore(main.colorLore(main.getConfig().getStringList(configPath + ".lore")));

        item.setItemMeta(itemMeta);
        return item;
    }

    private String getChatSeparator(char character, int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }


    public String getTitle() {
        return guiTitle;
    }

    // Slots

    public boolean isSlotEmpty(int slotNumber) {
        if (slots.containsKey(slotNumber)){
            return false;
        }
        return true;
    }

    public String getSlotItemSection(int slotNumber) {
        return slots.get(slotNumber);
    }

    // Click
    public void executeClick(Player cheater, Player checker, String clickedItemConfigSectionName) {
        executeAction(cheater, checker, clickedItemConfigSectionName);

        dispatchCommands(ListenerDadoSS.getCmdsExecutor(clickedItemConfigSectionName + ".commands.executor", checker),
                main.getConfig().getStringList(clickedItemConfigSectionName + ".commands.list"), cheater, checker);

        // Allow the "cheater" to close the "control gui"
        canCloseGui.add(cheater);
    }

    private void executeAction(Player cheater, Player checker, String clickedItemConfigSectionName) {
        String actionName = main.getConfig().getString(clickedItemConfigSectionName + ".action");

        // Continue control
        if (!actionName.equalsIgnoreCase("end")) {
            for (String message : main.getConfig().getStringList("cheater-message")) {
                cheater.sendMessage(main.format(message.replace("%player%", checker.getName())));
            }
            return;
        }

        // End control

        cheater.setAllowFlight(false);

        main.getInCheck().remove(ListenerDadoSS.getKeyFromValue(main.getInCheck(), checker.getName()));
    }

    private void dispatchCommands(CommandSender cmdSender, List<String> commands, Player cheater, Player checker) {
        for (String command : commands){
            main.getServer().dispatchCommand(cmdSender, command
                    .replace("%cheater%", cheater.getName())
                    .replace("%checker%", checker.getName()));
        }
    }


    public void openGui(Player player) {
        player.openInventory(controlGui);
    }


    public List<Player> getCanCloseGui() {
        return canCloseGui;
    }


}
