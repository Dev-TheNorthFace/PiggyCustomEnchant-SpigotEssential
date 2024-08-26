package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.HeadhunterEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class HeadhunterEnchant extends ReactiveEnchantment {

    public HeadhunterEnchant(String name, int maxLevel) {
        this.name = "Headhunter";
        this.rarity = Rarity.UNCOMMON;
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
                Entity hitEntity = damageEvent.getEntity();
                if (hitEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) hitEntity;
                    if (projectile.getLocation().getY() > livingEntity.getLocation().getY() + livingEntity.getEyeHeight()) {
                        double additionalDamage = damageEvent.getDamage() * getDefaultExtraData().get("additionalMultiplier") * level;
                        damageEvent.setDamage(damageEvent.getDamage() + additionalDamage);
                    }
                }
            }
        }
    }

    public static double getAdditionalMultiplier() {
        return 0.1;
    }
    
    private static Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("additionalMultiplier", getAdditionalMultiplier());
        return data;
    }
}