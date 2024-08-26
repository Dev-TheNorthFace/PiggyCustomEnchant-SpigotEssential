package DaPigGuy.PiggyCustomEnchants.enchants.armor.ForcefieldEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.Particle;
import java.util.List;

public class ForcefieldEnchant extends ToggleableEnchantment {

    private final double radiusMultiplier = 0.75;

    public ForcefieldEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.MYTHIC, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        int forcefieldLevel = getStack(player);
        if (forcefieldLevel > 0) {
            double radius = forcefieldLevel * radiusMultiplier;
            List<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius);
            for (Entity entity : entities) {
                if (entity instanceof Projectile) {
                    Projectile projectile = (Projectile) entity;
                    if (projectile.getShooter() != player) {
                        Vector newMotion = projectile.getVelocity().multiply(-1);
                        projectile.setVelocity(newMotion);
                    }
                } else if (!(entity instanceof Item || entity instanceof ExperienceOrb) && !AllyChecks.isAlly(player, entity)) {
                    Vector direction = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(-0.75);
                    entity.setVelocity(new Vector(direction.getX(), 0, direction.getZ()));
                }
            }
            if (player.getWorld().getTime() % 5 == 0) {
                double diff = radius / forcefieldLevel;
                for (double theta = 0; theta <= 360; theta += diff) {
                    double x = radius * Math.sin(Math.toRadians(theta));
                    double z = radius * Math.cos(Math.toRadians(theta));
                    player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, player.getLocation().add(x, 0.5, z), 1);
                }
            }
        }
    }
}