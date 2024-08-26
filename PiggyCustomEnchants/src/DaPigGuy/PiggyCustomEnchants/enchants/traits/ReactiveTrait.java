package DaPigGuy.PiggyCustomEnchants.enchants.traits.ReactiveEnchantmentBase;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByChildEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ReactiveEnchantmentBase extends ReactiveEnchantment {

    protected PiggyCustomEnchants plugin;
    protected Map<UUID, Float> chanceMultiplierMap = new HashMap<>();

    public ReactiveEnchantmentBase(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
        this.plugin = plugin;
    }

    public boolean canReact() {
        return true;
    }

    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class};
    }

    public void onReaction(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            if (((EntityDamageByEntityEvent) event).getEntity() == player) {
                if (!((EntityDamageByEntityEvent) event).getDamager().equals(player) && !shouldReactToDamaged()) return;
            } else if (!shouldReactToDamage()) return;
        }
        if (Math.random() <= getChance(player, level)) {
            react(player, item, inventory, slot, event, level, stack);
            setCooldown(player, getCooldownDuration());
        }
    }

    public abstract void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack);

    public double getChance(Player player, int level) {
        return getBaseChance(level) * getChanceMultiplier(player);
    }

    public double getBaseChance(int level) {
        return this.getChance() * level;
    }

    public double getChanceMultiplier(Player player) {
        return chanceMultiplierMap.getOrDefault(player.getUniqueId(), 1.0f);
    }

    public void setChanceMultiplier(Player player, float multiplier) {
        chanceMultiplierMap.put(player.getUniqueId(), multiplier);
    }

    public int getCooldownDuration() {
        return this.cooldownDuration;
    }

    public boolean shouldReactToDamage() {
        return getItemType() == CustomEnchant.ITEM_TYPE_WEAPON || getItemType() == CustomEnchant.ITEM_TYPE_BOW;
    }

    public boolean shouldReactToDamaged() {
        return getUsageType() == CustomEnchant.TYPE_ARMOR_INVENTORY;
    }

    public static void attemptReaction(Player player, Event event) {
        if (player.getInventory() == null) return;

        if (event instanceof EntityDamageByChildEntityEvent || event instanceof ProjectileHitEvent) {
            Projectile projectile = event instanceof EntityDamageByEntityEvent
                    ? (Projectile) ((EntityDamageByEntityEvent) event).getDamager()
                    : ((ProjectileHitEvent) event).getEntity();
            
            if (projectile != null && ProjectileTracker.isTrackedProjectile(projectile)) {
                if (!(event instanceof EntityDamageByEntityEvent) || ((EntityDamageByEntityEvent) event).getDamager().equals(player)) {
                    for (ReactiveEnchantment enchantment : Utils.sortEnchantmentsByPriority(ProjectileTracker.getEnchantments(projectile))) {
                        if (enchantment instanceof CustomEnchant && enchantment.canReact()) {
                            if (enchantment.getUsageType() == CustomEnchant.TYPE_INVENTORY || enchantment.getUsageType() == CustomEnchant.TYPE_ANY_INVENTORY || enchantment.getUsageType() == CustomEnchant.TYPE_HAND) {
                                for (Class<?> reagent : enchantment.getReagent()) {
                                    if (reagent.isInstance(event)) {
                                        ItemStack item = ProjectileTracker.getItem(projectile);
                                        int slot = 0;
                                        for (ItemStack content : player.getInventory().getContents()) {
                                            if (content.equals(item)) break;
                                            slot++;
                                        }
                                        enchantment.onReaction(player, item, player.getInventory(), slot, event, enchantment.getLevel(), 1);
                                    }
                                }
                            }
                        }
                    }
                    ProjectileTracker.removeProjectile(projectile);
                    return;
                }
            }
        }

        Map<Integer, Integer> enchantmentStacks = new HashMap<>();
        for (int slot = 0; slot < player.getInventory().getContents().length; slot++) {
            ItemStack content = player.getInventory().getContents()[slot];
            for (ReactiveEnchantment enchantment : Utils.sortEnchantmentsByPriority(content.getEnchantments())) {
                if (enchantment instanceof CustomEnchant && enchantment.canReact()) {
                    if (enchantment.getUsageType() == CustomEnchant.TYPE_INVENTORY || enchantment.getUsageType() == CustomEnchant.TYPE_ANY_INVENTORY || (enchantment.getUsageType() == CustomEnchant.TYPE_HAND && player.getInventory().getHeldItemSlot() == slot)) {
                        for (Class<?> reagent : enchantment.getReagent()) {
                            if (reagent.isInstance(event)) {
                                enchantmentStacks.put(enchantment.getId(), enchantmentStacks.getOrDefault(enchantment.getId(), 0) + enchantment.getLevel());
                                enchantment.onReaction(player, content, player.getInventory(), slot, event, enchantment.getLevel(), enchantmentStacks.get(enchantment.getId()));
                            }
                        }
                    }
                }
            }
        }

        for (int slot = 0; slot < player.getInventory().getArmorContents().length; slot++) {
            ItemStack content = player.getInventory().getArmorContents()[slot];
            for (ReactiveEnchantment enchantment : Utils.sortEnchantmentsByPriority(content.getEnchantments())) {
                if (enchantment instanceof CustomEnchant && enchantment.canReact()) {
                    if (enchantment.getUsageType() == CustomEnchant.TYPE_ANY_INVENTORY || enchantment.getUsageType() == CustomEnchant.TYPE_ARMOR_INVENTORY) {
                        for (Class<?> reagent : enchantment.getReagent()) {
                            if (reagent.isInstance(event)) {
                                enchantmentStacks.put(enchantment.getId(), enchantmentStacks.getOrDefault(enchantment.getId(), 0) + enchantment.getLevel());
                                enchantment.onReaction(player, content, player.getInventory(), slot, event, enchantment.getLevel(), enchantmentStacks.get(enchantment.getId()));
                            }
                        }
                    }
                }
            }
        }
    }
}