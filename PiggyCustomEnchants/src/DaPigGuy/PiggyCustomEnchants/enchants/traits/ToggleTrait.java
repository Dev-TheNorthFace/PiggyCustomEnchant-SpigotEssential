package DaPigGuy.PiggyCustomEnchants.enchants.traits.ToggleableEnchantmentBase;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

public abstract class ToggleableEnchantmentBase extends CustomEnchant {

    protected PiggyCustomEnchants plugin;

    private final Map<String, Integer> stack = new HashMap<>();
    private final Map<String, Integer> equippedArmorStack = new HashMap<>();

    public ToggleableEnchantmentBase(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
        this.plugin = plugin;
    }

    public boolean canToggle() {
        return true;
    }

    public void onToggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        String worldName = player.getWorld().getName();
        if (plugin.getConfig().getConfigurationSection("per-world-disabled-enchants") != null) {
            if (plugin.getConfig().getConfigurationSection("per-world-disabled-enchants").contains(worldName) &&
                    plugin.getConfig().getStringList("per-world-disabled-enchants." + worldName).contains(getName().toLowerCase())) {
                return;
            }
        }

        if (getCooldown(player) > 0) {
            return;
        }

        if (toggle) {
            addToStack(player, level);
        } else {
            removeFromStack(player, level);
        }

        toggle(player, item, inventory, slot, level, toggle);
    }

    public abstract void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle);

    public void addToStack(Player player, int level) {
        String playerName = player.getName();
        stack.put(playerName, getStack(player) + level);
        equippedArmorStack.put(playerName, getArmorStack(player) + 1);
    }

    public void removeFromStack(Player player, int level) {
        String playerName = player.getName();
        stack.put(playerName, getStack(player) - level);
        equippedArmorStack.put(playerName, getArmorStack(player) - 1);
    }

    public int getStack(Player player) {
        return stack.getOrDefault(player.getName(), 0);
    }

    public int getArmorStack(Player player) {
        return equippedArmorStack.getOrDefault(player.getName(), 0);
    }

    public static void attemptToggle(Player player, ItemStack item, ToggleableEnchantment enchantmentInstance, Inventory inventory, int slot, boolean toggle) {
        if (enchantmentInstance instanceof CustomEnchant && ((CustomEnchant) enchantmentInstance).canToggle()) {
            CustomEnchant enchantment = (CustomEnchant) enchantmentInstance;
            int usageType = enchantment.getUsageType();
            if (usageType == CustomEnchant.TYPE_ANY_INVENTORY ||
                    (usageType == CustomEnchant.TYPE_HAND && inventory instanceof PlayerInventory && ((PlayerInventory) inventory).getHeldItemIndex() == slot) ||
                    (inventory instanceof org.bukkit.inventory.ArmorInventory && (
                            usageType == CustomEnchant.TYPE_ARMOR_INVENTORY ||
                            (usageType == CustomEnchant.TYPE_HELMET && Utils.isHelmet(item)) ||
                            (usageType == CustomEnchant.TYPE_CHESTPLATE && Utils.isChestplate(item)) ||
                            (usageType == CustomEnchant.TYPE_LEGGINGS && Utils.isLeggings(item)) ||
                            (usageType == CustomEnchant.TYPE_BOOTS && Utils.isBoots(item))
                    ))
            ) {
                enchantment.onToggle(player, item, inventory, slot, enchantmentInstance.getLevel(), toggle);
            }
        }
    }
}