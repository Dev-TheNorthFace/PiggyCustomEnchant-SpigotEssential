package DaPigGuy.PiggyCustomEnchants.enchants.armor.ShieldedEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;

public class ShieldedEnchant extends ToggleableEnchantment {

    private final Map<String, PotionEffect> previousEffects = new HashMap<>();

    public ShieldedEnchant(int id, String name) {
        super(id, name, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        if (toggle) {
            PotionEffect currentResistance = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            if (currentResistance != null && currentResistance.getAmplifier() > level - 1) {
                previousEffects.put(player.getName(), currentResistance);
            }
        } else {
            if (getArmorStack(player) == 0) {
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                PotionEffect previousEffect = previousEffects.remove(player.getName());
                if (previousEffect != null) {
                    player.addPotionEffect(previousEffect);
                }
                return;
            }
        }
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public boolean canEffectsStack() {
        return true;
    }
}