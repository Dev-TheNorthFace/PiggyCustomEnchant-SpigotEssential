package DaPigGuy.PiggyCustomEnchants.enchants.weapons.VampireEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class VampireEnchant extends ReactiveEnchantment {

    public VampireEnchant(PiggyCustomEnchants plugin, int id, String name, int rarity) {
        super(plugin, id);
        this.name = name;
        this.rarity = rarity;
        this.maxLevel = 1;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity entity = damageEvent.getEntity();
            double finalDamage = damageEvent.getFinalDamage();
            double healthMultiplier = 0.5;
            double newHealth = player.getHealth() + finalDamage * healthMultiplier;
            player.setHealth(Math.min(newHealth, player.getMaxHealth()));
            double foodMultiplier = 0.5;
            int newFood = (int) (player.getFoodLevel() + finalDamage * foodMultiplier);
            player.setFoodLevel(Math.min(newFood, 20));
        }
    }
}