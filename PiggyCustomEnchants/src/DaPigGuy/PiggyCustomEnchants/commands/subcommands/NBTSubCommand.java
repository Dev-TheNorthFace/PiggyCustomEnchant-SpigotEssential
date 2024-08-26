package DaPigGuy.PiggyCustomEnchants.commands.subcommands.NBTSubCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class NBTSubCommand extends Command {

    private final PiggyCustomEnchants plugin;

    public NBTSubCommand(PiggyCustomEnchants plugin) {
        super("nbt");
        this.plugin = plugin;
        setDescription("Display NBT data of the item in hand");
        setUsage("/ce nbt");
        setPermission("piggycustomenchants.command.ce.nbt");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null) {
                String nbtData = getNBTData(itemInHand);
                player.sendMessage(ChatColor.GREEN + "Item NBT Data: " + ChatColor.WHITE + nbtData);
            } else {
                player.sendMessage(ChatColor.RED + "No item meta found.");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used in-game.");
            return false;
        }
    }

    private String getNBTData(ItemStack item) {
        NBTCompound nbt = NBTItem.fromItemTag(item).getCompound();
        return nbt.toString();n
        return "NBT data for item: " + item.toString();
    }
}