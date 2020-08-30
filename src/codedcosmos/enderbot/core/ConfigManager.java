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

package codedcosmos.enderbot.core;

import codedcosmos.enderbot.utils.FileUtils;
import codedcosmos.enderbot.utils.Log;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigManager {

	public static final String CONFIG_RUNTIME_PATH = ".";
	public static final String CONFIG_RUNTIME = "enderbot.properties";

	public static final String CONFIG_SPIGOT_PATH = "plugins/EnderBot/";
	public static final String CONFIG_SPIGOT = CONFIG_SPIGOT_PATH+"enderbot.properties";

	// Config values
	public static String minecraft_ingame_channel_name;
	public static String world_backups_channel_name;
	public static int world_backups_frequency_in_days;
	public static boolean world_backups_enabled;

	public static String world_name;

	// Censored Config values
	public static String discord_bot_token;

	public static HashMap<String, String> getDefault() {
		HashMap<String, String> defaults = new HashMap<String, String>();

		// Where the bot will output and input minecraft server messages to and from discord
		defaults.put("minecraft-ingame-channel-name", "minecraft-in-game");

		// Where the links for the world file will be automatically sent to
		defaults.put("world-backups-channel-name", "minecraft-backups");

		// How often the world will be backed up (in days)
		defaults.put("world-backups-frequency-in-days", "30");
		
		// Defines if the game will be backed up at all
		defaults.put("world-backups-enabled", "False");

		// Defines if the game will be backed up at all
		defaults.put("world_name", "world");

		// Discord Developer API bot token
		defaults.put("discord_bot_token", "");

		return defaults;
	}

	public static void createIfMissing() {
		if (!FileUtils.doesFileExist(getConfig())) {
			Log.print("EnderBot Config File does not exist, generating!");

			FileUtils.mkdir(CONFIG_SPIGOT_PATH);

			// Write to file
			try {
				PrintWriter writer = new PrintWriter(getConfig(), "UTF-8");

				writer.println("# Ender Bot Configuration File");

				HashMap<String, String> defaults = getDefault();
				for (Map.Entry<String, String> entry : defaults.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();

					writer.println(key + "=" + value);
				}

				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public static void load() {
		Log.print("Loading config file!");
		createIfMissing();

		try {
			// Load .properties file
			Properties prop = new Properties();
			prop.load(new FileInputStream(getConfig()));

			// Get values
			minecraft_ingame_channel_name = prop.getProperty("minecraft-ingame-channel-name");
			if (minecraft_ingame_channel_name == null) {
				Log.printErr("Failed to load config 'minecraft-ingame-channel-name");
				Log.printErr("Setting it as default!");
				minecraft_ingame_channel_name = "minecraft-in-game";
			}
			Log.print("Loaded 'minecraft-ingame-channel-name' as " + minecraft_ingame_channel_name);

			world_backups_channel_name = prop.getProperty("world-backups-channel-name");
			if (world_backups_channel_name == null) {
				Log.printErr("Failed to load config 'world-backups-channel-name'");
				Log.printErr("Setting it as default!");
				world_backups_channel_name = "minecraft-backups";
			}
			Log.print("Loaded 'world-backups-channel-name' as " + world_backups_channel_name);

			try {
				world_backups_frequency_in_days = Integer.parseInt(prop.getProperty("world-backups-frequency-in-days"));
			} catch (NumberFormatException e) {
				Log.printErr("Failed to load config 'world-backups-frequency-in-days'");
				Log.printErr("Setting it as default!");
				world_backups_frequency_in_days = 30;
			}
			Log.print("Loaded 'world-backups-frequency-in-days' as " + world_backups_frequency_in_days);
			
			try {
				world_backups_enabled = Boolean.parseBoolean(prop.getProperty("world-backups-enabled").toLowerCase());
			} catch (NumberFormatException e) {
				Log.printErr("Failed to load config 'world-backups-frequency-in-days'");
				Log.printErr("Setting it as default!");
				world_backups_enabled = false;
			}
			Log.print("Loaded 'world-backups-enabled' as " + world_backups_enabled);

			world_name = prop.getProperty("world_name");
			if (world_name == null) {
				Log.printErr("Failed to load config 'world_name");
				Log.printErr("Setting it as default!");
				world_name = "world";
			}
			Log.print("Loaded 'minecraft-ingame-channel-name' as " + world_name);


			// Censored Values
			discord_bot_token = prop.getProperty("discord_bot_token");
			if (discord_bot_token == null) {
				Log.printErr("Failed to load config 'discord_bot_token'");
				Log.printErr("Since this is not set, the plugin will fail to load!");
			}
			Log.print("Loaded 'discord_bot_token'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.print("Loaded EnderBot Configuration Files!");
	}

	private static String getPath() {
		return CONFIG_SPIGOT_PATH;
	}

	private static String getConfig() {
		return CONFIG_SPIGOT;
	}
}
