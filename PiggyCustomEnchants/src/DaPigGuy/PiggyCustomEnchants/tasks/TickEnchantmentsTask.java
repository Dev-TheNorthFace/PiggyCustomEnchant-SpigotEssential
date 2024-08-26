package DaPigGuy.PiggyCustomEnchants.tasks.TickEnchantmentsTask;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;
import java.util.HashSet;
import java.util.Set;

public class TickEnchantmentsTask extends BukkitRunnable {

    private final PiggyCustomEnchants plugin;

    public TickEnchantmentsTask(PiggyCustomEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        long currentTick = Bukkit.getServer().getCurrentTick();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Set<CustomEnchant> successfulEnchantments = new HashSet<>();
            checkInventory(player.getInventory(), successfulEnchantments, currentTick);
            checkInventory(player.getInventory().getArmorContents(), successfulEnchantments, currentTick);
        }
    }

    private void checkInventory(ItemStack[] items, Set<CustomEnchant> successfulEnchantments, long currentTick) {
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item != null && item.getType().isItem()) {
                if (item.getType().equals(org.bukkit.Material.BOOK)) {
                    if (item.getEnchantments().size() > 0) {
                        ItemStack enchantedBook = new ItemStack(org.bukkit.Material.ENCHANTED_BOOK);
                        enchantedBook.setCustomModelData(item.getCustomModelData());
                        enchantedBook.addEnchantments(item.getEnchantments());
                        items[i] = enchantedBook;
                        continue;
                    }
                }
                if (!item.hasCustomModelData() && item.getEnchantments().size() > 0) {
                    items[i] = cleanOldItems(item);
                }
                for (Enchantment enchantment : item.getEnchantments().keySet()) {
                    CustomEnchant customEnchant = CustomEnchantManager.getEnchant(enchantment);
                    if (customEnchant instanceof TickingEnchantment) {
                        if (customEnchant.canTick() && (!successfulEnchantments.contains(customEnchant) || customEnchant.supportsMultipleItems())) {
                            if (shouldTick(customEnchant, item, i)) {
                                if (currentTick % customEnchant.getTickingInterval() == 0) {
                                    ((TickingEnchantment) customEnchant).onTick((Player) null, item, null, i, item.getEnchantments().get(enchantment));
                                    successfulEnchantments.add(customEnchant);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean shouldTick(CustomEnchant enchantment, ItemStack item, int slot) {
        if (enchantment.getUsageType() == CustomEnchant.TYPE_ANY_INVENTORY) {
            return true;
        }
        if (enchantment.getUsageType() == CustomEnchant.TYPE_INVENTORY) {
            return item.getType() != org.bukkit.Material.AIR;
        }
        if (enchantment.getUsageType() == CustomEnchant.TYPE_HAND) {
            return slot == ((PlayerInventory) null).getHeldItemSlot();
        }
        return false;
    }

    private ItemStack cleanOldItems(ItemStack item) {
        for (Enchantment enchantment : item.getEnchantments().keySet()) {
            CustomEnchant customEnchant = CustomEnchantManager.getEnchant(enchantment);
            if (customEnchant instanceof CustomEnchant) {
                String enchantmentName = customEnchant.getDisplayName();
                String enchantmentLevel = Utils.getRomanNumeral(item.getEnchantments().get(enchantment));
                String enchantmentText = Utils.getColorFromRarity(customEnchant.getRarity()) + enchantmentName + " " + enchantmentLevel;

                String itemName = item.getCustomModelData() != null ? item.getCustomModelData().toString() : "";
                itemName = itemName.replace("\n" + enchantmentText, "");
                item.setCustomModelData(Integer.parseInt(itemName));

                String lore = String.join("\n", item.getLore());
                lore = lore.replace(enchantmentText, "");
                item.setLore(lore);

                item.setCustomModelData(0);
            }
        }
        return item;
    }
}