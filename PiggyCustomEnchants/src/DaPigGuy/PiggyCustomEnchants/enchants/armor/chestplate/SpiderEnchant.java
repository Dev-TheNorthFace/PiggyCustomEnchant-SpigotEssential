package DaPigGuy.PiggyCustomEnchants.enchants.armor.chestplate.SpiderEnchant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpiderEnchant extends ToggleableEnchantment implements TickingTrait {

    public SpiderEnchant(int id) {
        super(id, "Spider", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE);
    }

    @Override
    public void tick(Player player, ItemStack item, int slot, int level) {
        player.setCanClimbWalls(canClimb(player));
    }

    @Override
    public void toggle(Player player, ItemStack item, int slot, int level, boolean toggle) {
        if (!toggle) {
            player.setCanClimbWalls(false);
        }
    }

    public boolean canClimb(Player player) {
        Location playerLocation = player.getLocation();
        Block currentBlock = playerLocation.getBlock();
        Block aboveBlock = playerLocation.clone().add(0, 1, 0).getBlock();
        if (isClimbable(currentBlock) || isClimbable(aboveBlock)) {
            return true;
        }

        return false;
    }

    private boolean isClimbable(Block block) {
        return block.getRelative(1, 0, 0).getType().isSolid() ||
               block.getRelative(-1, 0, 0).getType().isSolid() ||
               block.getRelative(0, 0, 1).getType().isSolid() ||
               block.getRelative(0, 0, -1).getType().isSolid();
    }
}