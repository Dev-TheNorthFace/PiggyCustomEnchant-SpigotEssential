package DaPigGuy.PiggyCustomEnchants.enchants.armor.helmet.ImplantsEnchant;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;

public class ImplantsEnchant extends ReactiveEnchantment {

    public ImplantsEnchant(int id) {
        super(id, "Implants", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_HELMET, CustomEnchant.ITEM_TYPE_HELMET);
    }

    private static final Map<String, BukkitRunnable> tasks = new HashMap<>();

    @Override
    public Class<? extends Event>[] getReagent() {
        return new Class[]{PlayerMoveEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof PlayerMoveEvent) {
            PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
            int currentFoodLevel = player.getFoodLevel();
            int maxFoodLevel = 20;
            if (currentFoodLevel < maxFoodLevel) {
                int newFoodLevel = currentFoodLevel + level * getDefaultExtraData().getFoodReplenishAmountMultiplier();
                player.setFoodLevel(Math.min(newFoodLevel, maxFoodLevel));
            }

            int currentAirTicks = player.getRemainingAir();
            int maxAirTicks = player.getMaximumAir();
            if (currentAirTicks < maxAirTicks && !tasks.containsKey(player.getName())) {
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline() && !player.isDead() && hasImplantsEnchantment(player)) {
                            Block blockAbove = player.getLocation().add(0, 1, 0).getBlock();
                            if (blockAbove.getType() != Material.WATER || player.getRemainingAir() >= maxAirTicks) {
                                cancelTask(player.getName());
                                return;
                            }
                            int newAirTicks = player.getRemainingAir() + (level * getDefaultExtraData().getAirTicksReplenishAmountMultiplier());
                            player.setRemainingAir(Math.min(newAirTicks, maxAirTicks));
                        } else {
                            cancelTask(player.getName());
                        }
                    }
                };
                task.runTaskTimer(plugin, 20L, getDefaultExtraData().getAirReplenishInterval());
                tasks.put(player.getName(), task);
            }
            setCooldown(player, 1);
        }
    }

    private boolean hasImplantsEnchantment(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet == null) return false;
        CustomEnchant enchantment = CustomEnchantManager.getEnchantment(CustomEnchantIds.IMPLANTS);
        return helmet.containsEnchantment(enchantment);
    }

    private void cancelTask(String playerName) {
        BukkitRunnable task = tasks.remove(playerName);
        if (task != null) {
            task.cancel();
        }
    }

    public DefaultExtraData getDefaultExtraData() {
        return new DefaultExtraData(1, 40, 60);
    }

    public static class DefaultExtraData {
        private final int foodReplenishAmountMultiplier;
        private final int airTicksReplenishAmountMultiplier;
        private final int airReplenishInterval;

        public DefaultExtraData(int foodReplenishAmountMultiplier, int airTicksReplenishAmountMultiplier, int airReplenishInterval) {
            this.foodReplenishAmountMultiplier = foodReplenishAmountMultiplier;
            this.airTicksReplenishAmountMultiplier = airTicksReplenishAmountMultiplier;
            this.airReplenishInterval = airReplenishInterval;
        }

        public int getFoodReplenishAmountMultiplier() {
            return foodReplenishAmountMultiplier;
        }

        public int getAirTicksReplenishAmountMultiplier() {
            return airTicksReplenishAmountMultiplier;
        }

        public int getAirReplenishInterval() {
            return airReplenishInterval;
        }
    }
}