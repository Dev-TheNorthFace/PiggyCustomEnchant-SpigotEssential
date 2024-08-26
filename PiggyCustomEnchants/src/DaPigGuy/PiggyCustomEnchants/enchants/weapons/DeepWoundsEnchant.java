package DaPigGuy.PiggyCustomEnchants.enchants.weapons.DeepWoundsEnchant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.particle.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import java.util.HashMap;
import java.util.Map;

public class DeepWoundsEnchant extends ReactiveEnchantment {

    private static final Map<Integer, BukkitTask> tasks = new HashMap<>();
    private final int cooldownDuration = 7;

    public DeepWoundsEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Deep Wounds";
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity entity = damageEvent.getEntity();
            if (entity instanceof LivingEntity && !tasks.containsKey(entity.getEntityId())) {
                LivingEntity livingEntity = (LivingEntity) entity;
                int durationMultiplier = getDefaultExtraData().get("durationMultiplier");
                double base = getDefaultExtraData().get("base");
                double multiplier = getDefaultExtraData().get("multiplier");
                double endTime = System.currentTimeMillis() + durationMultiplier * level * 1000;
                
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!livingEntity.isValid() || livingEntity.isDead() || endTime < System.currentTimeMillis()) {
                            cancel();
                            tasks.remove(livingEntity.getEntityId());
                            return;
                        }
                        double damage = base + livingEntity.getHealth() * multiplier;
                        livingEntity.damage(damage, player);
                        livingEntity.getWorld().spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, 1, 0), 10, Block.BEDROCK.createBlockData());
                    }
                }.runTaskTimer(plugin, getDefaultExtraData().get("interval") * 20L, getDefaultExtraData().get("interval") * 20L);
                
                tasks.put(entity.getEntityId(), task);
            }
        }
    }

    @Override
    public Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("interval", 20.0);
        data.put("durationMultiplier", 20.0);
        data.put("base", 1.0);
        data.put("multiplier", 0.066);
        return data;
    }
}