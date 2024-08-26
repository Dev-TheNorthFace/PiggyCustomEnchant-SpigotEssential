package DaPigGuy.PiggyCustomEnchants.entities.PiggyWitherSkull;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.bukkit.projectiles.ProjectileSource;

public class PiggyWitherSkull extends WitherSkull implements Listener {
    
    private static final double DRAG = 0.01;
    private static final double GRAVITY = 0.05;
    private static final double DAMAGE = 0;
    
    public PiggyWitherSkull(Location location, ProjectileSource shooter) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), shooter);
        this.setFireTicks(0);
        this.setVelocity(new Vector(0, 0, 0));
    }

    @Override
    public void setVelocity(Vector velocity) {
        super.setVelocity(velocity.multiply(1.0 - DRAG));
    }

    @Override
    public void onHitEntity(Entity entityHit) {
        if (entityHit instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityHit;
            ProjectileSource owner = this.getShooter();
            if (owner instanceof Player) {
                Player player = (Player) owner;
                if (!AllyChecks.isAlly(player, livingEntity)) {
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 800, 1));
                }
            }
        }
        super.onHitEntity(entityHit);
    }

    @Override
    public void onHitBlock() {
        super.onHitBlock();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityShootBow(EntityShootBowEvent event) {
    }
    
    public static String getNetworkTypeId() {
        return "minecraft:witherskull";
    }
}