package DaPigGuy.PiggyCustomEnchants.enchants.tools.axes.LumberjackEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class LumberjackEnchant extends RecursiveEnchant {

    private static final int MAX_LEVEL = 1;
    private static final int LIMIT = 800;

    public LumberjackEnchant() {
        super("Lumberjack", MAX_LEVEL, CustomEnchant.ITEM_TYPE_AXE);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @Override
    public void safeReact(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof BlockBreakEvent) {
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
            Block block = blockBreakEvent.getBlock();

            if (player.isSneaking()) {
                if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
                    breakTree(block, player, item, 0);
                }
            }
        }
    }

    public void breakTree(Block block, Player player, ItemStack item, int mined) {
        if (mined > LIMIT) return;

        for (BlockFace face : BlockFace.values()) {
            Block adjacentBlock = block.getRelative(face);
            if (adjacentBlock.getType() == Material.LOG || adjacentBlock.getType() == Material.LOG_2) {
                adjacentBlock.breakNaturally(item);
                mined++;
                breakTree(adjacentBlock, player, item, mined);
            }
        }
    }
}