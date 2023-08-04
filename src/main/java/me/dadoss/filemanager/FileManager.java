package me.dadoss.filemanager;

import me.dadoss.Main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class FileManager {    
    private File configFile;
    private FileConfiguration config;

    public FileManager(String nameConfig, String directoryName) {
        configFile = new File(Main.getInstance().getDataFolder() + "/" + directoryName + "/" + nameConfig + ".yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }


    public void set(String path, Object value) {
        config.set(path, value);
        try{
            config.save(configFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public FileConfiguration getConfig() {
        return config;
    }


    public void add(String path, String value) {
        List<String> configList = getConfig().getStringList(path);
        configList.add(value);
        config.set(path, configList);
        try{
            config.save(configFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void remove(String path, String value) {
        List<String> configList = getConfig().getStringList(path);
        configList.remove(value);
        config.set(path, configList);
        try{
            config.save(configFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public File getConfigFile() {
        return configFile;
    }
}
