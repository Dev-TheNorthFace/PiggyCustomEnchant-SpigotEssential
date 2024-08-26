package DaPigGuy.PiggyCustomEnchants.entities.PiggyLightning;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.BlockPhysicsEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.World;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

public class PiggyLightning extends org.bukkit.entity.LightningStrike implements Listener {

    private int age = 0;

    public PiggyLightning(Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), false);
        this.setLocation(location);
    }

    @Override
    public void run() {
        if (this.isDead()) {
            this.cancel();
            return;
        }
        age++;
        for (Entity entity : this.getNearbyEntities(4, 3, 4)) {
            if (entity instanceof LivingEntity && entity.isValid()) {
                LivingEntity livingEntity = (LivingEntity) entity;
                if (!livingEntity.equals(this.getShooter())) {
                    Player owner = (this.getShooter() instanceof Player) ? (Player) this.getShooter() : null;
                    if (owner == null || !AllyChecks.isAlly(owner, livingEntity)) {
                        EntityCombustEvent combustEvent = new EntityCombustEvent(livingEntity, (int) (Math.random() * (8 - 3 + 1) + 3) * 20);
                        getServer().getPluginManager().callEvent(combustEvent);
                        if (!combustEvent.isCancelled()) {
                            livingEntity.setFireTicks(combustEvent.getDuration());
                        }
                    }
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(this, livingEntity, DamageCause.CUSTOM, 5);
                    getServer().getPluginManager().callEvent(damageEvent);
                    if (!damageEvent.isCancelled()) {
                        livingEntity.damage(damageEvent.getDamage());
                    }
                }
            }
        }
        Block block = this.getWorld().getBlockAt(this.getLocation());
        if (block.getType().isFlammable() && CustomEnchantManager.getPlugin().getConfig().getBoolean("world-damage.lightning", false)) {
            block.setType(Material.FIRE);
        }
        if (age > 20) {
            this.remove();
        }
    }

    @Override
    public EntityType getType() {
        return EntityType.LIGHTNING;
    }
}