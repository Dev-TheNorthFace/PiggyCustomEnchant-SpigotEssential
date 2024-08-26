package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.MissileEnchant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MissileEnchant extends ReactiveEnchantment {

    public MissileEnchant(String name, int maxLevel) {
        this.name = "Missile";
        this.maxLevel = maxLevel;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{ProjectileHitEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof ProjectileHitEvent) {
            ProjectileHitEvent hitEvent = (ProjectileHitEvent) event;
            Projectile projectile = hitEvent.getEntity();
            if (hitEvent.getHitBlock() != null) {
                Location impactLocation = projectile.getLocation();
                int tntCount = (int) (level * getDefaultExtraData().get("multiplier"));
                for (int i = 0; i <= tntCount; i++) {
                    TNTPrimed tnt = projectile.getWorld().spawn(impactLocation, TNTPrimed.class);
                    tnt.setFuseTicks(40);
                    tnt.setSource(player);
                }

                projectile.remove();
            }
        }
    }

    public static double getMultiplier() {
        return 1.0;
    }

    private static Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("multiplier", getMultiplier());
        return data;
    }
}