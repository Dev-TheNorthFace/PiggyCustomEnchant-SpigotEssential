package DaPigGuy.PiggyCustomEnchants.enchants.miscellaneous.SoulboundEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SoulboundEnchant extends ReactiveEnchantment {

    public SoulboundEnchant(String name, int rarity, int usageType, int itemType) {
        super(name, rarity, usageType, itemType);
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof PlayerDeathEvent) {
            PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
            ItemStack[] drops = deathEvent.getDrops().toArray(new ItemStack[0]);
            for (int i = 0; i < drops.length; i++) {
                if (drops[i].isSimilar(item)) {
                    drops[i] = new ItemStack(Material.AIR);
                    break;
                }
            }

            deathEvent.getDrops().clear();
            for (ItemStack drop : drops) {
                if (drop.getType() != Material.AIR) {
                    deathEvent.getDrops().add(drop);
                }
            }

            if (level > 1) {
                CustomEnchantManager.addEnchantment(item, CustomEnchantIds.SOULBOUND, level - 1);
            } else {
                CustomEnchantManager.removeEnchantment(item, CustomEnchantIds.SOULBOUND);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    inventory.setItem(slot, item);
                }
            }.runTaskLater(JavaPlugin.getProvidingPlugin(SoulboundEnchant.class), 1L);
        }
    }
}