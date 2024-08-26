package DaPigGuy.PiggyCustomEnchants.enchants.armor.GrowEnchant;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.TextFormat;
import java.util.HashMap;
import java.util.Map;

public class GrowEnchant extends ToggleableEnchantment implements Listener {

    private final double baseScaleIncrease = 0.3;
    private final double scaleMultiplier = 0.0125;
    private final int powerDuration = 60 * 20;
    private final int cooldownDuration = 75;

    private final Map<String, Player> grew = new HashMap<>();
    private final Map<String, Integer> growPower = new HashMap<>();
    private final Map<String, Boolean> shiftCache = new HashMap<>();

    public GrowEnchant(int id, String name, JavaPlugin plugin) {
        super(id, name, CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, plugin);
    }

    @Override
    public void onEnable() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @Override
    public void onReaction(Player player, ItemStack item, Inventory inventory, Event event, int level, int stack) {
        if (event instanceof PlayerToggleSneakEvent) {
            PlayerToggleSneakEvent sneakEvent = (PlayerToggleSneakEvent) event;
            String playerName = player.getName();
            if (sneakEvent.isSneaking()) {
                if (!shiftCache.containsKey(playerName)) {
                    super.onReaction(player, item, inventory, event, level, stack);
                    if (grew.containsKey(playerName)) {
                        setCooldown(player, 0);
                    }
                    shiftCache.put(playerName, true);
                } else {
                    player.sendMessage(TextFormat.RED + "Grow is still in cooldown: " + getCooldown(player) + "s");
                }
            } else {
                shiftCache.remove(playerName);
            }
        }
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, Event event, int level, int stack) {
        if (event instanceof PlayerToggleSneakEvent) {
            PlayerToggleSneakEvent sneakEvent = (PlayerToggleSneakEvent) event;
            String playerName = player.getName();
            if (getArmorStack(player) == 4) {
                if (sneakEvent.isSneaking()) {
                    if (stack - level == 0) {
                        if (grew.containsKey(playerName)) {
                            grew.remove(playerName);
                            player.setScale(1);
                            player.sendMessage(TextFormat.RED + "You have shrunk back to normal size.");
                        } else {
                            grew.put(playerName, player);
                            growPower.putIfAbsent(playerName, powerDuration);
                            player.setScale(player.getScale() + baseScaleIncrease + (getStack(player) * scaleMultiplier));
                            player.sendMessage(TextFormat.GREEN + "You have grown. Sneak again to shrink back to normal size.");
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        String playerName = player.getName();
        if (grew.containsKey(playerName)) {
            int remainingPower = growPower.get(playerName);
            remainingPower--;
            growPower.put(playerName, remainingPower);
            player.sendMessage(TextFormat.GREEN + "Grow power remaining: " + remainingPower);
            if (getArmorStack(player) < 4 || remainingPower <= 0) {
                grew.remove(playerName);
                setCooldown(player, cooldownDuration);
                if (remainingPower <= 0) {
                    growPower.put(playerName, powerDuration);
                }
                player.setScale(1);
                player.sendMessage(TextFormat.RED + "You have shrunk back to normal size.");
            }
        }
    }
}