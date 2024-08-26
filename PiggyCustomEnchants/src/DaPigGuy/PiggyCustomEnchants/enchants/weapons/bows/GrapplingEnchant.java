package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.GrapplingEnchant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GrapplingEnchant extends ReactiveEnchantment {

    private final JavaPlugin plugin;

    public GrapplingEnchant(JavaPlugin plugin, String name, int maxLevel) {
        this.plugin = plugin;
        this.name = "Grappling";
        this.maxLevel = maxLevel;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class, ProjectileHitEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getDamager() instanceof Projectile) {
                Projectile projectile = (Projectile) damageEvent.getDamager();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (projectile != null && !projectile.isDead()) {
                            Entity entity = damageEvent.getEntity();
                            Location damagerLocation = projectile.getLocation();
                            Location entityLocation = entity.getLocation();
                            double distance = damagerLocation.distance(entityLocation);

                            if (distance > 0) {
                                double motionX = (1.0 + 0.07 * distance) * (damagerLocation.getX() - entityLocation.getX()) / distance;
                                double motionY = (1.0 + 0.03 * distance) * (damagerLocation.getY() - entityLocation.getY()) / distance - 0.5 * -0.08 * distance;
                                double motionZ = (1.0 + 0.07 * distance) * (damagerLocation.getZ() - entityLocation.getZ()) / distance;
                                Vector motion = new Vector(motionX, motionY, motionZ);
                                entity.setVelocity(motion);
                            }
                        }
                    }
                }.runTaskLater(plugin, 1L);

                Utils.setShouldTakeFallDamage(player, false);
            }
        }

        if (event instanceof ProjectileHitEvent) {
            ProjectileHitEvent hitEvent = (ProjectileHitEvent) event;
            Projectile projectile = hitEvent.getEntity();
            if (projectile.getShooter() instanceof Player) {
                Player shooter = (Player) projectile.getShooter();
                Location projectileLocation = projectile.getLocation();
                Location shooterLocation = shooter.getLocation();
                double distance = projectileLocation.distance(shooterLocation);

                if (distance < 6) {
                    if (projectileLocation.getY() > shooterLocation.getY()) {
                        shooter.setVelocity(new Vector(0, 0.25, 0));
                    } else {
                        Vector direction = projectileLocation.toVector().subtract(shooterLocation.toVector());
                        shooter.setVelocity(direction);
                    }
                } else {
                    double motionX = (1.0 + 0.07 * distance) * (projectileLocation.getX() - shooterLocation.getX()) / distance;
                    double motionY = (1.0 + 0.03 * distance) * (projectileLocation.getY() - shooterLocation.getY()) / distance - 0.5 * -0.08 * distance;
                    double motionZ = (1.0 + 0.07 * distance) * (projectileLocation.getZ() - shooterLocation.getZ()) / distance;
                    Vector motion = new Vector(motionX, motionY, motionZ);
                    shooter.setVelocity(motion);
                }

                Utils.setShouldTakeFallDamage(shooter, false);
            }
        }
    }
}