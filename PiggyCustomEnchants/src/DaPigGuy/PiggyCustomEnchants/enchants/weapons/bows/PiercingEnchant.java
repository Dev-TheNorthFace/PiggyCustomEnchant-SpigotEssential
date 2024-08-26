package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.PiercingEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PiercingEnchant extends ReactiveEnchantment {

    public PiercingEnchant(String name, int maxLevel) {
        this.name = "Piercing";
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
                Entity targetEntity = damageEvent.getEntity();
                if (targetEntity instanceof LivingEntity) {
                    double originalDamage = damageEvent.getDamage();
                    double damageAfterModifiers = damageEvent.getFinalDamage();
                    double armorReduction = originalDamage - damageAfterModifiers;
                    damageEvent.setDamage(originalDamage + armorReduction);
                }
            }
        }
    }
}