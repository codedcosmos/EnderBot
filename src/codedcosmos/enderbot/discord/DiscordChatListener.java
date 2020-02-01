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
package codedcosmos.enderbot.discord;

import codedcosmos.enderbot.plugin.MinecraftPlugin;
import codedcosmos.enderbot.utils.Console;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordChatListener extends ListenerAdapter {
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// Just make sure guild is cached
		Guilds.addGuild(event.getGuild());

		// Get context
		GuildContext context = Guilds.getContextBy(event.getGuild());

		// Check if in game channel
		if (context.getInGameChannel().matches(event.getTextChannel())) {
			// Make sure its not a bot
			if (!event.getAuthor().isBot()) {
				Console.printDiscord(event.getAuthor().getName(), event.getMessage().getContentDisplay());
			}
		}
	}
}
