package DaPigGuy.PiggyCustomEnchants.blocks.PiggyObsidian;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;

public class PiggyObsidian extends JavaPlugin implements Listener {

    private final Random random = new Random();
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockUpdate(BlockRedstoneEvent event) {
        if (event.getBlock().getType() == Material.OBSIDIAN) {
            if (random.nextInt(4) == 0 || countNeighbors(event.getBlock()) < 4) {
                slightlyMelt(event.getBlock(), true);
            } else {
                getServer().getScheduler().runTaskLater(this, () -> {
                    if (event.getBlock().getType() == Material.OBSIDIAN) {
                        if (random.nextInt(4) == 0 || countNeighbors(event.getBlock()) < 4) {
                            slightlyMelt(event.getBlock(), true);
                        }
                    }
                }, random.nextInt(20) + 20L);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.OBSIDIAN) {
            block.getWorld().getBlockAt(block.getLocation()).setType(Material.LAVA);
            event.setDropItems(false);
        }
    }

    private int countNeighbors(Block block) {
        int count = 0;
        for (BlockFace face : BlockFace.values()) {
            Block neighbor = block.getRelative(face);
            if (neighbor.getType() == Material.OBSIDIAN) {
                count++;
                if (count >= 4) return count;
            }
        }
        return count;
    }

    private void slightlyMelt(Block block, boolean meltNeighbors) {
        if (block.getType() == Material.OBSIDIAN) {
            int age = getAge(block);
            if (age < 3) {
                setAge(block, age + 1);
                getServer().getScheduler().runTaskLater(this, () -> {
                    if (block.getType() == Material.OBSIDIAN) {
                        if (random.nextInt(4) == 0 || countNeighbors(block) < 4) {
                            slightlyMelt(block, true);
                        }
                    }
                }, random.nextInt(20) + 20L);
            } else {
                block.setType(Material.AIR);
                if (meltNeighbors) {
                    for (BlockFace face : BlockFace.values()) {
                        Block neighbor = block.getRelative(face);
                        if (neighbor.getType() == Material.OBSIDIAN) {
                            slightlyMelt(neighbor, false);
                        }
                    }
                }
            }
        }
    }

    private int getAge(Block block) {
        return 0;
    }
}