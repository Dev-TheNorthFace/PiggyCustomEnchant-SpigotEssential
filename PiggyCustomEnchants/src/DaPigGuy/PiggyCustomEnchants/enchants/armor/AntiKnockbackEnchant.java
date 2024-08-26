package DaPigGuy.PiggyCustomEnchants.enchants.armor.AntiKnockbackEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AntiKnockbackEnchant extends ReactiveEnchantment {

    public AntiKnockbackEnchant(int id) {
        super(id, "Anti Knockback", 1, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            stack = Math.min(stack, 4);
            double newKnockback = damageEvent.getKnockbackStrength() * (4.0 - stack) / (5.0 - stack);
            damageEvent.setKnockbackStrength(newKnockback);
        }
    }
}