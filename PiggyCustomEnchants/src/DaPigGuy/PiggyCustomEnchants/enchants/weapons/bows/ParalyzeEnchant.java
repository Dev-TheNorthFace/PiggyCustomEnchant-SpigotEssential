package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.ParalyzeEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ParalyzeEnchant extends ReactiveEnchantment {

    public ParalyzeEnchant(String name, int maxLevel) {
        this.name = "Paralyze";
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
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (entityEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) entityEvent.getDamager();
                Entity targetEntity = entityEvent.getEntity();
                if (targetEntity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) targetEntity;
                    if (!livingEntity.hasPotionEffect(PotionEffectType.SLOW)) {
                        int slownessDuration = 40 + level * 20;
                        int slownessAmplifier = 4 + level;
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slownessDuration, slownessAmplifier, false, false));
                    }
                    if (!livingEntity.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        int blindnessDuration = 40 + level * 20;
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, blindnessDuration, 1, false, false));
                    }
                    if (!livingEntity.hasPotionEffect(PotionEffectType.WEAKNESS)) {
                        int weaknessDuration = 40 + level * 20;
                        int weaknessAmplifier = 4 + level;
                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration, weaknessAmplifier, false, false));
                    }
                }
            }
        }
    }
}