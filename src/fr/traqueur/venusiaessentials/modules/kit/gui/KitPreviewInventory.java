package fr.traqueur.venusiaessentials.modules.kit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.traqueur.venusiaessentials.api.inventory.ClickableItem;
import fr.traqueur.venusiaessentials.api.inventory.SmartInventory;
import fr.traqueur.venusiaessentials.api.inventory.content.InventoryContents;
import fr.traqueur.venusiaessentials.api.inventory.content.InventoryProvider;
import fr.traqueur.venusiaessentials.api.inventory.content.Pagination;
import fr.traqueur.venusiaessentials.api.inventory.content.SlotIterator;
import fr.traqueur.venusiaessentials.modules.kit.Kit;

public class KitPreviewInventory implements InventoryProvider {

	private Kit kit;

	public KitPreviewInventory(Kit kit) {
		this.kit = kit;
	}

	public static void openInv(Player player, Kit kit) {
		SmartInventory.builder()
		.provider(new KitPreviewInventory(kit)).size(6, 9).title("Kit Preview").build().open(player);
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		Pagination pagination = contents.pagination();
		ClickableItem[] items = new ClickableItem[kit.getItems().length];
		int i = 0;
		for (ItemStack item: kit.getItems()) {
			items[i] = ClickableItem.empty(item);
			i++;
		}
	
		pagination.setItems(items);
		pagination.setItemsPerPage(45);
		pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
	}

}
