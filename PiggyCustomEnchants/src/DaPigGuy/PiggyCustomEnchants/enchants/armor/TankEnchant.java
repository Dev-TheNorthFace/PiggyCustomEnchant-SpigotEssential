package DaPigGuy.PiggyCustomEnchants.enchants.armor.TankEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

public class TankEnchant extends ReactiveEnchantment implements Listener {

    private static final double DEFAULT_ABSORBED_DAMAGE_MULTIPLIER = 0.2;

    public TankEnchant(int id, String name) {
        super(id, name, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            ItemStack itemInHand = damager.getInventory().getItemInMainHand();
            if (itemInHand.getType() == Material.DIAMOND_AXE || itemInHand.getType() == Material.IRON_AXE ||
                itemInHand.getType() == Material.GOLDEN_AXE || itemInHand.getType() == Material.STONE_AXE ||
                itemInHand.getType() == Material.WOODEN_AXE) {
                int level = getEnchantmentLevel(damager.getInventory().getArmorContents(), CustomEnchantIds.TANK);
                if (level > 0) {
                    double absorbedDamageMultiplier = getConfig().getDouble("absorbedDamageMultiplier", DEFAULT_ABSORBED_DAMAGE_MULTIPLIER);
                    double finalDamage = event.getFinalDamage();
                    double absorbedDamage = finalDamage * absorbedDamageMultiplier * level;
                    event.setDamage(finalDamage - absorbedDamage);
                }
            }
        }
    }

    private int getEnchantmentLevel(ItemStack[] armorContents, Enchantment enchantment) {
        int level = 0;
        for (ItemStack item : armorContents) {
            if (item != null && item.containsEnchantment(enchantment)) {
                level = Math.max(level, item.getEnchantmentLevel(enchantment));
            }
        }
        return level;
    }

    private PiggyCustomEnchants getPlugin() {
        return PiggyCustomEnchants.getInstance();
    }
}