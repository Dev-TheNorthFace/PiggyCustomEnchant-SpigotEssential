package DaPigGuy.PiggyCustomEnchants.enchants.tools.ExplosiveEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosiveEnchant extends ReactiveEnchantment implements Listener {

    public ExplosiveEnchant() {
        super("Explosive", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
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
            int sizeMultiplier = getExtraData().getOrDefault("sizeMultiplier", 5);
            boolean entityDamage = getExtraData().getOrDefault("entityDamage", true);
            PiggyExplosion explosion = new PiggyExplosion(block.getLocation(), level * sizeMultiplier, player, entityDamage);
            explosion.explodeA();
            explosion.explodeB();
        }
    }

    @Override
    public int getPriority() {
        return 4;
    }
}