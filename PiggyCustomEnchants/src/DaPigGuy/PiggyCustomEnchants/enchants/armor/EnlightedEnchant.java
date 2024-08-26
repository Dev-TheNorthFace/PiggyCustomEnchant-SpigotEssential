package DaPigGuy.PiggyCustomEnchants.enchants.armor.EnlightedEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.plugin.java.JavaPlugin;

public class EnlightedEnchant extends ReactiveEnchantment {

    public EnlightedEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            int duration = getDefaultExtraData().get("durationMultiplier") * level;
            int amplifier = (level * getDefaultExtraData().get("amplifierMultiplier")) + getDefaultExtraData().get("baseAmplifier");
            PotionEffect regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, duration, amplifier, false, false);
            player.addPotionEffect(regenerationEffect);
            player.sendMessage("You feel a rush of regeneration energy!");
        }
    }
}