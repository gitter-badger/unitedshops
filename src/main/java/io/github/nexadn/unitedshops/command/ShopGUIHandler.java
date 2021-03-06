package io.github.nexadn.unitedshops.command;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import io.github.nexadn.unitedshops.UnitedShops;
import io.github.nexadn.unitedshops.shop.GUIContainer;

public class ShopGUIHandler implements CommandExecutor {

    public ShopGUIHandler()
    {

    }

    public boolean onCommand (CommandSender commandSender, Command command, String label, String[] sArgv)
    {
        if (commandSender instanceof Player)
        {
            Player player = (Player) commandSender;
            if (player.hasPermission("unitedshops.useshop"))
            {
                player.openInventory(GUIContainer.getMenuGui()); // Menü GUI öffnen
                //
                return true;
            } else
            {
                commandSender.sendMessage(ChatColor.RED + UnitedShops.plugin.getMessage("missingPermission"));
                return true;
            }
        } else
        {
            commandSender.sendMessage(UnitedShops.plugin.getMessage("playerOnly"));
            return true;
        }
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