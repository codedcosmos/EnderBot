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

import codedcosmos.enderbot.utils.Log;
import codedcosmos.enderbot.utils.TextChannelHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class Guilds {
	private static ArrayList<GuildContext> contexts;

	public static void init() {
		contexts = new ArrayList<GuildContext>();
	}

	public static GuildContext getContextBy(long id) {
		for (GuildContext context : contexts) {
			if (context.matches(id)) return context;
		}

		Log.printErr("No guild of id " + id);

		return null;
	}

	public static void addGuild(Guild guild) {
		for (GuildContext context : contexts) {
			if (context.matches(guild.getIdLong())) return;
		}

		GuildContext context = new GuildContext(guild);
		Guilds.addGuild(context);
	}

	public static void addGuild(GuildContext context) {
		for (GuildContext contextB : contexts) {
			if (context.matches(contextB)) return;
		}

		Log.print("Added Guild " + context.getName());
		contexts.add(context);
	}

	public static GuildContext getContextBy(Guild guild) {
		return getContextBy(guild.getIdLong());
	}

	public static GuildContext getContextBy(MessageReceivedEvent event) {
		return getContextBy(event.getGuild());
	}

	public static boolean isCached(MessageReceivedEvent event, GuildContext guild) {
		if (guild == null) {
			TextChannelHandler.send(event,"Error Guild '" + event.getGuild().getName() + "' is not cached in server");
			return true;
		} return false;
	}

	public static ArrayList<GuildContext> getContexts() {
		return contexts;
	}
}
