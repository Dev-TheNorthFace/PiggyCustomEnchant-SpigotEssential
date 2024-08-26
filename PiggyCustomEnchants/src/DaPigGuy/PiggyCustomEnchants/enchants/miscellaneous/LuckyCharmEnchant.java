package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.LuckyCharmEnchant;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class LuckyCharmEnchant extends ToggleableEnchantment implements Listener {

    public LuckyCharmEnchant() {
        super("Lucky Charm", Rarity.MYTHIC, CustomEnchant.TYPE_INVENTORY, CustomEnchant.ITEM_TYPE_GLOBAL);
        this.maxLevel = 3;
    }

    @Override
    public Class<? extends Event> getReagent() {
        return null;
    }

    @Override
    public void toggle(Player player, ItemStack item, Inventory inventory, int slot, int level, boolean toggle) {
        double additionalMultiplier = (double) getExtraData().get("additionalMultiplier");
        for (CustomEnchant enchantment : CustomEnchantManager.getEnchantments()) {
            if (enchantment instanceof ReactiveEnchantment) {
                ReactiveEnchantment reactiveEnchantment = (ReactiveEnchantment) enchantment;
                if (reactiveEnchantment.canReact()) {
                    double currentMultiplier = reactiveEnchantment.getChanceMultiplier(player);
                    double newMultiplier = currentMultiplier + ((toggle ? 1 : -1) * level * additionalMultiplier);
                    reactiveEnchantment.setChanceMultiplier(player, newMultiplier);
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getPriority() {
        return 2;
    }
}