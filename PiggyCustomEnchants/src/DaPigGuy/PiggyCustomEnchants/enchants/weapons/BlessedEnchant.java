package DaPigGuy.PiggyCustomEnchants.enchants.weapons.BlessedEnchant;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlessedEnchant extends ReactiveEnchantment {

    private final String name = "Blessed";
    private final int rarity = Rarity.UNCOMMON;
    private final int maxLevel = 3;

    public BlessedEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().isBadEffect()) {
                    player.removePotionEffect(effect.getType());
                }
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRarity() {
        return rarity;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }
}