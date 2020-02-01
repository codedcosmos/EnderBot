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

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TextChannelHandler {

	// Send
	public static synchronized void send(Object channel, String message) {
		MessageChannel channelToUse = getMessageChannel(channel);
		if (channelToUse == null) return;
		channelToUse.sendMessage(message).queue();
	}

	// SendThenWait
	public static synchronized void sendThenWait(Object channel, String message) {
		MessageChannel channelToUse = getMessageChannel(channel);
		if (channelToUse == null) return;
		channelToUse.sendMessage(message).complete();
	}

	// Parse
	public static synchronized MessageChannel getMessageChannel(Object channel) {
		if (channel instanceof MessageChannel) {
			return (MessageChannel) channel;
		}

		if (channel instanceof MessageReceivedEvent) {
			return (MessageChannel)((MessageReceivedEvent) channel).getChannel();
		}

		Log.printErr("Error could not parse messagechannel from object '" + channel + "' of type '" + channel.getClass().getSimpleName() + "'");
		return null;
	}
}
