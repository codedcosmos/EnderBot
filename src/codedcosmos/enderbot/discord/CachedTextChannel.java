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

import codedcosmos.enderbot.utils.TextChannelHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class CachedTextChannel {
	private boolean isValid = false;
	private TextChannel channel;

	public CachedTextChannel(String name, Guild guild) {
		List<TextChannel> channels = guild.getTextChannelsByName(name, false);

		for (TextChannel channel : channels) {
			isValid = true;
			this.channel = channel;
		}
	}

	public void sendMessage(String message) {
		if (!isValid) return;
		TextChannelHandler.sendThenWait(channel, message);
	}

	public boolean matches(TextChannel channel) {
		return channel.getIdLong() == this.channel.getIdLong();
	}
}
