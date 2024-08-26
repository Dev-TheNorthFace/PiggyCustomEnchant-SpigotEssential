package DaPigGuy.PiggyCustomEnchants.enchants.armor.CactusEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Collection;

public class CactusEnchant extends TickableEnchantment {

    public CactusEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        Collection<Entity> nearbyEntities = player.getNearbyEntities(1, 1, 1);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && !AllyChecks.isAlly(player, (LivingEntity) entity)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, livingEntity, EntityDamageEvent.DamageCause.CONTACT, 1);
                player.getServer().getPluginManager().callEvent(damageEvent);
                if (!damageEvent.isCancelled()) {
                    livingEntity.damage(damageEvent.getFinalDamage());
                }
            }
        }
    }

    @Override
    public void startTicking(final Player player, final ItemStack item, final Inventory inventory, final int slot, final int level) {
        new BukkitRunnable() {
            @Override
            public void run() {
                tick(player, item, inventory, slot, level);
            }
        }.runTaskTimer(getPlugin(), 0, 20);
    }
}