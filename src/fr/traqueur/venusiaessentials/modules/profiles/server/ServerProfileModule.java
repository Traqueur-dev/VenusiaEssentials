package fr.traqueur.venusiaessentials.modules.profiles.server;

import java.io.File;

import fr.traqueur.venusiaessentials.api.Plugin;
import fr.traqueur.venusiaessentials.api.jsons.DiscUtil;
import fr.traqueur.venusiaessentials.api.modules.Saveable;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class ServerProfileModule extends Saveable {

	private static ServerProfileModule instance;
	private ServerProfile serverProfile;

	public ServerProfileModule(Plugin plugin) {
		super(plugin, "Server Profile");
		
		instance = this;
		
		this.serverProfile = new ServerProfile();
	}


	@Override
	public File getFile() {
		return new File(this.getPlugin().getDataFolder(), "storage.json");
	}


	@Override
	public void loadData() {
        String content = DiscUtil.readCatch(this.getFile());
        if (content == null) return;
        
        ServerProfile serverProfile = this.getPlugin().getGson().fromJson(content,new TypeToken<ServerProfile>() {
        }.getType());
        
        this.serverProfile = serverProfile;
	}

	public static ServerProfileModule getInstance() {
		return instance;
	}


	public ServerProfile getServerProfile() {
		return serverProfile;
	}


	@Override
	public void saveData() {
		DiscUtil.writeCatch(this.getFile(), this.getPlugin().getGson().toJson(this.serverProfile));			
	}
}
