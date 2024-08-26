package DaPigGuy.PiggyCustomEnchants.enchants.tools.hoe.FarmerEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Crop;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FarmerEnchant extends ReactiveEnchantment {

    public FarmerEnchant() {
        super("Farmer", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_HOE, 1);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof BlockBreakEvent) {
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
            Block block = blockBreakEvent.getBlock();
            if (block.getType() == Material.CROPS || block.getType() == Material.WHEAT) {
                ItemStack seed = new ItemStack(block.getType());
                if (player.getInventory().contains(seed)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Block belowBlock = block.getRelative(BlockFace.DOWN);
                            belowBlock.setType(seed.getType());
                            player.getInventory().removeItem(new ItemStack(seed.getType(), 1));
                        }
                    }.runTaskLater(getPlugin(), 1);
                }
            }
        }
    }
}