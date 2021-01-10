package fr.traqueur.venusiaessentials.modules.teleports;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.traqueur.venusiaessentials.api.utils.Utils;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class TeleportCooldown {

	private String sender;
	private String receiver;
	private long sentMillis;

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public long getSentMillis() {
		return sentMillis;
	}

	public TeleportCooldown(String sender, String receiver) {
		this.sender = sender;
		this.receiver = receiver;
		this.sentMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10L);
	}

	public boolean hasExpired() {
		return (sentMillis - System.currentTimeMillis()) <= 0L;
	}

	public TeleportCooldown send() {
		Player senderPlayer = Bukkit.getPlayer(sender);
		Player receiverPlayer = Bukkit.getPlayer(receiver);

		senderPlayer.sendMessage(
				Utils.color("&eVous venez d'envoyé une demande de téléportation auprés de &c" + receiver + "&e."));
		senderPlayer.sendMessage(Utils.color("&7&oCette demande expirera aprés &c10 secondes&7&o."));

		receiverPlayer.sendMessage(
				Utils.color("&eVous venez de recevoir une requête de téléportation du joueur &c" + sender + "&e."));
        IChatBaseComponent comp = ChatSerializer.a("[\"\",{\"text\":\"[\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpyes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"accepter\",\"color\":\"green\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}},{\"text\":\"ACCEPTER\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpyes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"accepter\",\"color\":\"green\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}},{\"text\":\"]\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpyes\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"accepter\",\"color\":\"green\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}},{\"text\":\" \",\"color\":\"dark_gray\"},{\"text\":\"[\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpno\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"décliner\",\"color\":\"red\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}},{\"text\":\"DECLINER\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpno\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"décliner\",\"color\":\"red\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}},{\"text\":\"]\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpno\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Clique pour \",\"color\":\"yellow\"},{\"text\":\"décliner\",\"color\":\"red\"},{\"text\":\" la demande de téléportation.\",\"color\":\"yellow\"}]}}]");
        PacketPlayOutChat packet = new PacketPlayOutChat(comp, true);
        ((CraftPlayer) receiverPlayer).getHandle().playerConnection.sendPacket(packet);
		
		return this;
	}
}
