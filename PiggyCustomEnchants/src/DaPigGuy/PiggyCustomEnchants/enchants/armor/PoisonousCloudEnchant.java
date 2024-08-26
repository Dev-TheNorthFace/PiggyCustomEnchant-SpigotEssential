package DaPigGuy.PiggyCustomEnchants.enchants.armor.PoisonousCloudEnchant;

import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Effect;
import org.bukkit.potion.EffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.world.World;
import org.bukkit.world.effect.Particle;
import java.util.List;

public class PoisonousCloudEnchant extends TickableEnchantment {

    public PoisonousCloudEnchant(int id, String name, CustomEnchant.Rarity rarity, int maxLevel) {
        super(id, name, rarity, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, maxLevel);
    }

    @Override
    public void tick(Player player, ItemStack item, Inventory inventory, int slot, int level) {
        int radius = level * 3;
        World world = player.getWorld();
        List<Entity> nearbyEntities = world.getEntities();
        
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != player && !AllyChecks.isAlly(player, (LivingEntity) entity)) {
                if (entity.getLocation().distance(player.getLocation()) <= radius) {
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, level * 100, level - 1, false, false));
                }
            }
        }

        if (world.getTickCount() % 20 == 0) {
            for (double x = -radius; x <= radius; x += 0.25) {
                for (double y = -radius; y <= radius; y += 0.25) {
                    for (double z = -radius; z <= radius; z += 0.25) {
                        if (Math.random() < 1.0 / (800 * level)) {
                            Vector position = player.getLocation().toVector().add(new Vector(x, y, z));
                            world.spawnParticle(Particle.REDSTONE, position.getX(), position.getY(), position.getZ(), 1, new Particle.DustOptions(Color.fromRGB(34, 139, 34), 1));
                        }
                    }
                }
            }
        }
    }
}