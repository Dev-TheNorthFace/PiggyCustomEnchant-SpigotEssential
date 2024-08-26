package DaPigGuy.PiggyCustomEnchants.EventListener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityEffectAddEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryTransactionEvent;
import org.bukkit.event.player.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class EventListener implements Listener {

    private final JavaPlugin plugin;

    public EventListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ReactiveEnchantment::attemptReaction(player, event);
    }

    @EventHandler
    public void onEntityBlockChange(EntityBlockFormEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.PRIMED_TNT) {s
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player player = (Player) entity;
                Utils::shouldTakeFallDamage
                event.setCancelled(true);
            }
            ReactiveEnchantment::attemptReaction(entity, event);
        }
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity attacker = damageEvent.getDamager();
            if (attacker instanceof Player) {
                ReactiveEnchantment::attemptReaction(attacker, event);
            }
        }
    }

    @EventHandler
    public void onEntityEffectAdd(EntityEffectAddEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            ReactiveEnchantment::attemptReaction(entity, event);
        }
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        Entity shooter = event.getEntity();
        if (shooter instanceof Player) {
            ReactiveEnchantment::attemptReaction(shooter, event);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        ReactiveEnchantment::attemptReaction(player, event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ReactiveEnchantment::attemptReaction(player, event);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack oldItem = inventory.getItem(event.getPreviousSlot());
        ItemStack newItem = inventory.getItem(event.getNewSlot());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Utils::shouldTakeFallDamage
        ReactiveEnchantment::attemptReaction(player, event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ReactiveEnchantment::attemptReaction(player, event);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity shooter = event.getEntity().getShooter();
        if (shooter instanceof Player) {
            ReactiveEnchantment::attemptReaction(shooter, event);
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity projectile = event.getEntity();
        Entity shooter = projectile.getShooter();
        if (shooter instanceof Player) {
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
    }

    @EventHandler
    public void onInventoryTransaction(InventoryTransactionEvent event) {
    }
}