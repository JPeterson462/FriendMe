package me.CopyableCougar4.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FriendMe extends JavaPlugin implements Listener {

	public static List<Friendship> friendships;
	public static List<TeleportRequest> teleports;
	
	public static Boolean friendlyFire;
	
	public static JavaPlugin pl;
	
	public void loadFriendships(){
		List<String> data = getConfig().getStringList("data");
		for(String s : data){
			String[] split = s.split(",");
			String send = split[0];
			Player sender = Bukkit.getPlayer(send);
			String receive = split[1];
			Player receiver = Bukkit.getPlayer(receive);
			friendships.add(Friendship.create(sender, receiver));
		}
	}
	
	public void findConfig(){
		File f = new File(this.getDataFolder() + "/config.yml");
		if(!f.exists())
			this.saveDefaultConfig();
	}
	
	public void unloadFriendships(){
		List<String> data = new ArrayList<String>();
		for(Friendship f : friendships){
			String value = f.getSender().getName() + "," + f.getReceiver().getName();
			data.add(value);
 		}
		getConfig().getStringList("data").clear();
		for(String v : data){
			getConfig().getStringList("data").add(v);
		}
		this.saveConfig();
	}
	
	public void onDisable(){
		unloadFriendships();
	}
	
	public void onEnable(){
		pl = this;
		findConfig();
		friendships = new ArrayList<Friendship>();
		teleports = new ArrayList<TeleportRequest>();
		loadFriendships();
		friendlyFire = getConfig().getBoolean("friendly-fire");
		if(getConfig().getBoolean("bar-api")){
			scheduleBar();
		}
		Bukkit.getPluginManager().registerEvents(new FriendMeEvents(), this);
		Bukkit.getPluginManager().registerEvents(new FriendChat(), this);
		Bukkit.getPluginCommand("friend").setExecutor(new FriendCommand());
	}
	
	public static void sendMessage(Player p, Type t, Player other){
		String message = ChatColor.DARK_AQUA + "[FriendMe] " + ChatColor.GOLD;
		switch(t){
			case INVALID_PLAYER:
				message += "Friend request not found";
				break;
			case REQUEST_NOT_FOUND:
				message += "Friend request not found";
				break;
			case SENT_ACCEPT:
				String before = "%player% accepted your request";
				before = before.replaceAll("%player%", other.getName());
				message += before;
				break;
			case RECEIVE_ACCEPT:
				String beforeV = "You accepted %player%'s request";
				beforeV = beforeV.replaceAll("%player%", other.getName());
				message += beforeV;
				break;
			case SENT_DENY:
				String beforeD = "%player% denied your request";
				beforeD = beforeD.replaceAll("%player%", other.getName());
				message += beforeD;
				break;
			case RECEIVE_DENY:
				String beforeR = "You accepted %player%'s request";
				beforeR = beforeR.replaceAll("%player%", other.getName());
				message += beforeR;
				break;
			case ALREADY_FRIENDS:
				String beforeF = "You are already friends with %player%";
				beforeF = beforeF.replaceAll("%player%", other.getName());
				message += beforeF;
				break;
			case SENT_REQUEST:
				String beforeS = "You sent a friend request to %player%";
				beforeS = beforeS.replaceAll("%player%", other.getName());
				message += beforeS;
				break;
			case RECEIVED_REQUEST:
				String beforeT = "You received a friend request from %player%";
				beforeT = beforeT.replaceAll("%player%", other.getName());
				message += beforeT;
				break;
			case INVALID_COMMAND:
				message += "Do /friend for command usage";
				break;
			case FRIENDLY_FIRE:
				message += "Friendly fire is disabled";
				break;
			case FRIEND_ON:
				String beforeO = "Your friend %player% is now online!";
				beforeO = beforeO.replaceAll("%player%", other.getName());
				message += beforeO;
				break;
			case FRIEND_OFF:
				String beforeM = "You friend %player% is no longer online!";
				beforeM = beforeM.replaceAll("%player%", other.getName());
				message += beforeM;
				break;
		}
		p.sendMessage(message);
	}
	
	public enum Type {
		INVALID_PLAYER, REQUEST_NOT_FOUND, SENT_ACCEPT, RECEIVE_ACCEPT, SENT_DENY, FRIENDLY_FIRE,
		RECEIVE_DENY, ALREADY_FRIENDS, SENT_REQUEST, RECEIVED_REQUEST, INVALID_COMMAND, FRIEND_ON,
		FRIEND_OFF;
	}
	
	public void scheduleBar(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers())
					FriendList.displayOnline(p, getConfig().getString("bar-message"));
			}
		}, 20, 20);
	}
	
}
