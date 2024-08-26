package DaPigGuy.PiggyCustomEnchants.enchants.armor.BerserkerEnchant;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BerserkerEnchant extends ReactiveEnchantment {

    private final int cooldownDuration;

    public BerserkerEnchant(int id, String name, int cooldownDuration, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.RARE, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
        this.cooldownDuration = cooldownDuration;
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityDamageEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageEvent) {
            EntityDamageEvent damageEvent = (EntityDamageEvent) event;
            if (player.getHealth() - damageEvent.getFinalDamage() <= getExtraData("minimumHealth", 4)) {
                if (!player.hasPotionEffect(PotionEffectType.STRENGTH)) {
                    int effectDuration = getExtraData("effectDurationMultiplier", 200) * level;
                    int effectAmplifier = getExtraData("effectAmplifierBase", 3) + (getExtraData("effectAmplifierMultiplier", 1) * level) - 1;
                    PotionEffect potionEffect = new PotionEffect(PotionEffectType.STRENGTH, effectDuration, effectAmplifier);
                    player.addPotionEffect(potionEffect);
                    player.sendMessage("Your bloodloss makes you stronger!");
                }
            }
        }
    }
}