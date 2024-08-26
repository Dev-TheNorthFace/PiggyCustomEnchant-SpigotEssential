package DaPigGuy.PiggyCustomEnchants.enchants.armor.OverloadEnchant;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class OverloadEnchant extends ToggleableEnchantment {

    private final int multiplier = 2;
    private final int maxLevel = 3;

    public OverloadEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.MYTHIC, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        double currentMaxHealth = player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        double changeAmount = multiplier * level * (toggle ? 1 : -1);
        double newMaxHealth = currentMaxHealth + changeAmount;
        player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
        double currentHealth = player.getHealth();
        double newHealth = currentHealth * (newMaxHealth / currentMaxHealth);
        player.setHealth(Math.min(newHealth, newMaxHealth));
    }
}