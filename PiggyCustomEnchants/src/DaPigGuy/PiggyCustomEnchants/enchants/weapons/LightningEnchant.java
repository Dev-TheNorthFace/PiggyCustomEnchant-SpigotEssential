package DaPigGuy.PiggyCustomEnchants.enchants.weapons.LightningEnchant;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class LightningEnchant extends ReactiveEnchantment {

    public LightningEnchant(PiggyCustomEnchants plugin, int id, String name, int rarity) {
        super(plugin, id);
        this.name = name;
        this.rarity = rarity;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            Location location = entity.getLocation();
            new BukkitRunnable() {
                @Override
                public void run() {
                    location.getWorld().strikeLightning(location);
                }
            }.runTaskLater(this.plugin, 1);
        }
    }
}