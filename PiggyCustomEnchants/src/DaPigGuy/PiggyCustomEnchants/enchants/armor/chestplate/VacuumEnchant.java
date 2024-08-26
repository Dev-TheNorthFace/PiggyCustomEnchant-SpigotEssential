package DaPigGuy.PiggyCustomEnchants.enchants.armor.chestplate.VacuumEnchant;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VacuumEnchant extends TickingEnchantment {

    public VacuumEnchant(int id) {
        super(id, "Vacuum", CustomEnchant.Rarity.RARE, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE, 3);
    }

    @Override
    public void tick(Player player, int slot, int level) {
        double radiusMultiplier = 3;
        for (org.bukkit.entity.Entity entity : player.getWorld().getEntities()) {
            if (entity instanceof Item) {
                Location playerLocation = player.getLocation();
                Location entityLocation = entity.getLocation();
                double distance = playerLocation.distance(entityLocation);
                if (distance <= radiusMultiplier * level) {
                    Vector motion = playerLocation.toVector().subtract(entityLocation.toVector()).normalize().multiply(level / 3.0);
                    entity.setVelocity(motion);
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}