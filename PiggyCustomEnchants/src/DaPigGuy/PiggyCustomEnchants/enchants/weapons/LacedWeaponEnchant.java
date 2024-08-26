package DaPigGuy.PiggyCustomEnchants.enchants.weapons.LacedWeaponEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Arrays;
import java.util.List;

public class LacedWeaponEnchant extends ReactiveEnchantment {

    private final List<PotionEffectType> effects;
    private final int[] durationMultiplier;
    private final int[] amplifierMultiplier;
    private final int[] baseDuration;
    private final int[] baseAmplifier;

    public LacedWeaponEnchant(PiggyCustomEnchants plugin, int id, String name, int rarity, List<PotionEffectType> effects, int[] durationMultiplier, int[] amplifierMultiplier, int[] baseDuration, int[] baseAmplifier) {
        super(plugin, id);
        this.name = name;
        this.rarity = rarity;
        this.effects = effects != null ? effects : Arrays.asList(PotionEffectType.POISON);
        this.durationMultiplier = durationMultiplier;
        this.amplifierMultiplier = amplifierMultiplier;
        this.baseDuration = baseDuration;
        this.baseAmplifier = baseAmplifier;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                for (int i = 0; i < effects.size(); i++) {
                    PotionEffectType effectType = effects.get(i);
                    int duration = (baseDuration[i] + durationMultiplier[i] * level) * 20;
                    int amplifier = baseAmplifier[i] + amplifierMultiplier[i] * level;
                    livingEntity.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                }
            }
        }
    }
}