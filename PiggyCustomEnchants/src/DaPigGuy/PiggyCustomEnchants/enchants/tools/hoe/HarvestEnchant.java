package DaPigGuy.PiggyCustomEnchants.enchants.tools.hoe.HarvestEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HarvestEnchant extends ReactiveEnchantment {

    public HarvestEnchant() {
        super("Harvest", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_HOE, 3);
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
            if (isCrops(block.getType())) {
                int radius = level * getExtraData().get("radiusMultiplier");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block newBlock = block.getRelative(x, 0, z);
                                if (isCrops(newBlock.getType())) {
                                    newBlock.breakNaturally(item);
                                }
                            }
                        }
                    }
                }.runTaskLater(getPlugin(), 1);
            }
        }
    }

    private boolean isCrops(Material material) {
        return material == Material.WHEAT || material == Material.CARROTS || material == Material.POTATOES ||
               material == Material.BEETROOTS || material == Material.MELON || material == Material.PUMPKIN;
    }
}