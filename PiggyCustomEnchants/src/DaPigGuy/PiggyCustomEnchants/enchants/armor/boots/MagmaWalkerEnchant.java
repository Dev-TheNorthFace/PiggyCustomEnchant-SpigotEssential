package DaPigGuy.PiggyCustomEnchants.enchants.armor.boots.MagmaWalkerEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class MagmaWalkerEnchant extends ReactiveEnchantment {

    public MagmaWalkerEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id, "Magma Walker", 2, CustomEnchant.TYPE_BOOTS, CustomEnchant.ITEM_TYPE_BOOTS);
        setRarity(CustomEnchant.Rarity.UNCOMMON);
        setUsageType(CustomEnchant.TYPE_BOOTS);
        setItemType(CustomEnchant.ITEM_TYPE_BOOTS);
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{PlayerMoveEvent.class};
    }

    @Override
    public Map<String, Object> getDefaultExtraData() {
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("baseRadius", 2);
        extraData.put("radiusMultiplier", 1);
        return extraData;
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
            Block currentBlock = player.getLocation().getBlock();
            Block belowBlock = currentBlock.getRelative(BlockFace.DOWN);
            if (belowBlock.getType() != Material.LAVA) {
                int radius = level * (int) getExtraData().get("radiusMultiplier") + (int) getExtraData().get("baseRadius");
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        Block block = belowBlock.getRelative(x, 0, z);
                        Block blockAbove = block.getRelative(BlockFace.UP);
                        if (block.getType() == Material.LAVA && block.getBlockData().getAsString().contains("level=0") && blockAbove.getType() == Material.AIR) {
                            block.setType(Material.OBSIDIAN);
                        }
                    }
                }
            }
        }
    }
}