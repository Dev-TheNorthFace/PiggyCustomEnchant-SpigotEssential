package DaPigGuy.PiggyCustomEnchants.enchants.weapons.GooeyEnchant;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GooeyEnchant extends ReactiveEnchantment {

    public GooeyEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Gooey";
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof LivingEntity) {
                getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!entity.isDead()) {
                            Vector velocity = entity.getVelocity();
                            double base = getDefaultExtraData().get("base");
                            double multiplier = getDefaultExtraData().get("multiplier");
                            entity.setVelocity(new Vector(velocity.getX(), level * multiplier + base, velocity.getZ()));
                        }
                    }
                }, 1L);
            }
        }
    }

    @Override
    public Map<String, Double> getDefaultExtraData() {
        Map<String, Double> data = new HashMap<>();
        data.put("base", 0.75);
        data.put("multiplier", 0.15);
        return data;
    }
}