package DaPigGuy.PiggyCustomEnchants.enchants.armor.CloakingEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.TextFormat;

public class CloakingEnchant extends ReactiveEnchantment {

    public CloakingEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            int duration = getDefaultExtraData().get("durationMultiplier") * level;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, duration * 20, 0, false, false));
            player.sendMessage(TextFormat.DARK_GRAY + "You have become invisible!");
        }
    }

    @Override
    public void applyCooldown(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
            }
        }.runTaskLater(getPlugin(), 20 * 10);
    }
}