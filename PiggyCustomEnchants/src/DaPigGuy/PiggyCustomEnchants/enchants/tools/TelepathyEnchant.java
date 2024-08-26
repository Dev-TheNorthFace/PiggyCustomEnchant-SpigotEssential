package DaPigGuy.PiggyCustomEnchants.enchants.tools.TelepathyEnchant;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class TelepathyEnchant extends ReactiveEnchantment implements Listener {

    public TelepathyEnchant() {
        super("Telepathy", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack[] drops = event.getBlock().getDrops().toArray(new ItemStack[0]);
        for (int i = 0; i < drops.length; i++) {
            ItemStack drop = drops[i];
            if (player.getInventory().addItem(drop).isEmpty()) {
                drops[i] = null;
            } else {
                for (ItemStack inventoryItem : player.getInventory().getStorageContents()) {
                    if (inventoryItem != null && inventoryItem.getType() == drop.getType() && inventoryItem.getAmount() < inventoryItem.getMaxStackSize()) {
                        int space = inventoryItem.getMaxStackSize() - inventoryItem.getAmount();
                        if (drop.getAmount() > space) {
                            drop.setAmount(drop.getAmount() - space);
                            inventoryItem.setAmount(inventoryItem.getMaxStackSize());
                        } else {
                            inventoryItem.setAmount(inventoryItem.getAmount() + drop.getAmount());
                            drop.setAmount(0);
                        }
                        break;
                    }
                }
                if (drop.getAmount() > 0) {
                    drops[i] = drop;
                } else {
                    drops[i] = null;
                }
            }
        }
        event.setExpToDrop(0);
        drops = java.util.Arrays.stream(drops).filter(item -> item != null).toArray(ItemStack[]::new);
        for (ItemStack drop : drops) {
            player.getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
        }
    }

    @Override
    public int getPriority() {
        return 2;
    }
}