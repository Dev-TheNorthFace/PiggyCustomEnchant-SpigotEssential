package DaPigGuy.PiggyCustomEnchants.enchants.armor.ReviveEnchant;

import org.bukkit.Color;
import org.bukkit.entity.EntityDamageEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Effect;
import org.bukkit.potion.EffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.world.World;
import org.bukkit.world.effect.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;

public class ReviveEnchant extends ReactiveEnchantment {

    public ReviveEnchant(int id, String name) {
        super(id, name, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, org.bukkit.event.Event event, int level, int stack) {
        if (event instanceof EntityDamageEvent) {
            EntityDamageEvent damageEvent = (EntityDamageEvent) event;
            if (damageEvent.getFinalDamage() >= player.getHealth()) {
                if (level > 1) {
                    item.addEnchantment(new EnchantmentWrapper(CustomEnchantIds.REVIVE), level - 1);
                } else {
                    item.removeEnchantment(new EnchantmentWrapper(CustomEnchantIds.REVIVE));
                }
                player.getInventory().setItem(slot, item);
                player.setHealth(player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
                player.setFoodLevel(player.getFoodLevel());
                player.setTotalExperience(0);
                player.setLevel(0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, getExtraData("nauseaDuration") * 20, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, getExtraData("slownessDuration") * 20, 0, false, false));
                World world = player.getWorld();
                for (double i = player.getLocation().getY(); i <= 256; i += 0.25) {
                    world.spawnParticle(Particle.FLAME, player.getLocation().add(0, i - player.getLocation().getY(), 0), 0);
                }
                player.sendMessage("§aYou were revived.");
                damageEvent.setDamage(0);
            }
        }
    }
}