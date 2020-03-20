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
package codedcosmos.enderbot.plugin;

import codedcosmos.enderbot.core.ConfigManager;
import codedcosmos.enderbot.discord.JDABot;
import codedcosmos.enderbot.core.EnderBot;
import codedcosmos.enderbot.plugin.commands.BackupCommand;
import codedcosmos.enderbot.utils.GoogleDrive;
import codedcosmos.enderbot.utils.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftPlugin extends JavaPlugin {

	public static JavaPlugin mainPlugin;

	@Override
	public void onEnable(){
		//Fired when the server enables the plugin
		Log.print("Enabling EnderBot v" + EnderBot.getVersion());
		EnderBot.load(true);

		JDABot.initBot();
		mainPlugin = this;
		
		this.getCommand("enderbackup").setExecutor(new BackupCommand());

		getServer().getPluginManager().registerEvents(new MinecraftChatListener(), this);
		
		if (ConfigManager.world_backups_enabled) archiveLoop();
	}

	public void archiveLoop() {
		JavaPlugin plugin = this;
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				GoogleDrive.archiveIfNeeded();
				archiveLoop();
			}
		}, 20L*60*30);
	}


	@Override
	public void onDisable(){
		//Fired when the server stops and disables all plugins
		Log.print("Disabiling EnderBot");
		JDABot.stop();
	}
}
