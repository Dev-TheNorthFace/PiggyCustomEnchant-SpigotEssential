package DaPigGuy.PiggyCustomEnchants.enchants.tools.QuickeningEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;

public class QuickeningEnchant extends ReactiveEnchantment implements Listener {

    public QuickeningEnchant() {
        super("Quickening", Rarity.UNCOMMON, CustomEnchant.ITEM_TYPE_TOOLS);
    }

    @Override
    public Class<? extends Event> getReagent() {
        return BlockBreakEvent.class;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof BlockBreakEvent) {
            if (player.getPotionEffect(PotionEffectType.SPEED) == null) {
                int duration = getExtraData().getOrDefault("duration", 40);
                int baseAmplifier = getExtraData().getOrDefault("baseAmplifier", 1);
                int amplifierMultiplier = getExtraData().getOrDefault("amplifierMultiplier", 1);
                int amplifier = level * amplifierMultiplier + baseAmplifier;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, duration, amplifier);
                player.addPotionEffect(effect);
            }
        }
    }
}