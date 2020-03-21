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

import codedcosmos.enderbot.discord.GuildContext;
import codedcosmos.enderbot.discord.Guilds;
import codedcosmos.enderbot.utils.Console;
import codedcosmos.enderbot.utils.Log;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MinecraftChatListener implements Listener {
	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		for (GuildContext context : Guilds.getContexts()) {
			context.getInGameChannel().sendMessage("**"+event.getPlayer().getDisplayName()+"** : " + event.getMessage());
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (GuildContext context : Guilds.getContexts()) {
			context.getInGameChannel().sendMessage("`"+event.getPlayer().getName()+" joined the game`");
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		for (GuildContext context : Guilds.getContexts()) {
			context.getInGameChannel().sendMessage("`"+event.getPlayer().getDisplayName()+" left the game`");
		}
	}
	
	public String format(String username) {
		String text = "";
		boolean skipNext = false;
		for (int i = 0; i < username.length(); i++) {
			char c = username.charAt(i);
			
			if (c == '§') {
				skipNext = true;
			} else if (skipNext) {
				skipNext = false;
				continue;
			} else {
				text += username.charAt(i);
			}
		}
		
		return text;
	}
}
