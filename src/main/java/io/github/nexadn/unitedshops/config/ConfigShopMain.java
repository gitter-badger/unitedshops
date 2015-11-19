package io.github.nexadn.unitedshops.config;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.nexadn.unitedshops.shop.ShopInventory;
import io.github.nexadn.unitedshops.shop.ShopObject;

/** Container for the shop config file
 * @author NexAdn
 */
public class ConfigShopMain extends ConfigBase {
	private HashMap<String, ShopInventory> menus;		// Menu container
	
	public ConfigShopMain(File file) {
		super(file);
	}
	public ConfigShopMain(File file, String mainTag)
	{
		super(file, mainTag);
	}
	// parse the Config File 
	public void parseConfig()
	{
		Set<String> kies = super.getSubKeys(false);
		for( String s:kies )
		{
			String title = super.getMainSection().getString(s + ".title");
			Material icon = Material.getMaterial(super.getMainSection().getString(s + ".iconitem"));
			this.menus.put(s, new ShopInventory(title, new ItemStack(icon, 1)) );
			String sect = super.getWorkKey() + "." + s;
			Set<String> subkies = super.getConf().getConfigurationSection(sect).getKeys(false);
			for( String sub:subkies )
			{
				ConfigurationSection sec = super.getConf().getConfigurationSection(sub);
				Material mat = Material.getMaterial(sec.getString("item"));
				ShopObject cont = new ShopObject(mat, sec.getDouble("buy"), sec.getDouble("sell")); // Shop Contents
				this.menus.get(s).addContent(cont);
			}
		}
	}
	
	public List<ShopInventory> getMenus() 
	{
		List<ShopInventory> temp = null;
		Collection<ShopInventory> inv = this.menus.values();
		temp.addAll(inv);
		return temp;
	}
}
