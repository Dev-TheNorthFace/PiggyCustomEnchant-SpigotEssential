package DaPigGuy.PiggyCustomEnchants.enchants.armor.helmet.FocusedEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FocusedEnchant extends RecursiveEnchant {

    public FocusedEnchant(int id) {
        super(id, "Focused", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_HELMET, CustomEnchant.ITEM_TYPE_HELMET);
    }

    @Override
    public void safeReact(Player player, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityPotionEffectEvent) {
            EntityPotionEffectEvent potionEvent = (EntityPotionEffectEvent) event;
            PotionEffect newEffect = potionEvent.getNewEffect();
            if (newEffect != null && newEffect.getType() == PotionEffectType.CONFUSION) {
                int currentAmplifier = newEffect.getAmplifier();
                int reducedAmplifier = currentAmplifier - (1 + (level * 2));
                if (reducedAmplifier > 0) {
                    PotionEffect reducedEffect = new PotionEffect(PotionEffectType.CONFUSION, newEffect.getDuration(), reducedAmplifier, newEffect.isAmbient(), newEffect.hasParticles(), newEffect.hasIcon());
                    player.removePotionEffect(PotionEffectType.CONFUSION);
                    player.addPotionEffect(reducedEffect);
                }
                potionEvent.setCancelled(true);
            }
        }
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityPotionEffectEvent.class};
    }
}