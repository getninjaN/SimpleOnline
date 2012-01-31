package se.xtralarge.online;

import java.util.List;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// Main class
public class Online extends JavaPlugin {
	Logger log = Logger.getLogger("Minecraft"); // Log
	private FileConfiguration config = null;
	private static int maxplayers = 0;
	private static int onlineplayers = 0;
	
	// Plug-in enabled
	public void onEnable(){
		log.info(this.getDescription().getFullName() +" has been enabled!");
		
		this.getConfig().options().copyDefaults(true);
		saveConfig();
		
		config = this.getConfig();
	}
	
	// Plug-in disabled
	public void onDisable(){
		log.info(this.getDescription().getFullName() +" has been disabled.");
	}
	
	// When player enters command
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		World world = null;
		List<World> worlds = null;
		onlineplayers = this.getServer().getOnlinePlayers().length;
		maxplayers = this.getServer().getMaxPlayers();
		
		if(cmd.getName().equalsIgnoreCase("online")) {
			// Reload
			if(sender.hasPermission("simpleonline.reload")) {
				if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
					configReload(sender);
					
					return true;
				}
			}
			
			sender.sendMessage(parseMessage(config.getString("messages.players.totalonline"),null,null,null));
			sender.sendMessage(parseMessage(config.getString("messages.uselist"),null,null,null));
		}
		
		if(cmd.getName().equalsIgnoreCase("listplayers")) {
			if(config.getBoolean("showtotalonline")) {
				sender.sendMessage(parseMessage(config.getString("messages.players.totalonline"),null,null,null));
				
				if(onlineplayers > 0) {
					sender.sendMessage(" ");
				}
			}
			
			if (args.length > 1) {
				return true;
			} else if (args.length == 1) {
				world = this.getServer().getWorld(args[0]);
				
				outputWorldPlayers(world, sender, args[0], true);
			} else if (args.length == 0) {
				worlds = this.getServer().getWorlds();
				
				for(int i=0; i < worlds.size(); i++) {
					outputWorldPlayers(worlds.get(i), sender, worlds.get(i).getName(), false);
				}
			}
		}
		
		return true;
	}
	
	// Output
	private void outputWorldPlayers(World world, CommandSender sender, String argWorld, Boolean showNotOnline) {
		if(world != null) {
			String worldName = world.getName();
			List<Player> playerList = world.getPlayers();
			int playerCount = 0;
			String playerOutList = "";
			String ratio = null;
			Player currentPlayer = null;
			
			for (int i=0; i < playerList.size(); i++) {
				currentPlayer = playerList.get(i);
				if(currentPlayer.isOnline()) {
					if(!playerOutList.contains(currentPlayer.getDisplayName())) {
						playerOutList += playerList.get(i).getDisplayName() +" ";
					
						playerCount++;
					}
				}
			}

			ratio = "("+ playerCount +"/"+ this.getServer().getOnlinePlayers().length +")";

			if(playerOutList.length() > 0) {
				String message = parseMessage(config.getString("messages.players.online"), worldName, playerOutList, ratio);
				sender.sendMessage(message);
			} else {
				if(showNotOnline) {
					String message = parseMessage(config.getString("messages.players.nonefound"), worldName, "", "");
					sender.sendMessage(message);
				}
			}
		} else {
			String message = parseMessage(config.getString("messages.world.notexists"), argWorld, "", "");
			sender.sendMessage(message);
		}
	}
	
	// Parse messages
	private static String parseMessage(String configString, String worldName, String playerOutList, String ratio) {
		String parsedString = configString;
		
		parsedString = parsedString.replaceAll("%maxplayers%", maxplayers +"");
		parsedString = parsedString.replaceAll("%online%", onlineplayers +"");
		parsedString = parsedString.replaceAll("%world%", worldName);
		parsedString = parsedString.replaceAll("%players%", playerOutList);
		parsedString = parsedString.replaceAll("%ratio%", ratio);
		parsedString = parsedString.replaceAll("%listplayers%", "/listplayers");
		
		parsedString = colorize(parsedString);
		
		
		return parsedString;
	}
	
	// Put some color on those messages!
	private static String colorize(String string) {
		string = string.replace("<r>", "")
				.replace("<black>", "\u00A70").replace("<navy>", "\u00A71")
				.replace("<green>", "\u00A72").replace("<teal>", "\u00A73")
				.replace("<red>", "\u00A74").replace("<purple>", "\u00A75")
				.replace("<gold>", "\u00A76").replace("<silver>", "\u00A77")
				.replace("<gray>", "\u00A78").replace("<blue>", "\u00A79")
				.replace("<lime>", "\u00A7a").replace("<aqua>", "\u00A7b")
				.replace("<rose>", "\u00A7c").replace("<pink>", "\u00A7d")
				.replace("<yellow>", "\u00A7e").replace("<white>", "\u00A7f");
		
		return string;
	}
	
	private void configReload(CommandSender sender) {
		this.reloadConfig();
		config = this.getConfig();
		sender.sendMessage(this.getDescription().getFullName() +" reloaded");
	}
}
