package DaPigGuy.PiggyCustomEnchants.enchants.weapons.ConditionalDamageMultiplierEnchant;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.function.Predicate;

public class ConditionalDamageMultiplierEnchant extends ReactiveEnchantment {

    private final Predicate<EntityDamageByEntityEvent> condition;
    private final double additionalMultiplier;

    public ConditionalDamageMultiplierEnchant(PiggyCustomEnchants plugin, int id, String name, Predicate<EntityDamageByEntityEvent> condition, int rarity) {
        super(plugin, id);
        this.name = name;
        this.rarity = rarity;
        this.condition = condition;
        this.additionalMultiplier = 0.1;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (condition.test(damageEvent)) {
                double newDamage = damageEvent.getDamage() * (1 + additionalMultiplier * level);
                damageEvent.setDamage(newDamage);
            }
        }
    }
}