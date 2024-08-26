package DaPigGuy.PiggyCustomEnchants.enchants.armor.MoltenEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class MoltenEnchant extends ReactiveEnchantment {

    private final int durationMultiplier = 3;

    public MoltenEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity damager = damageEvent.getDamager();
            if (damager instanceof LivingEntity) {
                LivingEntity livingDamager = (LivingEntity) damager;
                int duration = durationMultiplier * level;
                livingDamager.setFireTicks(Math.min(duration, 160));
            }
        }
    }
}