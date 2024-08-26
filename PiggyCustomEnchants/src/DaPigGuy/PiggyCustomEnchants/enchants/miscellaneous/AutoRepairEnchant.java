package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.AutoRepairEnchant;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.Material;

public class AutoRepairEnchant extends ReactiveEnchantment implements Listener {

    public AutoRepairEnchant() {
        super("Autorepair", Rarity.UNCOMMON, CustomEnchant.TYPE_ANY_INVENTORY, CustomEnchant.ITEM_TYPE_DAMAGEABLE);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return PlayerMoveEvent.class;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType().isDamageable()) {
                int maxDamage = item.getType().getMaxDurability();
                if (item.getDurability() > 0) {
                    int repairAmount = getRepairAmount(item);
                    if (item.getDurability() - repairAmount < 0) {
                        item.setDurability((short) 0);
                    } else {
                        item.setDurability((short) (item.getDurability() - repairAmount));
                    }
                    inventory.setItem(i, item);
                }
            }
        }
    }

    private int getRepairAmount(ItemStack item) {
        int level = getEnchantLevel(item);
        int baseRepair = (int) getExtraData().get("baseRepair");
        int repairMultiplier = (int) getExtraData().get("repairMultiplier");
        return baseRepair + (repairMultiplier * level);
    }

    @Override
    public int getPriority() {
        return 2;
    }
}