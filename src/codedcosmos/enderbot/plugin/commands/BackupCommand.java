package codedcosmos.enderbot.plugin.commands;

import codedcosmos.enderbot.core.ConfigManager;
import codedcosmos.enderbot.utils.GoogleDrive;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackupCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp()) {
				if (!ConfigManager.world_backups_enabled) {
					player.sendMessage("Backups are disabled");
					return true;
				}
				
				GoogleDrive.archive();
				return true;
			} else {
				player.sendMessage("You must be op to use this command");
			}
		}
		return false;
	}
}
