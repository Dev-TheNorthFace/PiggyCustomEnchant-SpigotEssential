package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.RadarEnchant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class RadarEnchant extends TickingEnchantment {

    public RadarEnchant(JavaPlugin plugin) {
        super("Radar", CustomEnchant.TYPE_INVENTORY, CustomEnchant.ITEM_TYPE_COMPASS);
        this.plugin = plugin;
    }

    private final JavaPlugin plugin;

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        Player detected = findNearestPlayer(player, level * getExtraData().getOrDefault("radiusMultiplier", 50.0).intValue());
        setCompassPosition(player, detected != null ? detected.getLocation() : player.getWorld().getSpawnLocation());
        if (player.getInventory().getItemInMainHand().equals(item)) {
            if (detected == null) {
                player.sendActionBar(ChatColor.RED + "No players found.");
            } else {
                double distance = player.getLocation().distance(detected.getLocation());
                player.sendActionBar(ChatColor.GREEN + "Nearest player " + Math.round(distance * 10.0) / 10.0 + " blocks away.");
            }
        }
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        if (!toggle && player.isOnline()) {
            setCompassPosition(player, player.getWorld().getSpawnLocation());
        }
    }

    private void setCompassPosition(Player player, Location location) {
        player.setCompassTarget(location);
    }

    private Player findNearestPlayer(Player player, int range) {
        Player nearestPlayer = null;
        double nearestDistance = range;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(player) || !p.isOnline() || p.isDead()) continue;
            double distance = player.getLocation().distance(p.getLocation());
            if (distance <= range && distance < nearestDistance) {
                nearestPlayer = p;
                nearestDistance = distance;
            }
        }

        return nearestPlayer;
    }

    @Override
    public Class<? extends Event> getReagent() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}