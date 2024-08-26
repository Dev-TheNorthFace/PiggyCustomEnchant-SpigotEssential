package DaPigGuy.PiggyCustomEnchants.utils.Utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.CompassMeta;
import java.util.*;

public class Utils {

    private static final Map<String, String> RARITY_NAMES = Map.of(
            "common", "Common",
            "uncommon", "Uncommon",
            "rare", "Rare",
            "mythic", "Mythic"
    );

    private static final Map<Integer, List<Integer>> INCOMPATIBLE_ENCHANTS = Map.of(
            CustomEnchantIds.BLAZE, Arrays.asList(CustomEnchantIds.PORKIFIED, CustomEnchantIds.WITHERSKULL),
            CustomEnchantIds.GRAPPLING, Collections.singletonList(CustomEnchantIds.VOLLEY),
            CustomEnchantIds.GROW, Collections.singletonList(CustomEnchantIds.SHRINK),
            CustomEnchantIds.HOMING, Arrays.asList(CustomEnchantIds.BLAZE, CustomEnchantIds.PORKIFIED, CustomEnchantIds.WITHERSKULL),
            CustomEnchantIds.PORKIFIED, Collections.singletonList(CustomEnchantIds.WITHERSKULL)
    );

    private static final Map<Player, Long> shouldTakeFallDamage = new HashMap<>();

    public static String getRomanNumeral(int integer) {
        final Map<Integer, String> romanNumeralConversionTable = Map.of(
                1000, "M",
                900, "CM",
                500, "D",
                400, "CD",
                100, "C",
                90, "XC",
                50, "L",
                40, "XL",
                10, "X",
                9, "IX",
                5, "V",
                4, "IV",
                1, "I"
        );
        StringBuilder romanString = new StringBuilder();
        while (integer > 0) {
            for (Map.Entry<Integer, String> entry : romanNumeralConversionTable.entrySet()) {
                int value = entry.getKey();
                String roman = entry.getValue();
                if (integer >= value) {
                    integer -= value;
                    romanString.append(roman);
                    break;
                }
            }
        }
        return romanString.toString();
    }

    public static boolean isHelmet(ItemStack item) {
        return item.getType() == Material.LEATHER_HELMET || item.getType() == Material.DIAMOND_HELMET ||
                item.getType() == Material.IRON_HELMET || item.getType() == Material.GOLD_HELMET ||
                item.getType() == Material.NETHERITE_HELMET;
    }

    public static boolean isChestplate(ItemStack item) {
        return item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.DIAMOND_CHESTPLATE ||
                item.getType() == Material.IRON_CHESTPLATE || item.getType() == Material.GOLD_CHESTPLATE ||
                item.getType() == Material.NETHERITE_CHESTPLATE;
    }

    public static boolean isLeggings(ItemStack item) {
        return item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.DIAMOND_LEGGINGS ||
                item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.GOLD_LEGGINGS ||
                item.getType() == Material.NETHERITE_LEGGINGS;
    }

    public static boolean isBoots(ItemStack item) {
        return item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.DIAMOND_BOOTS ||
                item.getType() == Material.IRON_BOOTS || item.getType() == Material.GOLD_BOOTS ||
                item.getType() == Material.NETHERITE_BOOTS;
    }

    public static boolean itemMatchesItemType(ItemStack item, int itemType) {
        if (item.getType() == Material.BOOK || item.getType() == Material.ENCHANTED_BOOK) return true;

        switch (itemType) {
            case CustomEnchant.ITEM_TYPE_GLOBAL:
                return true;
            case CustomEnchant.ITEM_TYPE_DAMAGEABLE:
                return item instanceof Damageable;
            case CustomEnchant.ITEM_TYPE_WEAPON:
                return item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.IRON_SWORD ||
                        item.getType() == Material.GOLDEN_SWORD || item.getType() == Material.STONE_SWORD ||
                        item.getType() == Material.WOODEN_SWORD || item.getType() == Material.BOW;
            case CustomEnchant.ITEM_TYPE_SWORD:
                return item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.IRON_SWORD ||
                        item.getType() == Material.GOLDEN_SWORD || item.getType() == Material.STONE_SWORD ||
                        item.getType() == Material.WOODEN_SWORD;
            case CustomEnchant.ITEM_TYPE_BOW:
                return item.getType() == Material.BOW;
            case CustomEnchant.ITEM_TYPE_TOOLS:
                return item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.IRON_PICKAXE ||
                        item.getType() == Material.GOLDEN_PICKAXE || item.getType() == Material.STONE_PICKAXE ||
                        item.getType() == Material.WOODEN_PICKAXE || item.getType() == Material.DIAMOND_AXE ||
                        item.getType() == Material.IRON_AXE || item.getType() == Material.GOLDEN_AXE ||
                        item.getType() == Material.STONE_AXE || item.getType() == Material.WOODEN_AXE ||
                        item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.IRON_SHOVEL ||
                        item.getType() == Material.GOLDEN_SHOVEL || item.getType() == Material.STONE_SHOVEL ||
                        item.getType() == Material.WOODEN_SHOVEL || item.getType() == Material.DIAMOND_HOE ||
                        item.getType() == Material.IRON_HOE || item.getType() == Material.GOLDEN_HOE ||
                        item.getType() == Material.STONE_HOE || item.getType() == Material.WOODEN_HOE ||
                        item.getType() == Material.SHEARS;
            case CustomEnchant.ITEM_TYPE_PICKAXE:
                return item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.IRON_PICKAXE ||
                        item.getType() == Material.GOLDEN_PICKAXE || item.getType() == Material.STONE_PICKAXE ||
                        item.getType() == Material.WOODEN_PICKAXE;
            case CustomEnchant.ITEM_TYPE_AXE:
                return item.getType() == Material.DIAMOND_AXE || item.getType() == Material.IRON_AXE ||
                        item.getType() == Material.GOLDEN_AXE || item.getType() == Material.STONE_AXE ||
                        item.getType() == Material.WOODEN_AXE;
            case CustomEnchant.ITEM_TYPE_SHOVEL:
                return item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.IRON_SHOVEL ||
                        item.getType() == Material.GOLDEN_SHOVEL || item.getType() == Material.STONE_SHOVEL ||
                        item.getType() == Material.WOODEN_SHOVEL;
            case CustomEnchant.ITEM_TYPE_HOE:
                return item.getType() == Material.DIAMOND_HOE || item.getType() == Material.IRON_HOE ||
                        item.getType() == Material.GOLDEN_HOE || item.getType() == Material.STONE_HOE ||
                        item.getType() == Material.WOODEN_HOE;
            case CustomEnchant.ITEM_TYPE_ARMOR:
                return item.getType() == Material.LEATHER_HELMET || item.getType() == Material.DIAMOND_HELMET ||
                        item.getType() == Material.IRON_HELMET || item.getType() == Material.GOLD_HELMET ||
                        item.getType() == Material.NETHERITE_HELMET || item.getType() == Material.LEATHER_CHESTPLATE ||
                        item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.IRON_CHESTPLATE ||
                        item.getType() == Material.GOLD_CHESTPLATE || item.getType() == Material.NETHERITE_CHESTPLATE ||
                        item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.DIAMOND_LEGGINGS ||
                        item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.GOLD_LEGGINGS ||
                        item.getType() == Material.NETHERITE_LEGGINGS || item.getType() == Material.LEATHER_BOOTS ||
                        item.getType() == Material.DIAMOND_BOOTS || item.getType() == Material.IRON_BOOTS ||
                        item.getType() == Material.GOLD_BOOTS || item.getType() == Material.NETHERITE_BOOTS;
            case CustomEnchant.ITEM_TYPE_HELMET:
                return isHelmet(item);
            case CustomEnchant.ITEM_TYPE_CHESTPLATE:
                return isChestplate(item);
            case CustomEnchant.ITEM_TYPE_LEGGINGS:
                return isLeggings(item);
            case CustomEnchant.ITEM_TYPE_BOOTS:
                return isBoots(item);
            case CustomEnchant.ITEM_TYPE_COMPASS:
                return item.getType() == Material.COMPASS;
            default:
                return false;
        }
    }

    public static Projectile createNewProjectile(Class<? extends Projectile> className, Location location, Player shooter, Projectile previousProjectile, int level) {
        try {
            if (Arrow.class.equals(className)) {
                return new Arrow(location.getWorld(), shooter, previousProjectile instanceof Arrow && ((Arrow) previousProjectile).isCritical());
            } else if (HomingArrow.class.equals(className)) {
                return new HomingArrow(location.getWorld(), shooter, previousProjectile instanceof Arrow && ((Arrow) previousProjectile).isCritical(), level);
            } else if (PiggyFireball.class.equals(className) || PiggyWitherSkull.class.equals(className)) {
                return className.getConstructor(Location.class, Player.class).newInstance(location, shooter);
            } else if (PigProjectile.class.equals(className)) {
                return new PigProjectile(location.getWorld(), shooter, level);
            } else {
                throw new IllegalArgumentException("Entity " + className.getName() + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkEnchantIncompatibilities(ItemStack item, CustomEnchant enchant) {
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            Enchantment otherEnchant = entry.getKey();
            if (otherEnchant instanceof CustomEnchant) {
                if (INCOMPATIBLE_ENCHANTS.getOrDefault(((CustomEnchant) otherEnchant).getId(), Collections.emptyList()).contains(enchant.getId())) {
                    return false;
                }
                if (INCOMPATIBLE_ENCHANTS.getOrDefault(enchant.getId(), Collections.emptyList()).contains(((CustomEnchant) otherEnchant).getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ItemStack displayEnchants(ItemStack itemStack) {
        JavaPlugin plugin = CustomEnchantManager.getPlugin();
        FileConfiguration config = plugin.getConfig();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null && !itemStack.getEnchantments().isEmpty()) {
            String additionalInformation = config.getString("enchants.position").equals("name") ? itemStack.getType().name() : "";

            for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                Enchantment enchantment = entry.getKey();
                if (enchantment instanceof CustomEnchant) {
                    String rarityName = RARITY_NAMES.getOrDefault(((CustomEnchant) enchantment).getRarity().name().toLowerCase(), "Common");
                    String color = config.getString("rarity-colors." + rarityName.toLowerCase(), "GRAY");
                    additionalInformation += "\n" + ChatColor.valueOf(color.toUpperCase()) + enchantment.getKey().getKey() + " " +
                            (config.getBoolean("enchants.roman-numerals", true) ? getRomanNumeral(entry.getValue()) : entry.getValue());
                }
            }

            if (config.getString("enchants.position", "name").equals("lore")) {
                List<String> lore = new ArrayList<>(Arrays.asList(additionalInformation.split("\n")));
                meta.setLore(lore);
            } else {
                meta.setDisplayName(additionalInformation);
            }

            itemStack.setItemMeta(meta);
        }

        if (!plugin.getDescription().getName().equals("PiggyCustomEnchants") || !plugin.getDescription().getAuthors().contains("DaPigGuy, North")) {
            meta.setCustomModelData(1);
        }

        return itemStack;
    }

    public static ItemStack filterDisplayedEnchants(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null && !itemStack.getEnchantments().isEmpty()) {
            meta.setDisplayName(null);
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static List<Enchantment> sortEnchantmentsByPriority(List<Enchantment> enchantments) {
        enchantments.sort((enchantment1, enchantment2) -> {
            int priority1 = enchantment1 instanceof CustomEnchant ? ((CustomEnchant) enchantment1).getPriority() : 1;
            int priority2 = enchantment2 instanceof CustomEnchant ? ((CustomEnchant) enchantment2).getPriority() : 1;
            return Integer.compare(priority2, priority1);
        });
        return enchantments;
    }

    public static String getColorFromRarity(int rarity) {
        String rarityName = RARITY_NAMES.getOrDefault(rarity, "COMMON").toLowerCase();
        FileConfiguration config = CustomEnchantManager.getPlugin().getConfig();
        String color = config.getString("rarity-colors." + rarityName, "GRAY");
        return ChatColor.valueOf(color.toUpperCase()).toString();
    }

    public static void errorForm(Player player, String error) {
        player.sendMessage(ChatColor.RED + "Error: " + error);
    }

    public static boolean shouldTakeFallDamage(Player player) {
        return !shouldTakeFallDamage.containsKey(player.getName());
    }

    public static void setShouldTakeFallDamage(Player player, boolean shouldTakeFallDamage, int duration) {
        if (!shouldTakeFallDamage) {
            shouldTakeFallDamage.put(player.getName(), System.currentTimeMillis() + (duration * 1000L));
        } else {
            shouldTakeFallDamage.remove(player.getName());
        }
    }

    public static int getNoFallDamageDuration(Player player) {
        return (int) ((shouldTakeFallDamage.getOrDefault(player.getName(), System.currentTimeMillis()) - System.currentTimeMillis()) / 1000L);
    }

    public static void increaseNoFallDamageDuration(Player player, int duration) {
        shouldTakeFallDamage.put(player.getName(), shouldTakeFallDamage.getOrDefault(player.getName(), System.currentTimeMillis()) + (duration * 1000L));
    }

    public static boolean isCoolKid(PluginDescriptionFile description) {
        return description.getName().equals("PiggyCustomEnchants") && description.getAuthors().contains("DaPigGuy, North");
    }
}