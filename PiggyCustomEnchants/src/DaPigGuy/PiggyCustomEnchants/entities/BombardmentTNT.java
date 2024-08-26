package DaPigGuy.PiggyCustomEnchants.entities.BombardmentTNT;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BombardmentTNT extends FallingBlock {

    private final int enchantmentLevel;

    public BombardmentTNT(Location location, int enchantmentLevel) {
        super(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), Material.TNT, (byte)0);
        this.enchantmentLevel = enchantmentLevel;
        this.setMetadata("Level", new FixedMetadataValue(PiggyCustomEnchants, enchantmentLevel));
    }

    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    @Override
    public void setMetadata(String metadataKey, FixedMetadataValue metadataValue) {
        super.setMetadata(metadataKey, metadataValue);
    }

    @Override
    public void removeMetadata(String metadataKey, JavaPlugin plugin) {
        super.removeMetadata(metadataKey, plugin);
    }

    public void explode() {
        EntityExplodeEvent event = new EntityExplodeEvent(this, this.getLocation(), 4.0f, false);
        this.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.getWorld().createExplosion(this.getLocation(), event.getYield(), event.getFire(), false);
        }
    }
}