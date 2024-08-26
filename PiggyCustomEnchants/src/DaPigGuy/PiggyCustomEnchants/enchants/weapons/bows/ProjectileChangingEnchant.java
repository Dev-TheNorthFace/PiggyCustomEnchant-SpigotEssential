package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.ProjectileChangingEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ProjectileChangingEnchant extends ReactiveEnchantment {

    private final String projectileType;

    public ProjectileChangingEnchant(PiggyCustomEnchants plugin, int id, String name, String projectileType, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
        this.projectileType = projectileType;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityShootBowEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityShootBowEvent) {
            EntityShootBowEvent shootEvent = (EntityShootBowEvent) event;
            Projectile projectile = shootEvent.getProjectile();
            ProjectileTracker.removeProjectile(projectile);
            Projectile newProjectile = Utils.createNewProjectile(projectileType, projectile.getLocation(), player, projectile, level);
            newProjectile.setVelocity(projectile.getVelocity());
            newProjectile.spawn();
            shootEvent.setProjectile(newProjectile);
            ProjectileTracker.addProjectile(newProjectile, item);
        }
    }

    @Override
    public int getPriority() {
        return 2;
    }
}