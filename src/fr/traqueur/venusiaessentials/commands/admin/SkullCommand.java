package fr.traqueur.venusiaessentials.commands.admin;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;
import fr.traqueur.venusiaessentials.modules.profiles.Profile;
import fr.traqueur.venusiaessentials.modules.profiles.ProfileModule;

public class SkullCommand extends ICommand {


	@Command(name = "skull", aliases ={"head", "myhead"}, permission = "base.skull")
	public void onCommand(CommandArgs args) {
		ProfileModule profileManager = ProfileModule.getInstance();
		Profile profile = profileManager.getProfile(args.getPlayer().getName());
		
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(profile.getPlayerName());
		item.setItemMeta(meta);
		
		profile.msg("&eVous venez recevoir votre &6tête&e.");
		profile.getPlayer().getInventory().addItem(item);
		profile.getPlayer().updateInventory();
	}

}
