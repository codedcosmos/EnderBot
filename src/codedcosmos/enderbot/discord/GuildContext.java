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
import net.dv8tion.jda.api.entities.Guild;

public class GuildContext {
	private Guild guild;
	private CachedTextChannel ingamechannel;
	private CachedTextChannel backupschannel;

	public GuildContext(Guild guild) {
		this.guild = guild;

		ingamechannel = new CachedTextChannel(ConfigManager.minecraft_ingame_channel_name, guild);
		backupschannel = new CachedTextChannel(ConfigManager.world_backups_channel_name, guild);
	}

	public boolean matches(GuildContext context) {
		return (guild.getIdLong() == context.getIdLong());
	}

	public boolean matches(long guildid) {
		return (guild.getIdLong() == guildid);
	}

	public long getIdLong() {
		return guild.getIdLong();
	}

	public String getName() {
		return guild.getName();
	}

	public CachedTextChannel getInGameChannel() {
		return ingamechannel;
	}

	public CachedTextChannel getBackupsChannel() {
		return backupschannel;
	}
}
