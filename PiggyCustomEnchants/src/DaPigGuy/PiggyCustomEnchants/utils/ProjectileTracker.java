package DaPigGuy.PiggyCustomEnchants.utils.ProjectileTracker;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProjectileTracker {

    private static final Map<Projectile, ItemStack> projectiles = new HashMap<>();

    public static void addProjectile(Projectile projectile, ItemStack item) {
        projectiles.put(projectile, item);
    }

    public static boolean isTrackedProjectile(Projectile projectile) {
        return projectiles.containsKey(projectile);
    }

    public static ItemStack getItem(Projectile projectile) {
        return projectiles.get(projectile);
    }

    public static Map<Enchantment, Integer> getEnchantments(Projectile projectile) {
        ItemStack item = projectiles.get(projectile);
        if (item == null) {
            return new HashMap<>();
        }
        return item.getEnchantments();
    }

    public static void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }
}