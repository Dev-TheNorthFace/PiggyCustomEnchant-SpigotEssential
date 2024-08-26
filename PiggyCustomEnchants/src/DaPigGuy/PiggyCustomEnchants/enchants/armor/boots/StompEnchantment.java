package DaPigGuy.PiggyCustomEnchants.enchants.armor.boots.StompEnchantment;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class StompEnchantment extends ReactiveEnchantment {

    public StompEnchantment(PiggyCustomEnchants plugin, int id) {
        super(plugin, id, "Stomp", 1, CustomEnchant.TYPE_BOOTS, CustomEnchant.ITEM_TYPE_BOOTS);
        setRarity(CustomEnchant.Rarity.UNCOMMON);
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityDamageEvent.class};
    }

    @Override
    public Map<String, Object> getDefaultExtraData() {
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("redistributedDamageMultiplier", 0.5);
        extraData.put("absorbedDamageMultiplier", 0.75);
        return extraData;
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageEvent) {
            EntityDamageEvent damageEvent = (EntityDamageEvent) event;
            if (damageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                double redistributedDamageMultiplier = (double) getExtraData().get("redistributedDamageMultiplier");
                double absorbedDamageMultiplier = (double) getExtraData().get("absorbedDamageMultiplier");
                for (Entity entity : player.getNearbyEntities(3, 3, 3)) {
                    if (entity instanceof LivingEntity && entity != player) {
                        LivingEntity target = (LivingEntity) entity;
                        double damage = damageEvent.getFinalDamage() * redistributedDamageMultiplier;
                        EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(player, target, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
                        target.damage(damage, player);
                        target.setLastDamageCause(entityDamageByEntityEvent);
                    }
                }
                double absorbedDamage = damageEvent.getFinalDamage() * absorbedDamageMultiplier;
                damageEvent.setDamage(Math.max(0, damageEvent.getDamage() - absorbedDamage));
            }
        }
    }
}