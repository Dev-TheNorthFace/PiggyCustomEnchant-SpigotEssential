package DaPigGuy.PiggyCustomEnchants.enchants.weapons.DisarmingEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

public class DisarmingEnchant extends ReactiveEnchantment {

    public DisarmingEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Disarming";
        this.rarity = Rarity.UNCOMMON;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof Player) {
                Player victim = (Player) entity;
                Inventory victimInventory = victim.getInventory();
                List<ItemStack> contents = List.of(victimInventory.getContents());
                if (!contents.isEmpty()) {
                    ItemStack itemToDrop = contents.get((int) (Math.random() * contents.size()));
                    victimInventory.removeItem(itemToDrop);
                    victim.getWorld().dropItemNaturally(victim.getLocation(), itemToDrop);
                }
            }
        }
    }
}