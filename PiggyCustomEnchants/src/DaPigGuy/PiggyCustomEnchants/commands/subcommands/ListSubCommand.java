package DaPigGuy.PiggyCustomEnchants.commands.subcommands.ListSubCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListSubCommand extends Command {

    private final PiggyCustomEnchants plugin;

    public ListSubCommand(PiggyCustomEnchants plugin) {
        super("list");
        this.plugin = plugin;
        setDescription("List custom enchantments");
        setUsage("/ce list");
        setPermission("piggycustomenchants.command.ce.list");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                sendTypesForm((Player) sender);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /ce list");
            }
            return true;
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }

    private void sendTypesForm(Player player) {

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Custom Enchants List");
        Map<Integer, ItemStack> items = new HashMap<>();
        int index = 0;
        for (int type : Utils.TYPE_NAMES.keySet()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + Utils.TYPE_NAMES.get(type));
                item.setItemMeta(meta);
            }
            items.put(index++, item);
        }

        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.RED + "Back");
            backButton.setItemMeta(backMeta);
        }

        items.put(index, backButton);
        items.forEach((i, stack) -> inv.setItem(i, stack));
        player.openInventory(inv);
        Bukkit.getPluginManager().registerEvents(new org.bukkit.event.Listener() {
            @org.bukkit.event.EventHandler
            public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
                if (event.getView().getTitle().equals(ChatColor.GREEN + "Custom Enchants List")) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (meta != null && meta.getDisplayName().equals(ChatColor.RED + "Back")) {
                            player.closeInventory();
                        } else {
                            int clickedIndex = event.getSlot();
                            if (clickedIndex < Utils.TYPE_NAMES.size()) {
                                int type = (int) Utils.TYPE_NAMES.keySet().toArray()[clickedIndex];
                                sendEnchantsForm(player, type);
                            }
                        }
                    }
                } else if (event.getView().getTitle().startsWith(ChatColor.GREEN + "Enchants")) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (meta != null && meta.getDisplayName().equals(ChatColor.RED + "Back")) {
                            sendTypesForm(player);
                        } else {
                            int clickedIndex = event.getSlot();
                            int type = getTypeFromInventoryTitle(event.getView().getTitle());
                            if (clickedIndex < CustomEnchantManager.getEnchantmentsByType(type).size()) {
                                CustomEnchant enchantment = CustomEnchantManager.getEnchantmentsByType(type).get(clickedIndex);
                                showEnchantmentInfo(player, enchantment);
                            }
                        }
                    }
                }
            }
        }, plugin);
    }

    private void sendEnchantsForm(Player player, int type) {

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + Utils.TYPE_NAMES.get(type) + " Enchants");
        List<CustomEnchant> enchantments = CustomEnchantManager.getEnchantmentsByType(type);
        int index = 0;
        for (CustomEnchant enchantment : enchantments) {
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + enchantment.getDisplayName());
                item.setItemMeta(meta);
            }
            inv.setItem(index++, item);
        }

        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.RED + "Back");
            backButton.setItemMeta(backMeta);
        }

        inv.setItem(index, backButton);
        player.openInventory(inv);
    }

    private void showEnchantmentInfo(Player player, CustomEnchant enchantment) {

        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.GREEN + enchantment.getDisplayName() + " Enchantment");
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + enchantment.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RESET + "ID: " + enchantment.getId());
            lore.add(ChatColor.RESET + "Description: " + enchantment.getDescription());
            lore.add(ChatColor.RESET + "Type: " + Utils.TYPE_NAMES.get(enchantment.getItemType()));
            lore.add(ChatColor.RESET + "Rarity: " + Utils.RARITY_NAMES.get(enchantment.getRarity()));
            lore.add(ChatColor.RESET + "Max Level: " + enchantment.getMaxLevel());
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        inv.setItem(13, item);
        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName(ChatColor.RED + "Back");
            backButton.setItemMeta(backMeta);
        }

        inv.setItem(26, backButton);
        player.openInventory(inv);
    }

    private int getTypeFromInventoryTitle(String title) {
        for (Map.Entry<Integer, String> entry : Utils.TYPE_NAMES.entrySet()) {
            if (title.startsWith(ChatColor.GREEN + entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }
}