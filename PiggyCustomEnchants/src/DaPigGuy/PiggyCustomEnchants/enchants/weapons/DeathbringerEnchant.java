package DaPigGuy.PiggyCustomEnchants.enchants.weapons.DeathbringerEnchant;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class DeathbringerEnchant extends ReactiveEnchantment {

    public DeathbringerEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Deathbringer";
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            double base = getDefaultExtraData().get("base");
            double multiplier = getDefaultExtraData().get("multiplier");
            double additionalDamage = base + level * multiplier;
            damageEvent.setDamage(damageEvent.getDamage() + additionalDamage);
        }
    }

    @Override
    public Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("base", 2.0);
        data.put("multiplier", 0.1);
        return data;
    }
}