package DaPigGuy.PiggyCustomEnchants.enchants.armor.EndershiftEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.java.JavaPlugin;

public class EndershiftEnchant extends ReactiveEnchantment {

    public EndershiftEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.RARE, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageEvent) {
            EntityDamageEvent damageEvent = (EntityDamageEvent) event;
            if (player.getHealth() - damageEvent.getFinalDamage() <= 4) {
                if (!player.hasPotionEffect(PotionEffectType.SPEED)) {
                    int speedDuration = getDefaultExtraData().get("speedDurationMultiplier") * level;
                    int speedAmplifier = (level * getDefaultExtraData().get("speedAmplifierMultiplier")) + getDefaultExtraData().get("speedBaseAmplifier");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, speedDuration, speedAmplifier, false, false));
                }
                if (!player.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                    int strengthDuration = getDefaultExtraData().get("strengthDurationMultiplier") * level;
                    int strengthAmplifier = (level * getDefaultExtraData().get("strengthAmplifierMultiplier")) + getDefaultExtraData().get("strengthBaseAmplifier");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, strengthDuration, strengthAmplifier, false, false));
                }
                player.sendMessage("You feel a rush of energy coming from your armor!");
            }
        }
    }

    @Override
    public void applyCooldown(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskLater(getPlugin(), 20 * 10);
    }
}