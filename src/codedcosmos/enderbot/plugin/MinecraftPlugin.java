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
import codedcosmos.enderbot.discord.GuildContext;
import codedcosmos.enderbot.discord.Guilds;
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
		EnderBot.load();

		JDABot.initBot();
		mainPlugin = this;
		
		this.getCommand("enderbackup").setExecutor(new BackupCommand());

		getServer().getPluginManager().registerEvents(new MinecraftChatListener(), this);

		for (GuildContext context : Guilds.getContexts()) {
			context.getInGameChannel().sendMessage("`Server is Online`");
		}
		
		if (ConfigManager.world_backups_enabled) {
			GoogleDrive.archiveIfNeeded();
			// Bad code but whatever
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			archiveLoop();
		}
	}

	public void archiveLoop() {
		getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
			GoogleDrive.archiveIfNeeded();
			archiveLoop();
		}, 20L*60*60*4);
	}


	@Override
	public void onDisable(){
		// Fired when the server stops and disables all plugins
		Log.print("Disabiling EnderBot");

		for (GuildContext context : Guilds.getContexts()) {
			context.getInGameChannel().sendMessage("`Server is Offline`");
		}

		// Terrible idea but it works I guess
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		JDABot.stop();
	}
}
