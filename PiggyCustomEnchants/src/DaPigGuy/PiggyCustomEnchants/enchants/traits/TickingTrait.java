package DaPigGuy.PiggyCustomEnchants.enchants.traits.TickingEnchantmentBase;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class TickingEnchantmentBase extends CustomEnchant {

    protected PiggyCustomEnchants plugin;

    public TickingEnchantmentBase(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
        this.plugin = plugin;
    }

    public boolean canTick() {
        return true;
    }

    public int getTickingInterval() {
        return 1;
    }

    public void onTick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
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

        tick(player, item, inventory, slot, level);
    }

    public abstract void tick(Player player, ItemStack item, Inventory inventory, int slot, int level);

    public boolean supportsMultipleItems() {
        return false;
    }
}