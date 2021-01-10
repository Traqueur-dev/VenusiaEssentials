package fr.traqueur.venusiaessentials.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import fr.traqueur.venusiaessentials.VenusiaEssentials;
import fr.traqueur.venusiaessentials.api.commands.CommandArgs;
import fr.traqueur.venusiaessentials.api.commands.ICommand;
import fr.traqueur.venusiaessentials.api.commands.annotations.Command;

public class BlockCommand extends ICommand {

	private Material[] minerals;
	private List<Ingot> ingots;

	public BlockCommand() {
		this.minerals = new Material[] { Material.DIAMOND, Material.GOLD_INGOT, Material.IRON_INGOT, Material.EMERALD };
		this.ingots = Lists.newArrayList();
	}
	
	@Command(name = "block", permission = "base.block")
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();

		if(player.getInventory().getContents().length == 0) {
			return;
		}
		
		new BukkitRunnable() {
			@Override
			public void run() {
				//get ingots
				for (ItemStack item : player.getInventory().getContents()) {
					if(item != null && Arrays.asList(minerals).contains(item.getType())) {
						if(getIngot(item.getType()) != null) {
							Ingot ingot = getIngot(item.getType());
							ingot.increment(item.getAmount());
							player.getInventory().remove(item);
						} else {
							Ingot ingot = new Ingot(item.getType());
							ingot.increment(item.getAmount());
							ingots.add(ingot);	
							player.getInventory().remove(item);
						}
					}
				}
			}
		}.runTaskLater(VenusiaEssentials.getInstance(), 2);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				//make blocks
				for(Material ore : minerals) {
					if(getIngot(ore) != null) {
						Ingot ingot = getIngot(ore);
						while(ingot.getAmount() > 8) {
							if(ingot.getResult() != null) {					
								player.getInventory().addItem(new ItemStack(ingot.getResult()));
								ingot.decrement(9);
							}
						}
					}
				}
			}
		}.runTaskLater(VenusiaEssentials.getInstance(), 2);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				//refund ingots
				for(Ingot ingot : ingots) {
					if(ingot.getAmount() > 0) {
						ItemStack item = new ItemStack(ingot.getMaterial());
						item.setAmount(ingot.getAmount());
						player.getInventory().addItem(item);
						ingot.setAmount(0);	
					}
				}
			}
		}.runTaskLater(VenusiaEssentials.getInstance(), 2);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				//clear ingots
				ingots.clear();
			}
		}.runTaskLater(VenusiaEssentials.getInstance(), 2);
	}
	
	public Ingot getIngot(Material material) {
		for(Ingot ingot : this.ingots) {
			if(ingot.getMaterial() == material) {
				return ingot;
			}
		}
		return null;
	}
	
	public boolean isOre(Material material) {
		for(Material ore : this.minerals) {
			if(ore == material) {
				return true;
			}
		}
		return false;
	}
	
	public class Ingot {
		
		private Material material;
		private Material result;
		private int amount;
		
		public Ingot(Material material) {
			this.material = material;
			this.result();
		}
		
		public void result() {
			switch(this.material) {
			case DIAMOND:
				this.setResult(Material.DIAMOND_BLOCK);
				break;
			case EMERALD:
				this.setResult(Material.EMERALD_BLOCK);
				break;
			case GOLD_INGOT:
				this.setResult(Material.GOLD_BLOCK);
				break;
			case IRON_INGOT:
				this.setResult(Material.IRON_BLOCK);
				break;
			default:
				break;
			}
		}
		
		public void decrement(int i) {
			this.amount = amount - i;
		}
		
		public void increment(int i) {
			this.amount =  amount + i;
		}

		public Material getMaterial() {
			return material;
		}

		public void setMaterial(Material material) {
			this.material = material;
		}

		public Material getResult() {
			return result;
		}

		public void setResult(Material result) {
			this.result = result;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
		
	}
}
