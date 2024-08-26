package DaPigGuy.PiggyCustomEnchants.entities.PiggyTNT;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

public class PiggyTNT extends TNTPrimed implements Listener {

    private boolean worldDamage;

    public PiggyTNT(Location location, boolean worldDamage) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), 4);
        this.worldDamage = worldDamage;
        this.setFuseTicks(0);
    }

    @Override
    public void explode() {
        Player owner = (Player) this.getSource();
        if (owner == null) {
            return;
        }

        ExplosionPrimeEvent ev = new ExplosionPrimeEvent(this, 4);
        ev.setFire(worldDamage);
        getServer().getPluginManager().callEvent(ev);
        
        if (!ev.isCancelled()) {
            PiggyExplosion explosion = new PiggyExplosion(this.getLocation(), ev.getRadius(), owner);
            if (ev.getFire()) {
                explosion.explodeA();
            }
            explosion.explodeB();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
    }
}