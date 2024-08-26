package DaPigGuy.PiggyCustomEnchants.enchants.armor.boots.JetpackEnchant;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JetpackEnchant extends ReactiveEnchantment {

    private final Map<UUID, Double> powerRemaining = new HashMap<>();
    private final Map<UUID, Long> lastActivated = new HashMap<>();
    private final Map<UUID, Player> activeJetpacks = new HashMap<>();

    public JetpackEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id, "Jetpack", 3, CustomEnchant.TYPE_BOOTS, CustomEnchant.ITEM_TYPE_BOOTS);
        setUsageType(CustomEnchant.TYPE_BOOTS);
        setItemType(CustomEnchant.ITEM_TYPE_BOOTS);
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{PlayerToggleSneakEvent.class, EntityDamageEvent.class};
    }

    @Override
    public Map<String, Object> getDefaultExtraData() {
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("power", 300.0);
        extraData.put("rechargeAmount", 0.66);
        extraData.put("enableAmount", 25.0);
        extraData.put("drainMultiplier", 1.0);
        extraData.put("sprintDrainMultiplier", 1.25);
        extraData.put("speedMultiplier", 1.0);
        extraData.put("sprintSpeedMultiplier", 1.25);
        return extraData;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageEvent && ((EntityDamageEvent) event).getCause() == EntityDamageEvent.DamageCause.FALL && hasActiveJetpack(player)) {
            ((EntityDamageEvent) event).setCancelled(true);
        }

        if (event instanceof PlayerToggleSneakEvent) {
            PlayerToggleSneakEvent toggleEvent = (PlayerToggleSneakEvent) event;
            if (toggleEvent.isSneaking()) {
                if (hasActiveJetpack(player)) {
                    if (!player.isOnGround() && player.getInventory().getChestplate() != null && !player.getAllowFlight()) {
                        player.sendMessage(ChatColor.RED + "It is unsafe to disable your jetpack while in the air.");
                    } else {
                        powerActiveJetpack(player, false);
                    }
                } else {
                    powerActiveJetpack(player, true);
                }
            }
        }
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        if (hasActiveJetpack(player)) {
            Vector direction = player.getLocation().getDirection();
            double speedMultiplier = level * (player.isSprinting() ? (double) getExtraData().get("sprintSpeedMultiplier") : (double) getExtraData().get("speedMultiplier"));
            player.setVelocity(direction.multiply(speedMultiplier));
            player.setFallDistance(0);
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10);
            double timeRemaining = powerRemaining.get(player.getUniqueId()) / 10;
            String powerBar = timeRemaining > 10 ? ChatColor.GREEN.toString() : (timeRemaining > 5 ? ChatColor.YELLOW.toString() : ChatColor.RED.toString());
            player.sendActionBar(powerBar + "Power: " + "|".repeat((int) timeRemaining));
            if (timeRemaining <= 2) {
                player.sendActionBar(ChatColor.RED + "Jetpack low on power: " + "|".repeat((int) (powerRemaining.get(player.getUniqueId()) / 5)));
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (hasActiveJetpack(player)) {
                        double drain = player.isSprinting() ? (double) getExtraData().get("sprintDrainMultiplier") : (double) getExtraData().get("drainMultiplier");
                        powerRemaining.put(player.getUniqueId(), powerRemaining.get(player.getUniqueId()) - drain);
                        if (powerRemaining.get(player.getUniqueId()) <= 0) {
                            player.sendActionBar(ChatColor.RED + "Jetpack has run out of power.");
                            powerActiveJetpack(player, false);
                        }
                    }
                }
            }.runTaskLater(getPlugin(), 20L);
        }
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        if (!toggle && hasActiveJetpack(player)) {
            powerActiveJetpack(player, false);
        }
    }

    public boolean hasActiveJetpack(Player player) {
        return activeJetpacks.containsKey(player.getUniqueId());
    }

    public void powerActiveJetpack(Player player, boolean power) {
        UUID playerUUID = player.getUniqueId();
        if (power) {
            if (!powerRemaining.containsKey(playerUUID)) {
                powerRemaining.put(playerUUID, (double) getExtraData().get("power"));
                activeJetpacks.put(playerUUID, player);
            } else {
                long lastActivatedTime = lastActivated.getOrDefault(playerUUID, System.currentTimeMillis());
                double rechargeAmount = (System.currentTimeMillis() - lastActivatedTime) * (double) getExtraData().get("rechargeAmount") / 1000;
                powerRemaining.put(playerUUID, Math.min(powerRemaining.get(playerUUID) + rechargeAmount, (double) getExtraData().get("power")));
                if (powerRemaining.get(playerUUID) < (double) getExtraData().get("enableAmount")) {
                    player.sendActionBar(ChatColor.RED + "Jetpack needs to charge up to " + getExtraData().get("enableAmount") + " before it can be re-enabled. (" + String.format("%.2f", powerRemaining.get(playerUUID)) + " / " + getExtraData().get("power") + ")");
                    return;
                }
                activeJetpacks.put(playerUUID, player);
            }
        } else {
            activeJetpacks.remove(playerUUID);
            lastActivated.put(playerUUID, System.currentTimeMillis());
        }
    }
}