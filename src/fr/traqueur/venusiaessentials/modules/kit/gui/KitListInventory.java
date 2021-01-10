package fr.traqueur.venusiaessentials.modules.kit.gui;

import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.venusiaessentials.api.inventory.ClickableItem;
import fr.traqueur.venusiaessentials.api.inventory.SmartInventory;
import fr.traqueur.venusiaessentials.api.inventory.content.InventoryContents;
import fr.traqueur.venusiaessentials.api.inventory.content.InventoryProvider;
import fr.traqueur.venusiaessentials.api.inventory.content.Pagination;
import fr.traqueur.venusiaessentials.api.inventory.content.SlotIterator;
import fr.traqueur.venusiaessentials.api.utils.ItemBuilder;
import fr.traqueur.venusiaessentials.modules.kit.Kit;
import fr.traqueur.venusiaessentials.modules.kit.KitModule;

public class KitListInventory implements InventoryProvider {

	public static final SmartInventory INVENTORY = SmartInventory.builder()
			.provider(new KitListInventory()).size(6, 9).title("Vos kits").build();
	
	@Override
	public void init(Player player, InventoryContents contents) {
		KitModule manager = KitModule.getInstance();
		Pagination pagination = contents.pagination();
		ClickableItem[] items = new ClickableItem[manager.getKits().size()];
		Collection<Kit> kits = manager.getKits().values();
		int i = 0;
		for (Kit kit: kits) {
			ItemStack icon = kit.getIcon();
			ItemStack item = new ItemBuilder(icon.getType()).displayname("&4» &eKit &6&l" + kit.getName())
			.build();
			items[i] = ClickableItem.empty(item);
			i++;
		}
		
		pagination.setItems(items);
		pagination.setItemsPerPage(45);
		pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

		contents.set(5, 3, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("§ePage Précédente").build(),
	            e -> INVENTORY.open(player, pagination.previous().getPage())));
	    contents.set(5, 5, ClickableItem.of(new ItemBuilder(Material.ARROW).displayname("§ePage Suivante").build(),
	            e -> INVENTORY.open(player, pagination.next().getPage())));
	}

}
