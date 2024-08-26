package DaPigGuy.PiggyCustomEnchants.enchants.armor.chestplate.ParachuteEnchant;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ParachuteEnchant extends TickableEnchantment {

    public ParachuteEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id, "Parachute", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE);
    }

    @Override
    public void tick(Player player, ItemStack item, int slot, int level) {
        if (isInAir(player) && !player.getAllowFlight() && !player.isClimbing() && 
            (player.getInventory().getBoots() == null || 
            !(player.getInventory().getBoots().getEnchantment(CustomEnchantIds.JETPACK) instanceof JetpackEnchant jetpackEnchant) || 
            !jetpackEnchant.hasActiveJetpack(player))) {
            if (player.getPotionEffect(PotionEffectType.SLOW_FALLING) == null) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 1, false, false));
            }
        } else if (player.getPotionEffect(PotionEffectType.SLOW_FALLING) != null) {
            if (isInAir(player) || !player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getType().equals(Material.AIR)) {
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }
        player.setFallDistance(0);
    }

    @Override
    public void toggle(Player player, ItemStack item, int slot, int level, boolean toggle) {
        if (!toggle && player.getPotionEffect(PotionEffectType.SLOW_FALLING) != null &&
            player.getPotionEffect(PotionEffectType.SLOW_FALLING).getAmplifier() == 1) {
            player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        }
    }

    private boolean isInAir(Player player) {
        for (int y = 1; y <= 5; y++) {
            if (!player.getWorld().getBlockAt(player.getLocation().subtract(0, y, 0)).getType().equals(Material.AIR)) {
                return false;
            }
        }
        return true;
    }
}