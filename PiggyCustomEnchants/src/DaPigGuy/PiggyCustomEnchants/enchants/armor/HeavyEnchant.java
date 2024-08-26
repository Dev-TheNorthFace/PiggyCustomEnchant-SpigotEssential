package DaPigGuy.PiggyCustomEnchants.enchants.armor.HeavyEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class HeavyEnchant extends ReactiveEnchantment {

    private final double absorbedDamageMultiplier = 0.2;

    public HeavyEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity damager = damageEvent.getDamager();
            if (damager instanceof Player) {
                Player damagerPlayer = (Player) damager;
                ItemStack itemInHand = damagerPlayer.getInventory().getItemInMainHand();
                if (itemInHand.getType() == org.bukkit.Material.BOW) {
                    double finalDamage = damageEvent.getFinalDamage();
                    damageEvent.setDamage(finalDamage * (1 - absorbedDamageMultiplier * level));
                    damageEvent.setDamage(damageEvent.getDamage() - finalDamage * (absorbedDamageMultiplier * level));
                }
            }
        }
    }
}