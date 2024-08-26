package DaPigGuy.PiggyCustomEnchants.utils.PiggyExplosion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashSet;
import java.util.Set;

public class PiggyExplosion {

    private final Location center;
    private final double size;
    private final Player player;
    private final boolean entityDamage;

    public PiggyExplosion(Location center, double size, Player player, boolean entityDamage) {
        this.center = center;
        this.size = size;
        this.player = player;
        this.entityDamage = entityDamage;
    }

    public boolean explode() {
        World world = center.getWorld();
        double yield = (1 / size) * 100;

        EntityExplodeEvent explodeEvent = new EntityExplodeEvent(player, center, new HashSet<>(), yield);
        world.getServer().getPluginManager().callEvent(explodeEvent);

        if (explodeEvent.isCancelled()) {
            return false;
        }

        Set<Block> affectedBlocks = explodeEvent.getBlockList();
        double explosionSize = size * 2;
        int minX = (int) Math.floor(center.getX() - explosionSize - 1);
        int maxX = (int) Math.ceil(center.getX() + explosionSize + 1);
        int minY = (int) Math.floor(center.getY() - explosionSize - 1);
        int maxY = (int) Math.ceil(center.getY() + explosionSize + 1);
        int minZ = (int) Math.floor(center.getZ() - explosionSize - 1);
        int maxZ = (int) Math.ceil(center.getZ() + explosionSize + 1);

        for (Entity entity : world.getEntities()) {
            if (entity.getLocation().distance(center) <= explosionSize && entityDamage) {
                Vector motion = entity.getLocation().toVector().subtract(center.toVector()).normalize();
                double distance = center.distance(entity.getLocation()) / explosionSize;
                double impact = 1 - distance;
                double damage = ((impact * impact + impact) / 2) * 8 * size + 1;

                EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(player, entity, EntityDamageByEntityEvent.DamageCause.ENTITY_EXPLOSION, damage);
                world.getServer().getPluginManager().callEvent(damageEvent);

                if (!damageEvent.isCancelled()) {
                    entity.setVelocity(motion.multiply(impact));
                }
            }
        }

        Block airBlock = world.getBlockAt(center.getBlockX(), center.getBlockY(), center.getBlockZ()).getType() == Material.AIR ? null : Material.AIR;

        for (Block block : affectedBlocks) {
            BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
            world.getServer().getPluginManager().callEvent(breakEvent);

            if (breakEvent.isCancelled()) {
                continue;
            }

            Location blockLocation = block.getLocation();
            if (block.getType() == Material.TNT) {
                block.getWorld().createExplosion(blockLocation, 0);
            } else {
                for (ItemStack drop : breakEvent.getExpToDrop()) {
                    world.dropItemNaturally(blockLocation.add(0.5, 0.5, 0.5), drop);
                }

                BlockState state = block.getState();
                if (state != null) {
                    state.getData().update();
                }
                block.setType(airBlock);
            }
        }

        world.spawnParticle(Particle.EXPLOSION_HUGE, center, 1);
        world.playSound(center, Sound.EXPLODE, 1, 1);
        return true;
    }
}