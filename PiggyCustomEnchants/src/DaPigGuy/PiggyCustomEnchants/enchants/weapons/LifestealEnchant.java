package DaPigGuy.PiggyCustomEnchants.enchants.weapons.LifestealEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LifestealEnchant extends ReactiveEnchantment {

    public LifestealEnchant(PiggyCustomEnchants plugin, int id, String name, int rarity) {
        super(plugin, id);
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof Player) {
                Player targetPlayer = (Player) entity;
                double healAmount = getDefaultExtraData().get("base") + level * getDefaultExtraData().get("multiplier");
                double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
                player.setHealth(newHealth);
            }
        }
    }

    @Override
    public Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("base", 2.0);
        data.put("multiplier", 1.0);
        return data;
    }
}