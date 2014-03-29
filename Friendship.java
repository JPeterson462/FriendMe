package me.CopyableCougar4.main;

import org.bukkit.entity.Player;

public class Friendship {

	public Player sender, receiver;
	public Status requestStatus;
	
	public Friendship(Player sent, Player receive){
		this.sender = sent;
		this.receiver = receive;
		this.requestStatus = Status.PENDING;
	}
	
	public enum Status {
		PENDING,
		ACCEPTED,
		DENIED,
		REMOVED;
	}
	
	public void accept(){
		this.requestStatus = Status.ACCEPTED;
	}
	
	public void deny(){
		this.requestStatus = Status.DENIED;
	}
	
	public void delete(){
		this.requestStatus = Status.REMOVED;
	}
	
	public Player getSender(){
		return this.sender;
	}
	
	public Player getReceiver(){
		return this.receiver;
	}
	
	public static Friendship create(Player sent, Player receive){
		Friendship f = new Friendship(sent, receive);
		return f;
	}
	
	public static Friendship bySender(Player sender){
		for(Friendship f : FriendMe.friendships){
			if(f.getSender().equals(sender))
				return f;
		}
		return null;
	}
	
	public Status getStatus(){
		return this.requestStatus;
	}
	
	public static boolean isFriend(Player test, Player check){
		for(Friendship f : FriendMe.friendships){
			if(f.getSender().equals(test) && f.getReceiver().equals(check))
				return true;
			if(f.getSender().equals(check) && f.getReceiver().equals(test))
				return true;
		}
		return false;
	}
	
}
