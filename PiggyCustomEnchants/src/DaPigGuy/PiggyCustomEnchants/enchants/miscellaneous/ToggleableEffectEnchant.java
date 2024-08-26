package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.ToggleableEffectEnchant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;

public class ToggleableEffectEnchant extends ToggleableEnchantment {

    private final PotionEffectType effect;
    private final int baseAmplifier;
    private final int amplifierMultiplier;
    private final Map<String, PotionEffect> previousEffects = new HashMap<>();

    public ToggleableEffectEnchant(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int usageType, int itemType, PotionEffectType effect, int baseAmplifier, int amplifierMultiplier, int rarity) {
        super(plugin, id, name, maxLevel, usageType, itemType, rarity);
        this.effect = effect;
        this.baseAmplifier = baseAmplifier;
        this.amplifierMultiplier = amplifierMultiplier;
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        if (toggle) {
            if (effect.equals(PotionEffectType.JUMP)) {
                Utils.setShouldTakeFallDamage(player, false);
            }

            if (player.hasPotionEffect(effect) && player.getPotionEffect(effect).getAmplifier() > calculateAmplifier(level)) {
                previousEffects.put(player.getName(), player.getPotionEffect(effect));
            }
        } else {
            if (this.getUsageType() != CustomEnchant.TYPE_ARMOR_INVENTORY || this.getArmorStack(player) == 0) {
                if (effect.equals(PotionEffectType.JUMP)) {
                    Utils.setShouldTakeFallDamage(player, true);
                }

                player.removePotionEffect(effect);

                if (previousEffects.containsKey(player.getName())) {
                    player.addPotionEffect(previousEffects.get(player.getName()));
                    previousEffects.remove(player.getName());
                }

                return;
            }
        }

        player.removePotionEffect(effect);

        int amplifier = calculateAmplifier(level);
        player.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, Math.min(amplifier, 255), false, false));
    }

    private int calculateAmplifier(int level) {
        return baseAmplifier + amplifierMultiplier * level;
    }

    @Override
    public int getUsageType() {
        return this.usageType;
    }

    @Override
    public int getItemType() {
        return this.itemType;
    }
}