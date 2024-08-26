package DaPigGuy.PiggyCustomEnchants.PiggyCustomEnchants;

import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PiggyCustomEnchants extends JavaPlugin {

    private static final PotionEffect SLOW_FALL = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false);
    private FileConfiguration enchantmentConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        enchantmentConfig = getConfig();
        CustomEnchantManager.init(this);
        getCommand("customenchants").setExecutor(new CustomEnchantsCommand(this));
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
        new BukkitRunnable() {
            @Override
            public void run() {
                CustomEnchantManager.tickEnchantments();
            }
        }.runTaskTimer(this, 1, 1);
    }

    @Override
    public void onDisable() {
        for (Player player : getServer().getOnlinePlayers()) {
            for (ItemStack item : player.getInventory().getContents()) {
                disableEnchantments(item);
            }
            for (ItemStack item : player.getInventory().getArmorContents()) {
                disableEnchantments(item);
            }
        }
    }

    private void disableEnchantments(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasEnchants()) {
            meta.getEnchants().forEach((enchant, level) -> {
                CustomEnchant enchantment = CustomEnchantManager.getEnchantment(enchant.getKey());
                if (enchantment instanceof ToggleableEnchantment) {
                    ToggleableEnchantment.attemptToggle(null, item, enchantment, null, 0, false);
                }
            });
            item.setItemMeta(meta);
        }
    }

    public Object getEnchantmentData(String enchant, String data, Object defaultValue) {
        return enchantmentConfig.getOrDefault(enchant + "." + data, defaultValue);
    }

    public void setEnchantmentData(String enchant, String data, Object value) {
        enchantmentConfig.set(enchant + "." + data, value);
        saveConfig();
    }

    public boolean areFormsEnabled() {
        return getConfig().getBoolean("forms.enabled", true);
    }
}