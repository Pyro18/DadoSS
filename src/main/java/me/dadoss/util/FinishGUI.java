package me.dadoss.util;

import me.dadoss.Main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FinishGUI {

    private Main main = Main.getInstance();

    private String guiTitle = main.format(main.getConfig().getString("finish-gui-title"));
    private Inventory finishGui;

    public FinishGUI() {
        finishGui = Bukkit.createInventory(null, 9, guiTitle);

        finishGui.setItem(1, itemStack(4, "first"));
        finishGui.setItem(4, itemStack(14, "second"));
        finishGui.setItem(7, itemStack(5, "third"));

    }

    private ItemStack itemStack(int data, String option) {
        ItemStack itemStack = new ItemStack(Material.GLASS, 1, (short) data);
        ItemMeta itemMeta = itemStack.getItemMeta();

        String configPath = "finish-" + option + "-option";
        itemMeta.setDisplayName(main.format(main.getConfig().getString(configPath + ".display-name")));
        itemMeta.setLore(main.colorLore(main.getConfig().getStringList(configPath + ".lore")));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    public String getTitle() {
        return guiTitle;
    }


    public void openGui(Player player) {
        player.openInventory(finishGui);
    }

}

