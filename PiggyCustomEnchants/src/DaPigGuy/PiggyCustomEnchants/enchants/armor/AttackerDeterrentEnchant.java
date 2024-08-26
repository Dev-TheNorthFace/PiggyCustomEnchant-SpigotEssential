package DaPigGuy.PiggyCustomEnchants.enchants.armor.AttackerDeterrentEnchant;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.List;

public class AttackerDeterrentEnchant extends ReactiveEnchantment {

    private final List<PotionEffectType> effects;
    private final List<Integer> durationMultipliers;
    private final List<Integer> amplifierMultipliers;

    public AttackerDeterrentEnchant(int id, String name, List<PotionEffectType> effects, List<Integer> durationMultipliers, List<Integer> amplifierMultipliers, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.RARE, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
        this.effects = effects;
        this.durationMultipliers = durationMultipliers;
        this.amplifierMultipliers = amplifierMultipliers;
    }

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getDamager() instanceof LivingEntity) {
                LivingEntity damager = (LivingEntity) damageEvent.getDamager();
                for (int i = 0; i < effects.size(); i++) {
                    PotionEffectType effectType = effects.get(i);
                    int duration = durationMultipliers.get(i) * level * 20;
                    int amplifier = amplifierMultipliers.get(i) * level - 1;
                    PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier);
                    damager.addPotionEffect(potionEffect);
                }
            }
        }
    }
}