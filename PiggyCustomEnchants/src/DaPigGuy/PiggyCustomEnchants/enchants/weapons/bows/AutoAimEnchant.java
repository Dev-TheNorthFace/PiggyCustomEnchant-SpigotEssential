package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.AutoAimEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class AutoAimEnchant extends TickingEnchantment {

    public AutoAimEnchant(String name, int rarity) {
        this.name = "Auto Aim";
        this.rarity = rarity;
        this.maxLevel = 1;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public void tick(Player player, ItemStack item, int slot, int level) {
        if (player.isSneaking() && player.isOnGround()) {
            LivingEntity target = findNearestEntity(player, level * getDefaultExtraData().get("radiusMultiplier"));
            if (target != null) {
                Location targetLocation = target.getLocation();
                Location playerLocation = player.getLocation();
                Vector direction = targetLocation.toVector().subtract(playerLocation.toVector());
                double yaw = Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
                double length = new Vector(direction.getX(), direction.getZ()).length();
                if (length != 0) {
                    double g = 0.006;
                    double tmp = 1 - g * (g * (length * length) + 2 * direction.getY());
                    double pitch = -Math.toDegrees(Math.atan((1 - Math.sqrt(tmp)) / (g * length)));
                    player.teleport(new Location(player.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), (float) yaw, (float) pitch));
                }
            }
        }
    }

    private LivingEntity findNearestEntity(Player player, double range) {
        LivingEntity nearestEntity = null;
        double nearestEntityDistance = range;
        for (Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof LivingEntity && entity != player && entity.isValid() && !entity.isDead()) {
                double distance = player.getLocation().distance(entity.getLocation());
                if (distance <= range && distance < nearestEntityDistance && !AllyChecks.isAlly(player, (LivingEntity) entity)) {
                    nearestEntity = (LivingEntity) entity;
                    nearestEntityDistance = distance;
                }
            }
        }
        return nearestEntity;
    }
}