package DaPigGuy.PiggyCustomEnchants.entities.HomingArrow;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;

public class HomingArrow extends Arrow {

    private final int enchantmentLevel;

    public HomingArrow(Location location, Entity shootingEntity, boolean critical, int enchantmentLevel) {
        super(location.getWorld());
        this.setVelocity(location.getDirection().normalize());
        this.teleport(location);
        this.setShooter(shootingEntity);
        this.setCritical(critical);
        this.enchantmentLevel = enchantmentLevel;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isOnGround()) {
            return;
        }

        LivingEntity target = findNearestEntity(this.enchantmentLevel * 10);
        if (target != null) {
            Vector direction = target.getLocation().add(0, target.getHeight() / 2, 0).subtract(this.getLocation().toVector()).normalize().multiply(1.5);
            this.setVelocity(direction);
            lookAt(target.getLocation().add(0, target.getHeight() / 2, 0));
        }
    }

    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    public LivingEntity findNearestEntity(int range) {
        LivingEntity nearestEntity = null;
        double nearestEntityDistance = range;

        for (Entity entity : this.getWorld().getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                double distance = this.getLocation().distance(livingEntity.getLocation());
                if (distance <= range && distance < nearestEntityDistance && livingEntity.isAlive()) {
                    if (!isAlly(this.getShooter(), livingEntity)) {
                        nearestEntity = livingEntity;
                        nearestEntityDistance = distance;
                    }
                }
            }
        }
        return nearestEntity;
    }

    private boolean isAlly(Entity shooter, LivingEntity target) {
        return false;
    }

    private void lookAt(Location targetLocation) {
        Vector direction = targetLocation.toVector().subtract(this.getLocation().toVector()).normalize();
        double pitch = Math.asin(direction.getY()) * (180 / Math.PI);
        double yaw = Math.atan2(direction.getZ(), direction.getX()) * (180 / Math.PI);
        this.setRotation((float) (yaw - 90), (float) -pitch);
    }
}