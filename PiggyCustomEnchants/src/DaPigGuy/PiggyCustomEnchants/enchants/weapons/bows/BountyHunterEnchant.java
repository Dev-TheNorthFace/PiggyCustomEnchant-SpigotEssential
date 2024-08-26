package DaPigGuy.PiggyCustomEnchants.enchants.weapons.bows.BountyHunterEnchant;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import java.util.Random;

public class BountyHunterEnchant extends ReactiveEnchantment {

    private final Random random = new Random();

    public BountyHunterEnchant(String name, int rarity) {
        this.name = "Bounty Hunter";
        this.rarity = rarity;
        this.cooldownDuration = 30;
        this.itemType = CustomEnchant.ITEM_TYPE_BOW;
    }

    @Override
    public Class<?>[] getReagent() {
        return new Class<?>[]{EntityDamageByEntityEvent.class};
    }

    @Override
    public void react(Player player, ItemStack item, PlayerInventory inventory, int slot, Event event, int level, int stack) {
        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
            Entity damager = damageEvent.getDamager();
            if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
                Player shooter = (Player) ((Projectile) damager).getShooter();
                if (shooter.equals(player)) {
                    Material bountyMaterial = getBounty();
                    int amount = random.nextInt(getExtraData("base") + level * getExtraData("multiplier")) + 1;
                    ItemStack bountyItem = new ItemStack(bountyMaterial, amount);
                    player.getInventory().addItem(bountyItem);
                }
            }
        }
    }

    private Material getBounty() {
        int randomValue = random.nextInt(76);
        double currentChance = 2.5;

        if (randomValue < currentChance) {
            return Material.EMERALD;
        }
        currentChance += 5;
        if (randomValue < currentChance) {
            return Material.DIAMOND;
        }
        currentChance += 15;
        if (randomValue < currentChance) {
            return Material.GOLD_INGOT;
        }
        currentChance += 27.5;
        if (randomValue < currentChance) {
            return Material.IRON_INGOT;
        }
        return Material.COAL;
    }
}