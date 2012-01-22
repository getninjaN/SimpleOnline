package se.xtralarge.online;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// Main class
public class Online extends JavaPlugin {
	Logger log = Logger.getLogger("Minecraft"); // Log
	
	// Plug-in enabled
	public void onEnable(){
		log.info(this.getDescription().getFullName() +" has been enabled!");
	}
	
	// Plug-in disabled
	public void onDisable(){
		log.info(this.getDescription().getFullName() +" has been disabled.");
	}
	
	// When player enters command
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = null;
		
		if(sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("Endast spelare i dagsläget.");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("online")) {
			World world = null;
			List<World> worlds = null;
			
			if (args.length > 1) {
				return true;
			} else if (args.length == 1) {
				world = this.getServer().getWorld(args[0]);
				
				outputWorldPlayers(world, player, args[0], true);
			} else if (args.length == 0) {
				worlds = this.getServer().getWorlds();
				
				for(int i=0; i < worlds.size(); i++) {
					outputWorldPlayers(worlds.get(i), player, worlds.get(i).getName(), false);
				}
				//Player[] players = this.getServer().getOnlinePlayers();
				
				//outputServerPlayers(player, players);
			}
		}
		
		return true;
	}
	
	/*
	private void outputServerPlayers(Player player, Player[] players) {
		String playerOutList = "";
		
		for(int i = 0; i < players.length; i++) {
			playerOutList += players[i].getDisplayName() +" ";
		}
		
		player.sendMessage("Spelare "+ ChatColor.GREEN +"online:");
		player.sendMessage(playerOutList);
	}
	*/
	
	private void outputWorldPlayers(World world, Player player, String argWorld, Boolean showNotOnline) {
		if(world != null) {
			String worldName = world.getName();
			List<Player> playerList = world.getPlayers();
			String playerOutList = "";
			
			for (int i=0; i < playerList.size(); i++) {
				playerOutList += playerList.get(i).getDisplayName() +" ";
			}

			if(playerOutList.length() > 0) {
				player.sendMessage(ChatColor.GREEN +"Online"+ ChatColor.WHITE +" i \""+ worldName +"\": "+ playerOutList);
			} else {
				if(showNotOnline) {
					player.sendMessage(ChatColor.RED +"Inga spelare online i \""+ worldName +"\"");
				}
			}
		} else {
			player.sendMessage(ChatColor.RED +"Världen \""+ argWorld +"\" finns inte.");
		}
	}
}
