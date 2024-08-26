package DaPigGuy.PiggyCustomEnchants.entities.PiggyFireball;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

public class PiggyFireball extends SmallFireball implements Listener {

    private final float damage = 5.0f;

    public PiggyFireball(Location location, Entity shooter) {
        super(shooter.getWorld(), shooter, new Vector(0, 0, 0));
        this.teleport(location);
        this.setDirection(location.getDirection());
        this.setShooter(shooter);
    }

    @Override
    public void onCollideWithEntity(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) entity;
            Entity shooter = this.getShooter();
            if (!(shooter instanceof Player) || !AllyChecks.isAlly((Player) shooter, target)) {
                EntityCombustByEntityEvent event = new EntityCombustByEntityEvent(this, target, 100);
                getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    target.setFireTicks(event.getDuration());
                }
            }
        }
        super.onCollideWithEntity(entity);
    }

    @Override
    public void onCollideWithBlock(Block block) {
        this.getWorld().getBlockAt(this.getLocation()).setType(Material.FIRE);
        super.onCollideWithBlock(block);
    }

    @Override
    public void setDirection(Vector direction) {
        super.setVelocity(direction.multiply(1.5));
    }
}