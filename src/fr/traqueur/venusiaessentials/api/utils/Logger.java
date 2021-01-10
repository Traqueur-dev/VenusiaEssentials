package fr.traqueur.venusiaessentials.api.utils;

import org.bukkit.Bukkit;

import fr.traqueur.venusiaessentials.api.Plugin;

public class Logger {

	public static enum Level {
		INFO("§e", "INFO: "), SUCCESS("§a", "SUCCESS: "), WARNING("§c", "WARNING: "), DEBUG("§5", "DEBUG: "),
		SEVERE("§4", "ERROR: ");

		private String color;
		private String prefix;

		private Level(String color, String prefix) {
			this.color = color;
			this.prefix = prefix;
		}

		public String getColor() {
			return this.color;
		}

		public String getPrefix() {
			return this.prefix;
		}
	}

	public static void log(Level level, Plugin plugin, String message) {
		log(level, plugin, message, true);
	}

	public static void log(Level level, Plugin plugin, String message, boolean color) {
		log(level, plugin.getName(), message, color);
	}

	public static void log(Level level, String prefix, String message) {
		log(level, prefix, message, true);
	}

	public static void log(Level level, String prefix, String message, boolean color) {
		if ((level.equals(Level.DEBUG))) {
			return;
		}
		Bukkit.getConsoleSender()
				.sendMessage((color ? level.getColor() : "") + "[" + prefix + "] " + level.getPrefix() + message);
	}
}