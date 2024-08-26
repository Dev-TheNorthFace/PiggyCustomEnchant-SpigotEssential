package DaPigGuy.PiggyCustomEnchants.enchants.armor.ArmoredEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmoredEnchant extends ReactiveEnchantment {

    private static final double ABSORBED_DAMAGE_MULTIPLIER = 0.2;

    public ArmoredEnchant(int id, JavaPlugin plugin) {
        super(id, "Armored", 1, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getDamager() instanceof Player) {
                Player damager = (Player) damageEvent.getDamager();
                ItemStack handItem = damager.getInventory().getItemInMainHand();
                ItemMeta meta = handItem.getItemMeta();
                if (handItem.getType().name().endsWith("_SWORD")) {
                    double reducedDamage = damageEvent.getDamage() * ABSORBED_DAMAGE_MULTIPLIER * level;
                    damageEvent.setDamage(damageEvent.getDamage() - reducedDamage);
                }
            }
        }
    }
}