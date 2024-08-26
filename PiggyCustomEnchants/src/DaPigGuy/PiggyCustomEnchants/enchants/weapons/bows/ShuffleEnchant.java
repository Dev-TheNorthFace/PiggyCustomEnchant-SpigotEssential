package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.ShuffleEnchant;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatColor;

public class ShuffleEnchant extends ReactiveEnchantment {

    public ShuffleEnchant(PiggyCustomEnchants plugin, int id, String name, int maxLevel, int rarity) {
        super(plugin, id, name, maxLevel, rarity);
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            if (damageEvent.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) damageEvent.getEntity();
                Location playerLocation = player.getLocation().clone();
                Location entityLocation = entity.getLocation().clone();
                player.teleport(entityLocation);
                entity.teleport(playerLocation);
                String name = entity.getCustomName() != null ? entity.getCustomName() : entity.getName();
                if (entity instanceof Player) {
                    Player targetPlayer = (Player) entity;
                    name = targetPlayer.getDisplayName();
                    targetPlayer.sendMessage(ChatColor.DARK_PURPLE + "You have switched positions with " + player.getDisplayName());
                }
                player.sendMessage(ChatColor.DARK_PURPLE + "You have switched positions with " + name);
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getPriority() {
        return 2;
    }
}