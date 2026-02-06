package me.explosivebow;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("ExplosiveBow enabled on Paper!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("explosivebow")) {
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta meta = bow.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Explosive Bow");
            bow.setItemMeta(meta);

            player.getInventory().addItem(bow);
            player.sendMessage(ChatColor.GREEN + "You received an Explosive Bow!");
        }
        return true;
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) return;
        explodeIfExplosiveBow((Arrow) event.getEntity());
    }

    @EventHandler
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow)) return;
        explodeIfExplosiveBow((Arrow) event.getDamager());
    }

    private void explodeIfExplosiveBow(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player)) return;
        Player player = (Player) arrow.getShooter();

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() != Material.BOW) return;
        if (!item.hasItemMeta()) return;
        if (!ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Explosive Bow")) return;

        Location loc = arrow.getLocation();
        arrow.remove();
        loc.getWorld().createExplosion(loc, 3.0F, false, false);
    }
}
