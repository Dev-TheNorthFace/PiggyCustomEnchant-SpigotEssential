package DaPigGuy.PiggyCustomEnchants.enchants.tools.pickaxe.JackpotEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class JackpotEnchant extends ReactiveEnchantment {

    private static final Material[] ORE_TIERS = {
        Material.COAL_ORE,
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.DIAMOND_ORE,
        Material.EMERALD_ORE
    };

    public JackpotEnchant() {
        super("Jackpot", Rarity.MYTHIC, CustomEnchant.ITEM_TYPE_PICKAXE);
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
            Material blockType = block.getType();
            int index = getIndexOf(blockType);
            if (index != -1 && index < ORE_TIERS.length - 1) {
                Material nextOre = ORE_TIERS[index + 1];
                List<ItemStack> drops = new ArrayList<>(blockBreakEvent.getBlock().getDrops(item));
                blockBreakEvent.getBlock().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(nextOre));
                blockBreakEvent.setDropItems(false);
                blockBreakEvent.getBlock().setType(nextOre);
            }
        }
    }

    private int getIndexOf(Material material) {
        for (int i = 0; i < ORE_TIERS.length; i++) {
            if (ORE_TIERS[i] == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getPriority() {
        return 3;
    }
}