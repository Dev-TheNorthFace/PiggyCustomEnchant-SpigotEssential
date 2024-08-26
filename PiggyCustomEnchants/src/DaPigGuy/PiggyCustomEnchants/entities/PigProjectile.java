package DaPigGuy.PiggyCustomEnchants.entities.PigProjectile;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Pig;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class PigProjectile extends Pig implements Listener {
    
    private static final int MAX_PORK_LEVEL = 6;
    private static final int MIN_PORK_LEVEL = 1;

    private int porkLevel;
    private boolean zombie;

    public PigProjectile(Location location, int porkLevel) {
        super(location.getWorld());
        this.setCustomNameVisible(true);
        this.setCustomName("PigProjectile");
        this.setMetadata("porkLevel", new FixedMetadataValue(PiggyCustomEnchantsPlugin.getInstance(), porkLevel));
        this.porkLevel = Math.max(MIN_PORK_LEVEL, Math.min(porkLevel, MAX_PORK_LEVEL));
        this.zombie = porkLevel >= 5;
        setupProperties();
        this.teleport(location);
        this.setVelocity(new Vector(0, 0, 0));
    }

    private void setupProperties() {
    }

    @Override
    public void setVelocity(Vector velocity) {
        super.setVelocity(velocity.multiply(1.0 - 0.01));
    }

    @Override
    public void onCollision(Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            Player shooter = (Player) this.getShooter();
            if (shooter != null && !AllyChecks.isAlly(shooter, livingEntity)) {
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 800, 1));
            }
        }
        this.remove();
    }

    @Override
    public void onCollisionWithBlock() {
        this.remove();
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
    }

    public ItemStack[] getDrops() {
        ItemStack drop = new ItemStack(Material.AIR);
        switch (porkLevel) {
            case 2:
                drop = new ItemStack(Material.RAW_PORKCHOP);
                drop.getItemMeta().setDisplayName("Mysterious Raw Pork");
                break;
            case 3:
                drop = new ItemStack(Material.COOKED_PORKCHOP);
                drop.getItemMeta().setDisplayName("Mysterious Cooked Pork");
                break;
            case 5:
                drop = new ItemStack(Material.ROTTEN_FLESH);
                drop.getItemMeta().setDisplayName("Mysterious Rotten Pork");
                break;
            case 6:
                drop = new ItemStack(Material.ROTTEN_FLESH);
                drop.getItemMeta().setDisplayName("Mysterious Rotten Pork");
                break;
            default:
                break;
        }
        return new ItemStack[]{drop};
    }
}