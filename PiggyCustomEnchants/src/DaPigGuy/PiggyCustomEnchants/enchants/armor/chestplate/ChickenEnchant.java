package DaPigGuy.PiggyCustomEnchants.enchants.armor.chestplate.ChickenEnchant;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChickenEnchant extends TickingEnchantment {

    private final PiggyCustomEnchants plugin;
    private final Random random = new Random();

    public ChickenEnchant(PiggyCustomEnchants plugin, int id) {
        super(plugin, id, "Chicken", CustomEnchant.Rarity.UNCOMMON, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE);
        this.plugin = plugin;
    }

    @Override
    public Map<String, Object> getDefaultExtraData() {
        Map<String, Object> extraData = new HashMap<>();
        extraData.put("treasureChanceMultiplier", 5);
        extraData.put("treasures", List.of("GOLD_INGOT:0:1"));
        extraData.put("interval", 1200 * 5);
        return extraData;
    }

    @Override
    public void tick(Player player, ItemStack item, int slot, int level) {
        int treasureChanceMultiplier = (int) getExtraData().get("treasureChanceMultiplier");
        List<String> treasures = (List<String>) getExtraData().get("treasures");
        if (random.nextInt(100) <= treasureChanceMultiplier * level) {
            String dropData = treasures.get(random.nextInt(treasures.size()));
            String[] dropParts = dropData.split(":");
            ItemStack treasureItem;
            if (dropParts.length < 3) {
                treasureItem = new ItemStack(Material.GOLD_INGOT);
            } else {
                Material material = Material.matchMaterial(dropParts[0]);
                int amount = Integer.parseInt(dropParts[2]);
                treasureItem = new ItemStack(material, amount);
            }

            dropItem(player, treasureItem);
            player.sendMessage("§aYou have laid a " + getFormattedName(treasureItem) + "...");
        } else {
            ItemStack egg = new ItemStack(Material.EGG);
            dropItem(player, egg);
            player.sendMessage("§aYou have laid an egg.");
        }
    }

    private void dropItem(Player player, ItemStack item) {
        Vector direction = player.getLocation().getDirection().multiply(-0.4);
        player.getWorld().dropItem(player.getLocation(), item).setVelocity(direction);
    }

    private String getFormattedName(ItemStack item) {
        String itemName = item.getType().toString().toLowerCase().replace("_", " ");
        return (isVowel(itemName.charAt(0)) ? "n " : " ") + itemName;
    }

    private boolean isVowel(char c) {
        return "aeiou".indexOf(Character.toLowerCase(c)) != -1;
    }

    @Override
    public int getTickingInterval() {
        return (int) getExtraData().get("interval");
    }
}