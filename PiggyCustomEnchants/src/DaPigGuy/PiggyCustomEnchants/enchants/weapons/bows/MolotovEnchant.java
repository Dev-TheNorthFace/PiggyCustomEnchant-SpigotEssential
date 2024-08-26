package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.MolotovEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MolotovEnchant extends ReactiveEnchantment {

    public MolotovEnchant(String name, int maxLevel) {
        this.name = "Molotov";
        this.maxLevel = maxLevel;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) event;
            if (entityEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) entityEvent.getDamager();
                Entity targetEntity = entityEvent.getEntity();
                Location targetLocation = targetEntity.getLocation();
                double boundaries = 0.1 * level;
                for (double x = -boundaries; x <= boundaries; x += 0.1) {
                    for (double z = -boundaries; z <= boundaries; z += 0.1) {
                        Location fireLocation = targetLocation.clone().add(x, 1, z);
                        FallingBlock fireBlock = fireLocation.getWorld().spawnFallingBlock(fireLocation, Material.FIRE.createBlockData());
                        fireBlock.setVelocity(new Vector(x, 0.1, z));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Block block = fireBlock.getLocation().getBlock();
                                if (block.getType() == Material.FIRE) {
                                    block.setType(Material.AIR);
                                }
                                fireBlock.remove();
                            }
                        }.runTaskLater(Bukkit.getPluginManager().getPlugin("PiggyCustomEnchants"), 1638);
                    }
                }

                projectile.remove();
            }
        }
    }
}