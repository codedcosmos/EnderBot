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

import codedcosmos.enderbot.utils.GoogleDrive;
import codedcosmos.enderbot.utils.Log;

public class EnderBot {

	private static final String VERSION = "1.0";
	private static boolean runningInSpigot = false;

	public static void load(boolean runningInSpigot) {
		EnderBot.runningInSpigot = runningInSpigot;

		if (runningInSpigot) {
			Log.print("Loading for Spigot Runtime");
		} else {
			Log.print("Loading for Local Runtime");
		}

		ConfigManager.load();
	}

	public static boolean isRunningInSpigot() {
		return runningInSpigot;
	}
	
	public static String getVersion() {
		return VERSION;
	}
}
