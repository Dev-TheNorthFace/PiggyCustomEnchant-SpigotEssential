package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.VolleyEnchant;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class VolleyEnchant extends ReactiveEnchantment {

    private final int base;
    private final int multiplier;

    public VolleyEnchant(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
        this.base = 1;
        this.multiplier = 2;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityShootBowEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityShootBowEvent) {
            EntityShootBowEvent shootEvent = (EntityShootBowEvent) event;
            int amount = base + multiplier * level;
            double anglesBetweenArrows = Math.toRadians(45.0 / (amount - 1));
            double pitch = Math.toRadians(player.getLocation().getPitch() + 90);
            double yaw = Math.toRadians(player.getLocation().getYaw() + 90 - 45.0 / 2);
            Projectile originalProjectile = shootEvent.getProjectile();
            for (int i = 0; i < amount; i++) {
                Vector direction = new Vector(
                        Math.sin(pitch) * Math.cos(yaw + anglesBetweenArrows * i),
                        Math.cos(pitch),
                        Math.sin(pitch) * Math.sin(yaw + anglesBetweenArrows * i)
                );
                Projectile newProjectile = (Projectile) player.getWorld().spawn(player.getEyeLocation(), originalProjectile.getClass());
                newProjectile.setVelocity(direction.normalize().multiply(originalProjectile.getVelocity().length()));
                if (newProjectile instanceof Arrow) {
                    ((Arrow) newProjectile).setPickupStatus(Arrow.PickupStatus.DISALLOWED);
                }
                if (originalProjectile.getFireTicks() > 0) {
                    newProjectile.setFireTicks(originalProjectile.getFireTicks());
                }
                ProjectileTracker.addProjectile(newProjectile, item);
            }
            ProjectileTracker.removeProjectile(originalProjectile);
            originalProjectile.remove();
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getPriority() {
        return 2;
    }
}