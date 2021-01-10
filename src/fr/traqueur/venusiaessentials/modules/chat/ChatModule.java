package fr.traqueur.venusiaessentials.modules.chat;

import java.io.File;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import fr.traqueur.venusiaessentials.modules.chat.commands.ChatClearCommand;
import fr.traqueur.venusiaessentials.modules.chat.commands.ChatCommand;
import fr.traqueur.venusiaessentials.modules.chat.commands.ChatSlowCommand;
import fr.traqueur.venusiaessentials.modules.chat.commands.ChatToggleCommand;
import fr.traqueur.venusiaessentials.modules.chat.listeners.ChatListener;

public class ChatModule extends Saveable {

	private Chat chat;
	
	private static ChatModule instance;
	
	public ChatModule(Plugin plugin) {
		super(plugin, "Chat");
		instance = this;
		this.chat = new Chat();
		this.registerCommand(new ChatCommand());
		this.registerCommand(new ChatClearCommand());
		this.registerCommand(new ChatSlowCommand());
		this.registerCommand(new ChatToggleCommand());
		this.registerListener(new ChatListener());
	}

	public Chat getChat() {
		return this.chat;
	}
	
	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "/chat/chat.json");
	}

	@Override
	public void loadData() {
		String content = DiscUtil.readCatch(this.getFile());
		if(content != null) {
			this.chat = this.getGson().fromJson(content, Chat.class);
		}
	}

	@Override
	public void saveData() {
		DiscUtil.writeCatch(this.getFile(), this.getGson().toJson(this.chat));
	}
	
	public static ChatModule getInstance() {
		return instance;
	}

}
