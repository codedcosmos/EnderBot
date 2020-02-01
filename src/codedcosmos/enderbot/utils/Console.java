/*
 * EnderBot by codedcosmos
 *
 * EnderBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * EnderBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with EnderBot.  If not, see <https://www.gnu.org/licenses/>.
 */
package codedcosmos.enderbot.utils;

import codedcosmos.enderbot.plugin.MinecraftPlugin;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Console {
	public static void print(String text) {
		MinecraftPlugin.mainPlugin.getServer().broadcastMessage("<"+ChatColor.DARK_GREEN+"EnderBot"+ChatColor.RESET+"> " + text);
	}

	public static void printDiscord(String username, String text) {
		MinecraftPlugin.mainPlugin.getServer().broadcastMessage("<"+ChatColor.LIGHT_PURPLE+"discord : "+username+ChatColor.RESET+"> " + text);
	}
}
