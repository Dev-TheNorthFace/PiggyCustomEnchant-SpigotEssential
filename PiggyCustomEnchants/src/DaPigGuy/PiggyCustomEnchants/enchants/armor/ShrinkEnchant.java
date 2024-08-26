package DaPigGuy.PiggyCustomEnchants.enchants.armor.ShrinkEnchant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShrinkEnchant extends ToggleableEnchantment implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Boolean> shiftCache = new HashMap<>();
    private final Map<UUID, Integer> shrinkPower = new HashMap<>();
    private final Map<UUID, Double> originalScales = new HashMap<>();
    
    private final int cooldownDuration = 75;

    public ShrinkEnchant(int id, String name) {
        super(id, name, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
        Bukkit.getPluginManager().registerEvents(this, PiggyCustomEnchants);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (event.isSneaking()) {
            if (!shiftCache.getOrDefault(playerId, false)) {
                if (getCooldown(player) > 0) {
                    player.sendMessage("Shrink is still in cooldown: " + getCooldown(player) + "s");
                    return;
                }
                applyShrinkEffect(player);
                shiftCache.put(playerId, true);
            } else {
                player.sendMessage("You are already shrunk.");
            }
        } else {
            shiftCache.remove(playerId);
        }
    }

    private void applyShrinkEffect(Player player) {
        UUID playerId = player.getUniqueId();
        if (getArmorStack(player) == 4) {
            if (originalScales.containsKey(playerId)) {
                player.setScale(originalScales.get(playerId).floatValue());
                player.sendMessage("You have grown back to normal size.");
                originalScales.remove(playerId);
                setCooldown(player, cooldownDuration);
            } else {
                originalScales.put(playerId, (double) player.getScale());
                double newScale = player.getScale() - (0.7 + (getStack(player) * 0.0125));
                player.setScale(newScale);
                player.sendMessage("You have shrunk. Sneak again to grow back to normal size.");
                shrinkPower.put(playerId, 60 * 20);
            }
        }
    }

    private void tick() {
        for (Map.Entry<UUID, Integer> entry : shrinkPower.entrySet()) {
            UUID playerId = entry.getKey();
            int power = entry.getValue();
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && originalScales.containsKey(playerId)) {
                power--;
                player.sendMessage("Shrink power remaining: " + power);
                if (getArmorStack(player) < 4 || power <= 0) {
                    if (power <= 0) {
                        player.setScale(1.0);
                        player.sendMessage("You have grown back to normal size.");
                    }
                    shrinkPower.remove(playerId);
                    originalScales.remove(playerId);
                    setCooldown(player, cooldownDuration);
                } else {
                    shrinkPower.put(playerId, power);
                }
            }
        }
    }

    private void setCooldown(Player player, int seconds) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + seconds * 1000L);
    }

    private int getCooldown(Player player) {
        long endTime = cooldowns.getOrDefault(player.getUniqueId(), 0L);
        long remaining = endTime - System.currentTimeMillis();
        return (int) Math.max(0, remaining / 1000L);
    }
}