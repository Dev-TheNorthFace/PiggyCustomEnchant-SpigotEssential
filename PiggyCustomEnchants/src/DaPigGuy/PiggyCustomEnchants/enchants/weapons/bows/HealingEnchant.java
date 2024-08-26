package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.HealingEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HealingEnchant extends ReactiveEnchantment {

    public HealingEnchant(String name, int maxLevel) {
        this.name = "Healing";
        this.maxLevel = maxLevel;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) damageEvent.getDamager();
                Entity shooter = (Entity) projectile.getShooter();
                if (shooter instanceof Player && shooter.equals(player)) {
                    double damage = damageEvent.getFinalDamage();
                    double healthToReplenish = damage + (level * getDefaultExtraData().get("healthReplenishMultiplier"));
                    double newHealth = Math.min(player.getHealth() + healthToReplenish, player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
                    player.setHealth(newHealth);
                    for (org.bukkit.event.entity.EntityDamageEvent.DamageModifier modifier : org.bukkit.event.entity.EntityDamageEvent.DamageModifier.values()) {
                        damageEvent.setDamage(modifier, 0);
                    }
                    damageEvent.setDamage(0);
                }
            }
        }
    }

    public static double getHealthReplenishMultiplier() {
        return 1.0;
    }

    private static Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("healthReplenishMultiplier", getHealthReplenishMultiplier());
        return data;
    }
}