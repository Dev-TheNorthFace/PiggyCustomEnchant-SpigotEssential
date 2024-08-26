package DaPigGuy.PiggyCustomEnchants.enchants.armor.chestplate.ProwlEnchant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;

public class ProwlEnchant extends ToggleableEnchantment implements TickingTrait {

    private Map<String, Boolean> prowled = new HashMap<>();

    public ProwlEnchant(int id) {
        super(id, "Prowl", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE);
    }

    @Override
    public void toggle(Player player, ItemStack item, int slot, int level, boolean toggle) {
        if (!toggle && prowled.containsKey(player.getName())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showPlayer(player);
            }
            player.removePotionEffect(PotionEffectType.SLOW);
            if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                player.setInvisible(false);
            }
            prowled.remove(player.getName());
        }
    }

    @Override
    public void tick(Player player, ItemStack item, int slot, int level) {
        if (player.isSneaking()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hidePlayer(player);
            }
            PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false);
            player.addPotionEffect(slowness);
            player.setInvisible(true);
            prowled.put(player.getName(), true);
        } else {
            if (prowled.containsKey(player.getName())) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(player);
                }
                player.removePotionEffect(PotionEffectType.SLOW);
                if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    player.setInvisible(false);
                }
                prowled.remove(player.getName());
            }
        }
    }
}