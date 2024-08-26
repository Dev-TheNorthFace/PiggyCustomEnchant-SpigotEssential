package DaPigGuy.PiggyCustomEnchants.tasks.CheckDisabledEnchantsTask;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class CheckDisabledEnchantsTask extends BukkitRunnable {

    private final JavaPlugin plugin;

    public CheckDisabledEnchantsTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        try {
            URL url = new URL("https://gist.githubusercontent.com/DaPigGuy/9c65a998bc0aa8d6b4708796110f7d11/raw/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

            String body = result.toString();
            onCompletion(body, null);

        } catch (Exception e) {
            onCompletion(null, e.getMessage());
        }
    }

    private void onCompletion(String body, String error) {
        if (error == null) {
            if (plugin.isEnabled()) {
                List<Map<String, Object>> disabledEnchants = parseJson(body);
                for (Map<String, Object> disabledEnchantEntry : disabledEnchants) {
                    List<String> apis = (List<String>) disabledEnchantEntry.get("api");
                    List<String> versions = (List<String>) disabledEnchantEntry.get("version");

                    if (apis.stream().anyMatch(api -> plugin.getDescription().getCompatibleApis().contains(api)) ||
                        apis.contains("all") ||
                        versions.contains(plugin.getDescription().getVersion()) ||
                        versions.contains("all")) {

                        plugin.getLogger().info("Enchantment " + disabledEnchantEntry.get("name") +
                                " (id " + disabledEnchantEntry.get("id") + ") has been remotely disabled for " +
                                disabledEnchantEntry.get("reason"));

                        CustomEnchantManager.unregisterEnchantment((String) disabledEnchantEntry.get("id"));
                    }
                }
            }
        } else {
            plugin.getLogger().severe("Failed to check disabled enchants: " + error);
        }
    }

    private List<Map<String, Object>> parseJson(String json) {
        return List.of();
    }
}