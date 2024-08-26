package DaPigGuy.PiggyCustomEnchants.enchants.tools.hoe.FertilizerEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FertilizerEnchant extends ReactiveEnchantment {

    public FertilizerEnchant() {
        super("Fertilizer", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_HOE, 3);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return PlayerInteractEvent.class;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
            Block block = playerInteractEvent.getClickedBlock();
            if (block != null && (block.getType() == Material.GRASS_BLOCK || (block.getType() == Material.DIRT && block.getData() == 0))) {
                int radius = level * getExtraData().get("radiusMultiplier");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                Block newBlock = block.getRelative(x, 0, z);
                                if (newBlock.getType() == Material.GRASS_BLOCK || (newBlock.getType() == Material.DIRT && newBlock.getData() == 0)) {
                                    newBlock.setType(Material.FARMLAND);
                                }
                            }
                        }
                    }
                }.runTaskLater(getPlugin(), 1);
            }
        }
    }
}