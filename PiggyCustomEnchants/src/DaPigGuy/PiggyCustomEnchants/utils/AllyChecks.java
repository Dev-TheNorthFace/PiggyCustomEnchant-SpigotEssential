package DaPigGuy.PiggyCustomEnchants.utils.AllyChecks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class AllyChecks {

    private static final List<Check> checks = new ArrayList<>();

    public static void addCheck(Plugin plugin, BiPredicate<Player, Entity> check) {
        checks.add(new Check(plugin, check));
    }

    public static boolean isAlly(Player player, Entity entity) {
        for (Check check : checks) {
            if (check.plugin.isEnabled()) {
                if (check.check.test(player, entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Check {
        private final Plugin plugin;
        private final BiPredicate<Player, Entity> check;

        public Check(Plugin plugin, BiPredicate<Player, Entity> check) {
            this.plugin = plugin;
            this.check = check;
        }
    }
}