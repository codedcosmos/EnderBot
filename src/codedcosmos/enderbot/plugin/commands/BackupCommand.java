package codedcosmos.enderbot.plugin.commands;

import codedcosmos.enderbot.core.ConfigManager;
import codedcosmos.enderbot.core.EnderBot;
import codedcosmos.enderbot.utils.GoogleDrive;
import net.minecraft.server.v1_15_R1.ItemMapEmpty;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
