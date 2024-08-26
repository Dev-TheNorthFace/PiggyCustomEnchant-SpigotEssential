package DaPigGuy.PiggyCustomEnchants.particles.JetpackParticle;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class JetpackParticle {

    private final Particle particleType;
    private final int data;

    public JetpackParticle() {
        this.particleType = Particle.CAMPFIRE_COSY_SMOKE;
        this.data = 0;
    }

    public void spawnParticle(Location location) {
        World world = location.getWorld();
        if (world != null) {
            world.spawnParticle(particleType, location, 10, 0.5, 0.5, 0.5, data);
        }
    }

    public void startSpawning(Location location, long periodTicks) {
        new BukkitRunnable() {
            @Override
            public void run() {
                spawnParticle(location);
            }
        }.runTaskTimer(PiggyCustomEnchants, 0L, periodTicks);
    }
}