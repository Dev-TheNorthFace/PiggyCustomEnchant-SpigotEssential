package DaPigGuy.PiggyCustomEnchants.enchants.tools.SmeltingEnchant;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SmeltingEnchant extends ReactiveEnchantment implements Listener {

    public SmeltingEnchant() {
        super("Smelting", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof BlockBreakEvent) {
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
            ItemStack[] drops = blockBreakEvent.getBlock().getDrops(item).toArray(new ItemStack[0]);
            for (int i = 0; i < drops.length; i++) {
                ItemStack drop = drops[i];
                Material smeltedMaterial = getSmeltedMaterial(drop.getType());
                if (smeltedMaterial != null) {
                    drops[i] = new ItemStack(smeltedMaterial, drop.getAmount());
                }
            }
            blockBreakEvent.setDrops(drops);
        }
    }

    private Material getSmeltedMaterial(Material material) {
        switch (material) {
            case IRON_ORE:
                return Material.IRON_INGOT;
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case COPPER_ORE:
                return Material.COPPER_INGOT;
            default:
                return null;
        }
    }

    @Override
    public int getPriority() {
        return 2;
    }
}