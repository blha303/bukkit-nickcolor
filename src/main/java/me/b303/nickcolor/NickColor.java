package me.b303.nickcolor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NickColor extends JavaPlugin implements Listener {

	String[] colors = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e"};

	@Override
	public void onEnable() {
		this.getCommand("fixnick").setExecutor(this);
		this.getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
	}

	@Override
	public void onDisable() {
		return;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (commandLabel.equals("fixnick") && sender.hasPermission("nickcolor.fixnick")) {
			Player p;
			if (sender instanceof Player) {
				p = (Player)sender;
			} else if (args.length == 1) {
				p = this.getServer().getPlayer(args[0]);
			} else {
				sender.sendMessage("Usage: /fixnick [name]");
				return true;
			}
			p.setDisplayName(ChatColor.getByChar(getColor(p.getUniqueId().toString())) + p.getName());
			sender.sendMessage(String.format("Name fixed (%s%s)", p.getDisplayName(), ChatColor.RESET));
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event) {
		if (!event.getPlayer().hasPermission("nickcolor.autosetnick")) return;
		if (this.getConfig().contains("players." + event.getPlayer().getName())) {
			event.getPlayer().setDisplayName(this.getConfig().getString("players." + event.getPlayer().getName()));
		}
		this.getLogger().info(getColor(event.getPlayer().getUniqueId().toString()));
		event.getPlayer().setDisplayName(ChatColor.getByChar(getColor(event.getPlayer().getUniqueId().toString())) + event.getPlayer().getName());
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event) {
		this.getConfig().set("players." + event.getPlayer().getUniqueId().toString(), event.getPlayer().getDisplayName());
		this.saveConfig();
	}

	// Utils
	
	public String getColor(String name) {
		return colors[toAscii(name, colors.length - 1)];
	}

	public static int toAscii(String s, int modulus){
		StringBuilder sb = new StringBuilder();
		String ascString;
		long asciiInt;
		for (int i = 0; i < s.length(); i++){
			sb.append((int)s.charAt(i));
		}
		ascString = sb.toString();
		asciiInt = Long.parseLong(ascString);
		return (int) (asciiInt % modulus);
	}

}
