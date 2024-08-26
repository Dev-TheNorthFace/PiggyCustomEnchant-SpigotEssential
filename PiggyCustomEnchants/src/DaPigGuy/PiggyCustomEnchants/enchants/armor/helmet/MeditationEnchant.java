package DaPigGuy.PiggyCustomEnchants.enchants.armor.helmet.MeditationEnchant;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;

public class MeditationEnchant extends ReactiveEnchantment implements TickingTrait {

    public MeditationEnchant(int id) {
        super(id, "Meditation", Rarity.UNCOMMON, CustomEnchant.TYPE_HELMET, CustomEnchant.ITEM_TYPE_HELMET);
    }

    private final Map<String, Player> meditating = new HashMap<>();
    private final Map<String, Integer> meditationTick = new HashMap<>();

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{PlayerMoveEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
            meditating.put(player.getName(), player);
            meditationTick.put(player.getName(), 0);
        }
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        if (meditationTick.containsKey(player.getName())) {
            int tickCount = meditationTick.get(player.getName());
            tickCount++;
            meditationTick.put(player.getName(), tickCount);
            int time = tickCount / 40;
            int maxDurationTicks = getDefaultExtraData().getDuration() / 40;
            player.sendTitle("", ChatColor.DARK_GREEN + "Meditating...\n" + ChatColor.GREEN + repeat("▌", time) + ChatColor.GRAY + repeat("▌", maxDurationTicks - time), 0, 5, 0);
            if (tickCount >= getDefaultExtraData().getDuration()) {
                meditationTick.put(player.getName(), 0);
                EntityRegainHealthEvent regainHealthEvent = new EntityRegainHealthEvent(player, level * getDefaultExtraData().getHealthReplenishAmountMultiplier(), EntityRegainHealthEvent.RegainReason.MAGIC);
                player.getServer().getPluginManager().callEvent(regainHealthEvent);
                if (!regainHealthEvent.isCancelled()) {
                    player.setHealth(Math.min(player.getHealth() + regainHealthEvent.getAmount(), player.getMaxHealth()));
                }

                int currentFoodLevel = player.getFoodLevel();
                int maxFoodLevel = 20;
                int newFoodLevel = currentFoodLevel + level * getDefaultExtraData().getFoodReplenishAmountMultiplier();
                player.setFoodLevel(Math.min(newFoodLevel, maxFoodLevel));
            }
        }
    }

    private String repeat(String str, int count) {
        return new String(new char[count]).replace("\0", str);
    }

    public DefaultExtraData getDefaultExtraData() {
        return new DefaultExtraData(20 * 20, 1, 1);
    }

    public static class DefaultExtraData {
        private final int duration;
        private final int healthReplenishAmountMultiplier;
        private final int foodReplenishAmountMultiplier;

        public DefaultExtraData(int duration, int healthReplenishAmountMultiplier, int foodReplenishAmountMultiplier) {
            this.duration = duration;
            this.healthReplenishAmountMultiplier = healthReplenishAmountMultiplier;
            this.foodReplenishAmountMultiplier = foodReplenishAmountMultiplier;
        }

        public int getDuration() {
            return duration;
        }

        public int getHealthReplenishAmountMultiplier() {
            return healthReplenishAmountMultiplier;
        }

        public int getFoodReplenishAmountMultiplier() {
            return foodReplenishAmountMultiplier;
        }
    }
}