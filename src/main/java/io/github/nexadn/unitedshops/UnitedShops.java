package io.github.nexadn.unitedshops;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.nexadn.unitedshops.command.*;
import io.github.nexadn.unitedshops.config.ConfigMessages;
import io.github.nexadn.unitedshops.events.*;
import io.github.nexadn.unitedshops.shop.*;
import io.github.nexadn.unitedshops.tradeapi.EcoManager;

public class UnitedShops extends JavaPlugin {
    private boolean                                 unitTest = false;

    private HashMap<OfflinePlayer, AutoSellManager> autoSaleInventories;
    private HashMap<String, String>                 messages;

    public static UnitedShops                       plugin;

    @SuppressWarnings ("unused")
    private Metrics                                 metrics;

    public UnitedShops()
    {
    }

    /** Unit Test constructor */
    public UnitedShops(JavaPluginLoader mockPluginLoader, PluginDescriptionFile pluginDescriptionFile, File pluginDir,
            File file)
    {
        super(mockPluginLoader, pluginDescriptionFile, pluginDir, file);
        log(Level.SEVERE, "Running in Unit testing mode!");
    }

    @Override
    /** Enable the plugin */
    public void onEnable ()
    {
        plugin = this;
        this.autoSaleInventories = new HashMap<OfflinePlayer, AutoSellManager>();

        if (!this.unitTest)
            this.metrics = new Metrics(this);

        this.getLogger().log(Level.FINE, "Establishing economy hook...");
        if (!EcoManager.initEco())
        {
            this.getLogger().log(Level.SEVERE, "The Economy hook couldn't be initialized. Is Vault missing?");
            this.setEnabled(false);
            return;
        }
        this.getLogger().log(Level.FINE, "Economy hook successful.");

        // config.yml
        try
        {
            if (!getDataFolder().exists())
            {
                getDataFolder().mkdirs();
            }
            File configyml = new File(getDataFolder(), "config.yml");
            if (!configyml.exists())
            {
                this.getLogger().log(Level.INFO, "config.yml not found, creating a new one just for you!");
                this.saveResource("config.yml", true);
            }
            File messagesyml = new File(getDataFolder(), "messages.yml");
            if (!messagesyml.exists())
            {
                this.getLogger().log(Level.INFO, "messages.yml not found, creating a new one just for you!");
                this.saveResource("messages.yml", true);
            }
        } catch (Exception e)
        {
            this.getLogger().log(Level.INFO, e.getMessage());
            e.printStackTrace();
            this.setEnabled(false);
        }

        ConfigMessages configMessages = new ConfigMessages(new File(getDataFolder(), "messages.yml"));
        configMessages.parseConfig();
        this.messages = configMessages.getMessages();

        // Command executors
        this.getServer().getPluginCommand("ushop").setExecutor(new ShopGUIHandler()); // /ushop
        this.getServer().getPluginCommand("usell").setExecutor(new AutoSellHandler()); // /usell

        // Event handler
        this.getServer().getPluginManager().registerEvents(new GUIClick(), this);
        this.getServer().getPluginManager().registerEvents(new OnInventoryClose(), this);

        GUIContainer.initGUI();

        if (this.unitTest)
            UnitedShops.plugin.log(Level.SEVERE, "Running in unit testing mode!");
    }

    public void onUnitTest ()
    {
        this.unitTest = true;
    }

    @Override
    public void onDisable ()
    {
        saveConfig();
    }

    public void log (Level loglevel, String message)
    {
        this.getLogger().log(loglevel, message);
    }

    public void sendMessage (CommandSender target, String message)
    {
        target.sendMessage("[" + this.getName() + "] " + message);
    }

    public AutoSellManager getAutoSellManager (OfflinePlayer player)
    {
        if ( (!this.autoSaleInventories.containsKey(player)) || this.autoSaleInventories.get(player) == null)
        {
            this.autoSaleInventories.put(player, new AutoSellManager(player));
        }
        return this.autoSaleInventories.get(player);
    }

    public boolean hasAutoSellManager (OfflinePlayer player)
    {
        return this.autoSaleInventories.containsKey(player);

    }

    public boolean isUnitTest ()
    {
        return this.unitTest;
    }

    public String getMessage (String key)
    {
        return this.messages.get(key);
    }
}

/*
 * Copyright (C) 2015, 2016, 2017 Adrian Schollmeyer
 * 
 * This file is part of UnitedShops.
 * 
 * UnitedShops is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
