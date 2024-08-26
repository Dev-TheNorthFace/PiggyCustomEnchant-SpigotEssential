package DaPigGuy.PiggyCustomEnchants.enchants.weapons.DisarmorEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.List;

public class DisarmorEnchant extends DisarmingEnchant {

    public DisarmorEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Disarmor";
        this.rarity = Rarity.UNCOMMON;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof Player) {
                Player victim = (Player) entity;
                Inventory armorInventory = victim.getInventory();
                ItemStack[] armorContents = armorInventory.getArmorContents();
                if (armorContents.length > 0) {
                    ItemStack armorItem = armorContents[(int) (Math.random() * armorContents.length)];
                    armorInventory.removeItem(armorItem);
                    victim.getWorld().dropItemNaturally(victim.getLocation(), armorItem);
                }
            }
        }
    }
}