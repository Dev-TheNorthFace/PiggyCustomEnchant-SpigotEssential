package DaPigGuy.PiggyCustomEnchants.enchants.CustomEnchant;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CustomEnchant {

    private final PiggyCustomEnchants plugin;
    private final int id;
    private String name;
    private int rarity;
    private int maxLevel;
    private String displayName;
    private String description;
    private Map<String, Object> extraData;
    private int cooldownDuration;
    private int chance;

    private final Map<String, Long> cooldown = new HashMap<>();

    public static final int TYPE_HAND = 0;
    public static final int TYPE_ANY_INVENTORY = 1;
    public static final int TYPE_INVENTORY = 2;
    public static final int TYPE_ARMOR_INVENTORY = 3;
    public static final int TYPE_HELMET = 4;
    public static final int TYPE_CHESTPLATE = 5;
    public static final int TYPE_LEGGINGS = 6;
    public static final int TYPE_BOOTS = 7;

    public static final int ITEM_TYPE_GLOBAL = 0;
    public static final int ITEM_TYPE_DAMAGEABLE = 1;
    public static final int ITEM_TYPE_WEAPON = 2;
    public static final int ITEM_TYPE_SWORD = 3;
    public static final int ITEM_TYPE_BOW = 4;
    public static final int ITEM_TYPE_TOOLS = 5;
    public static final int ITEM_TYPE_PICKAXE = 6;
    public static final int ITEM_TYPE_AXE = 7;
    public static final int ITEM_TYPE_SHOVEL = 8;
    public static final int ITEM_TYPE_HOE = 9;
    public static final int ITEM_TYPE_ARMOR = 10;
    public static final int ITEM_TYPE_HELMET = 11;
    public static final int ITEM_TYPE_CHESTPLATE = 12;
    public static final int ITEM_TYPE_LEGGINGS = 13;
    public static final int ITEM_TYPE_BOOTS = 14;
    public static final int ITEM_TYPE_COMPASS = 15;

    public CustomEnchant(PiggyCustomEnchants plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.name = plugin.getEnchantmentData("name", "defaultName");
        this.rarity = Utils.RARITY_NAMES.getOrDefault(plugin.getEnchantmentData(this.name, "rarities", "rare"), 1);
        this.maxLevel = Integer.parseInt(plugin.getEnchantmentData(this.name, "max_levels", "5"));
        this.displayName = plugin.getEnchantmentData(this.name, "display_names", this.name);
        this.description = plugin.getEnchantmentData(this.name, "descriptions", "");
        this.extraData = (Map<String, Object>) plugin.getEnchantmentData(this.name, "extra_data", new HashMap<>());
        this.cooldownDuration = Integer.parseInt(plugin.getEnchantmentData(this.name, "cooldowns", "0"));
        this.chance = Integer.parseInt(plugin.getEnchantmentData(this.name, "chances", "100"));
        Map<String, Object> defaultExtraData = getDefaultExtraData();
        for (Map.Entry<String, Object> entry : defaultExtraData.entrySet()) {
            this.extraData.putIfAbsent(entry.getKey(), entry.getValue());
        }
        plugin.setEnchantmentData(this.name, "extra_data", this.extraData);

        if (!Utils.isCoolKid(plugin.getDescription())) {
            try {
                Method valuesMethod = CustomEnchantIds.class.getMethod("values");
                Object[] values = (Object[]) valuesMethod.invoke(null);
                this.id = (int) values[(int) (Math.random() * values.length)];
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public Map<String, Object> getDefaultExtraData() {
        return new HashMap<>();
    }

    public int getUsageType() {
        return TYPE_HAND;
    }

    public int getItemType() {
        return ITEM_TYPE_WEAPON;
    }

    public int getPriority() {
        return 1;
    }

    public boolean canReact() {
        return false;
    }

    public boolean canTick() {
        return false;
    }

    public boolean canToggle() {
        return false;
    }

    public int getCooldown(Player player) {
        return (int) (cooldown.getOrDefault(player.getName(), 0L) - System.currentTimeMillis()) / 1000;
    }

    public void setCooldown(Player player, int cooldown) {
        this.cooldown.put(player.getName(), System.currentTimeMillis() + cooldown * 1000);
    }
}