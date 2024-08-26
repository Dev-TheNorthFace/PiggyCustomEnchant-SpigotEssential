package DaPigGuy.PiggyCustomEnchants.entities.PiggyProjectile;

import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PiggyProjectile extends Projectile implements Listener {

    @Override
    public void onHit(ProjectileHitEvent event) {
        this.remove();
        super.onHit(event);
    }

    public boolean canSaveWithChunk() {
        return false;
    }

    @Override
    public boolean isSaveable() {
        return !canSaveWithChunk();
    }
}