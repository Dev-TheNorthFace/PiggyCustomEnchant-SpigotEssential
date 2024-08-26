package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.BombardmentEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class BombardmentEnchant extends ReactiveEnchantment {

    public BombardmentEnchant(String name, int rarity) {
        this.name = "Bombardment";
        this.rarity = rarity;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByProjectileEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, PlayerInventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByProjectileEvent) {
            EntityDamageByProjectileEvent projectileEvent = (EntityDamageByProjectileEvent) event;
            if (projectileEvent.getDamager() instanceof Player && projectileEvent.getEntity() instanceof Player) {
                Player target = (Player) projectileEvent.getEntity();
                Location targetLocation = target.getLocation();
                World world = targetLocation.getWorld();
                if (world != null) {
                    Location spawnLocation = targetLocation.clone().add(0, 255 - targetLocation.getY(), 0);
                    TNTPrimed tnt = (TNTPrimed) world.spawnEntity(spawnLocation, EntityType.PRIMED_TNT);
                    tnt.setFuseTicks(80);
                    tnt.setVelocity(new Vector(0, -5, 0));
                    tnt.setSource(player);
                }
            }
        }
    }
}