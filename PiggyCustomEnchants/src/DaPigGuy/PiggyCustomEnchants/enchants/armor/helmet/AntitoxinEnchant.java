package DaPigGuy.PiggyCustomEnchants.enchants.armor.helmet.AntitoxinEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AntitoxinEnchant extends ReactiveEnchantment {

    public AntitoxinEnchant(int id) {
        super(id, "Antitoxin", CustomEnchant.Rarity.MYTHIC, CustomEnchant.TYPE_HELMET, CustomEnchant.ITEM_TYPE_HELMET, 1);
    }

    @Override
    public void react(Player player, Inventory inventory, int slot, Event event, int level) {
        if (event instanceof EntityPotionEffectEvent) {
            EntityPotionEffectEvent potionEvent = (EntityPotionEffectEvent) event;
            PotionEffect newEffect = potionEvent.getNewEffect();
            if (newEffect != null && newEffect.getType() == PotionEffectType.POISON) {
                potionEvent.setCancelled(true);
            }
        }
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityPotionEffectEvent.class};
    }
}