package DaPigGuy.PiggyCustomEnchants.enchants.tools.EnergizingEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnergizingEnchant extends ReactiveEnchantment implements Listener {

    public EnergizingEnchant() {
        super("Energizing", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof BlockBreakEvent) {
            BlockBreakEvent blockBreakEvent = (BlockBreakEvent) event;
            if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                int duration = getExtraData().getOrDefault("duration", 20);
                int baseAmplifier = getExtraData().getOrDefault("baseAmplifier", -1);
                int amplifierMultiplier = getExtraData().getOrDefault("amplifierMultiplier", 1);
                int amplifier = level * amplifierMultiplier + baseAmplifier;
                PotionEffect hasteEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, duration * 20, amplifier, false, false);
                player.addPotionEffect(hasteEffect);
            }
        }
    }
}