package DaPigGuy.PiggyCustomEnchants.enchants.armor.SelfDestructEnchant;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.Random;

public class SelfDestructEnchant extends ReactiveEnchantment {

    public SelfDestructEnchant(int id, String name) {
        super(id, name, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDeathEvent) {
            EntityDeathEvent deathEvent = (EntityDeathEvent) event;
            if (deathEvent.getEntity() instanceof Player) {
                Player deadPlayer = (Player) deathEvent.getEntity();
                for (int i = 0; i < level * getExtraData("tntAmountMultiplier"); i++) {
                    Random random = new Random();
                    Location location = deadPlayer.getLocation();
                    PiggyTNT tnt = new PiggyTNT(location, null, false);
                    tnt.setFuse(40);
                    tnt.setOwningEntity(deadPlayer);
                    tnt.setMotion(new Vector(
                            random.nextDouble() * 1.5 - 1,
                            random.nextDouble() * 1.5,
                            random.nextDouble() * 1.5 - 1
                    ));
                    tnt.spawn();
                }
            }
        }
    }
}