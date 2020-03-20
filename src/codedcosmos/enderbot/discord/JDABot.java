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

import codedcosmos.enderbot.core.ConfigManager;
import codedcosmos.enderbot.core.EnderBot;
import codedcosmos.enderbot.utils.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class JDABot {

	private static JDA jda;
	
	public static void main(String[] args) {
		Log.print("Starting EnderBot Discord Subsystem");
		EnderBot.load(false);
		initBot();
	}

	public static void initBot() {
		Guilds.init();

		try {
			JDABuilder builder = new JDABuilder(ConfigManager.discord_bot_token);

			builder.setActivity(Activity.playing("Minecraft"));
			builder.addEventListeners(new DiscordChatListener());
			builder.addEventListeners(new DiscordEventHandler());

			jda = builder.build();

			jda.awaitReady();
		} catch (LoginException | InterruptedException e) {
			Log.printErr(e);
		}
	}
	
	public static void stop() {
		jda.shutdownNow();
	}
}
