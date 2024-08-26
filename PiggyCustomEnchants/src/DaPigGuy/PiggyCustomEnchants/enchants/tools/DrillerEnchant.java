package DaPigGuy.PiggyCustomEnchants.enchants.tools.DrillerEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class DrillerEnchant extends ReactiveEnchantment {

    private static final Map<String, BlockFace> lastBreakFace = new HashMap<>();

    public DrillerEnchant() {
        super("Driller", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
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
            BlockFace breakFace = lastBreakFace.getOrDefault(player.getName(), BlockFace.UP);

            int distance = level * getExtraData().getOrDefault("distanceMultiplier", 1);
            for (int i = 0; i <= distance; i++) {
                Block sideBlock = block.getRelative(breakFace.getOppositeFace(), i);
                for (BlockFace face : getAdjacentFaces(breakFace)) {
                    Block adjacentBlock = sideBlock.getRelative(face);
                    breakBlock(player, item, adjacentBlock);
                }
                if (!sideBlock.equals(block)) {
                    breakBlock(player, item, sideBlock);
                }
            }
        }
    }

    private void breakBlock(Player player, ItemStack item, Block block) {
        if (block != null && block.getType() != Material.AIR) {
            block.breakNaturally(item);
        }
    }

    private BlockFace[] getAdjacentFaces(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return new BlockFace[] { BlockFace.WEST, BlockFace.EAST, BlockFace.UP, BlockFace.DOWN };
            case EAST:
            case WEST:
                return new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN };
            case UP:
            case DOWN:
                return new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST };
            default:
                return new BlockFace[] {};
        }
    }

    @Override
    public int getPriority() {
        return 3;
    }
}