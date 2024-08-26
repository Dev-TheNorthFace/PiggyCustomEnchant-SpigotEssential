package DaPigGuy.PiggyCustomEnchants.enchants.weapons.HallucinationEnchant;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.Map;

public class HallucinationEnchant extends ReactiveEnchantment {

    private static final Map<String, BukkitTask> hallucinating = new HashMap<>();
    
    public HallucinationEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.name = "Hallucination";
        this.rarity = Rarity.MYTHIC;
    }

    @Override
    public void react(Player player, ItemStack item, Inventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = ((EntityDamageByEntityEvent) event).getEntity();
            if (entity instanceof Player && !hallucinating.containsKey(entity.getName())) {
                Player target = (Player) entity;
                final Vector originalPosition = target.getLocation().toVector();
                hallucinating.put(target.getName(), new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (target.isOnline()) {
                            for (int x = (int) originalPosition.getX() - 1; x <= (int) originalPosition.getX() + 1; x++) {
                                for (int y = (int) originalPosition.getY() - 1; y <= (int) originalPosition.getY() + 2; y++) {
                                    for (int z = (int) originalPosition.getZ() - 1; z <= (int) originalPosition.getZ() + 1; z++) {
                                        Location loc = new Location(target.getWorld(), x, y, z);
                                        Material material = Material.BEDROCK;
                                        if (loc.equals(target.getLocation())) material = Material.LAVA;
                                        if (loc.equals(target.getLocation().add(0, 1, 0))) {
                                            material = Material.SIGN;
                                        }
                                        target.getWorld().getBlockAt(loc).setType(material);
                                    }
                                }
                            }
                            target.sendMessage("You seem to be hallucinating...");
                        }
                    }
                }.runTaskTimer(getPlugin(), 1L, 1L));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BukkitTask task = hallucinating.remove(target.getName());
                        if (task != null) {
                            task.cancel();
                        }
                        for (int x = (int) originalPosition.getX() - 1; x <= (int) originalPosition.getX() + 1; x++) {
                            for (int y = (int) originalPosition.getY() - 1; y <= (int) originalPosition.getY() + 2; y++) {
                                for (int z = (int) originalPosition.getZ() - 1; z <= (int) originalPosition.getZ() + 1; z++) {
                                    Location loc = new Location(target.getWorld(), x, y, z);
                                    target.getWorld().getBlockAt(loc).setType(Material.AIR);
                                }
                            }
                        }
                    }
                }.runTaskLater(getPlugin(), 20L * 60L);
            }
        }
    }
}