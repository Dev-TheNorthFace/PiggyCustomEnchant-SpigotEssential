package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.RecursiveEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashSet;
import java.util.Set;

public class RecursiveEnchant extends ReactiveEnchantment {

    private static final Set<String> isUsing = new HashSet<>();

    public RecursiveEnchant(String name, int usageType, int itemType) {
        super(name, usageType, itemType);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (isUsing.contains(player.getName())) return;
        isUsing.add(player.getName());
        safeReact(player, item, inventory, slot, event, level, stack);
        isUsing.remove(player.getName());
    }

    public void safeReact(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
    }
}