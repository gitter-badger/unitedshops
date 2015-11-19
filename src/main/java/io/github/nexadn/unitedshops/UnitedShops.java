package io.github.nexadn.unitedshops;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.nexadn.unitedshops.command.ShopGUIHandler;
import io.github.nexadn.unitedshops.command.UShopDebug;
import io.github.nexadn.unitedshops.config.ConfigShopMain;
import io.github.nexadn.unitedshops.tradeapi.EcoManager;

public class UnitedShops extends JavaPlugin {
	
	public static Server server;
	public static FileConfiguration conf;
	public static ConfigShopMain shopconf;
	
	private File shopyml = new File("shops.yml");
	
	@Override
	public void onEnable()
	{
		UnitedShops.server = getServer();
		UnitedShops.conf = getConfig();
		UnitedShops.shopconf = new ConfigShopMain(shopyml);
		
		// Hook into Vault
		if ( !EcoManager.initEco() ) {
			// Economy nicht eingestellt...
			UnitedShops.server.getLogger().log(Level.SEVERE, "The Economy hook couldn't be initialized. Is Vault missing?");
		}
		
		// Commande executors
		UnitedShops.server.getPluginCommand("ushopdebug").setExecutor(new UShopDebug());		// /ushopdebug
		UnitedShops.server.getPluginCommand("ushop").setExecutor(new ShopGUIHandler());			// /ushop
		
	}
	
	@Override
	public void onDisable()
	{
		saveConfig();
		UnitedShops.shopconf.saveConf();
	}
}

/*
	TODO: 
	- [DONE] Testbefehl hinzufügen
	- [DONE] Testbefehl Executor registrieren
	- GUI vervollständigen
	- EventHandler einstellen
	- [WIP/ERROR] CommandExecutor setzen
	- [DONE] Permissions 
*/