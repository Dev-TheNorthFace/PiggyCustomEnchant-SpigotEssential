package DaPigGuy.PiggyCustomEnchants.CustomEnchantManager;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.Map;

public class CustomEnchantManager {

    private static JavaPlugin plugin;
    private static final Map<Integer, CustomEnchant> enchants = new HashMap<>();

    public static void init(JavaPlugin plugin) {
        CustomEnchantManager.plugin = plugin;
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.CURSED, "Cursed", new VanillaEffect[]{VanillaEffects.WITHER()}, new int[]{60}, new int[]{1}, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.DRUNK, "Drunk", new VanillaEffect[]{VanillaEffects.SLOWNESS(), VanillaEffects.MINING_FATIGUE(), VanillaEffects.NAUSEA()}, new int[]{60, 60, 60}, new int[]{1, 1, 0}));
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.FROZEN, "Frozen", new VanillaEffect[]{VanillaEffects.SLOWNESS()}, new int[]{60}, new int[]{1}));
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.HARDENED, "Hardened", new VanillaEffect[]{VanillaEffects.WEAKNESS()}, new int[]{60}, new int[]{1}, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.POISONED, "Poisoned", new VanillaEffect[]{VanillaEffects.POISON()}, new int[]{60}, new int[]{1}, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new AttackerDeterrentEnchant(this, CustomEnchantIds.REVULSION, "Revulsion", new VanillaEffect[]{VanillaEffects.NAUSEA()}, new int[]{20}, new int[]{0}, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ConditionalDamageMultiplierEnchant(this, CustomEnchantIds.AERIAL, "Aerial", event -> !event.getDamager().isOnGround(), Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ConditionalDamageMultiplierEnchant(this, CustomEnchantIds.BACKSTAB, "Backstab", event -> event.getDamager().getDirectionVector().dot(event.getEntity().getDirectionVector()) > 0, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ConditionalDamageMultiplierEnchant(this, CustomEnchantIds.CHARGE, "Charge", event -> event.getDamager() instanceof Living && ((Living) event.getDamager()).isSprinting(), Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new LacedWeaponEnchant(this, CustomEnchantIds.BLIND, "Blind", Rarity.COMMON, new VanillaEffect[]{VanillaEffects.BLINDNESS()}, new int[]{20}, new int[]{0}, new int[]{100}));
        CustomEnchantManager.registerEnchantment(new LacedWeaponEnchant(this, CustomEnchantIds.CRIPPLE, "Cripple", Rarity.COMMON, new VanillaEffect[]{VanillaEffects.NAUSEA(), VanillaEffects.SLOWNESS()}, new int[]{100, 100}, new int[]{0, 1}));
        CustomEnchantManager.registerEnchantment(new LacedWeaponEnchant(this, CustomEnchantIds.POISON, "Poison", Rarity.UNCOMMON, new VanillaEffect[]{VanillaEffects.POISON()}));
        CustomEnchantManager.registerEnchantment(new LacedWeaponEnchant(this, CustomEnchantIds.WITHER, "Wither", Rarity.UNCOMMON, new VanillaEffect[]{VanillaEffects.WITHER()}));
        CustomEnchantManager.registerEnchantment(new ProjectileChangingEnchant(this, CustomEnchantIds.BLAZE, "Blaze", PiggyFireball.class));
        CustomEnchantManager.registerEnchantment(new ProjectileChangingEnchant(this, CustomEnchantIds.HOMING, "Homing", HomingArrow.class, 3, Rarity.MYTHIC));
        CustomEnchantManager.registerEnchantment(new ProjectileChangingEnchant(this, CustomEnchantIds.PORKIFIED, "Porkified", PigProjectile.class, 3, Rarity.MYTHIC));
        CustomEnchantManager.registerEnchantment(new ProjectileChangingEnchant(this, CustomEnchantIds.WITHERSKULL, "Wither Skull", PiggyWitherSkull.class, 1, Rarity.MYTHIC));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.ENRAGED, "Enraged", 5, CustomEnchant.TYPE_CHESTPLATE, CustomEnchant.ITEM_TYPE_CHESTPLATE, VanillaEffects.STRENGTH(), -1));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.GEARS, "Gears", 1, CustomEnchant.TYPE_BOOTS, CustomEnchant.ITEM_TYPE_BOOTS, VanillaEffects.SPEED(), 0, 0, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.GLOWING, "Glowing", 1, CustomEnchant.TYPE_HELMET, CustomEnchant.ITEM_TYPE_HELMET, VanillaEffects.NIGHT_VISION(), 0, 0, Rarity.COMMON));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.HASTE, "Haste", 5, CustomEnchant.TYPE_HAND, CustomEnchant.ITEM_TYPE_PICKAXE, VanillaEffects.HASTE(), 0, 1, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.OBSIDIANSHIELD, "Obsidian Shield", 1, CustomEnchant.TYPE_ARMOR_INVENTORY, CustomEnchant.ITEM_TYPE_ARMOR, VanillaEffects.FIRE_RESISTANCE(), 0, 0, Rarity.COMMON));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.OXYGENATE, "Oxygenate", 1, CustomEnchant.TYPE_HAND, CustomEnchant.ITEM_TYPE_PICKAXE, VanillaEffects.WATER_BREATHING(), 0, 0, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new ToggleableEffectEnchant(this, CustomEnchantIds.SPRINGS, "Springs", 1, CustomEnchant.TYPE_BOOTS, CustomEnchant.ITEM_TYPE_BOOTS, VanillaEffects.JUMP_BOOST(), 3, 0, Rarity.UNCOMMON));
        CustomEnchantManager.registerEnchantment(new AntiKnockbackEnchant(this, CustomEnchantIds.ANTIKNOCKBACK));
        CustomEnchantManager.registerEnchantment(new AntitoxinEnchant(this, CustomEnchantIds.ANTITOXIN));
        CustomEnchantManager.registerEnchantment(new AutoAimEnchant(this, CustomEnchantIds.AUTOAIM));
        CustomEnchantManager.registerEnchantment(new AutoRepairEnchant(this, CustomEnchantIds.AUTOREPAIR));
        CustomEnchantManager.registerEnchantment(new ArmoredEnchant(this, CustomEnchantIds.ARMORED));
        CustomEnchantManager.registerEnchantment(new BerserkerEnchant(this, CustomEnchantIds.BERSERKER));
        CustomEnchantManager.registerEnchantment(new BlessedEnchant(this, CustomEnchantIds.BLESSED));
        CustomEnchantManager.registerEnchantment(new BombardmentEnchant(this, CustomEnchantIds.BOMBARDMENT));
        CustomEnchantManager.registerEnchantment(new BountyHunterEnchant(this, CustomEnchantIds.BOUNTYHUNTER));
        CustomEnchantManager.registerEnchantment(new CactusEnchant(this, CustomEnchantIds.CACTUS));
        CustomEnchantManager.registerEnchantment(new ChickenEnchant(this, CustomEnchantIds.CHICKEN));
        CustomEnchantManager.registerEnchantment(new CloakingEnchant(this, CustomEnchantIds.CLOAKING));
        CustomEnchantManager.registerEnchantment(new DeathbringerEnchant(this, CustomEnchantIds.DEATHBRINGER));
        CustomEnchantManager.registerEnchantment(new DeepWoundsEnchant(this, CustomEnchantIds.DEEPWOUNDS));
        CustomEnchantManager.registerEnchantment(new DisarmingEnchant(this, CustomEnchantIds.DISARMING));
        CustomEnchantManager.registerEnchantment(new DisarmorEnchant(this, CustomEnchantIds.DISARMOR));
        CustomEnchantManager.registerEnchantment(new DrillerEnchant(this, CustomEnchantIds.DRILLER));
        CustomEnchantManager.registerEnchantment(new EndershiftEnchant(this, CustomEnchantIds.ENDERSHIFT));
        CustomEnchantManager.registerEnchantment(new EnergizingEnchant(this, CustomEnchantIds.ENERGIZING));
        CustomEnchantManager.registerEnchantment(new EnlightedEnchant(this, CustomEnchantIds.ENLIGHTED));
        CustomEnchantManager.registerEnchantment(new ExplosiveEnchant(this, CustomEnchantIds.EXPLOSIVE));
        CustomEnchantManager.registerEnchantment(new FarmerEnchant(this, CustomEnchantIds.FARMER));
        CustomEnchantManager.registerEnchantment(new FertilizerEnchant(this, CustomEnchantIds.FERTILIZER));
        CustomEnchantManager.registerEnchantment(new FocusedEnchant(this, CustomEnchantIds.FOCUSED));
        CustomEnchantManager.registerEnchantment(new ForcefieldEnchant(this, CustomEnchantIds.FORCEFIELD));
        CustomEnchantManager.registerEnchantment(new GooeyEnchant(this, CustomEnchantIds.GOOEY));
        CustomEnchantManager.registerEnchantment(new GrapplingEnchant(this, CustomEnchantIds.GRAPPLING));
        CustomEnchantManager.registerEnchantment(new GrowEnchant(this, CustomEnchantIds.GROW));
        CustomEnchantManager.registerEnchantment(new HallucinationEnchant(this, CustomEnchantIds.HALLUCINATION));
        CustomEnchantManager.registerEnchantment(new HarvestEnchant(this, CustomEnchantIds.HARVEST));
        CustomEnchantManager.registerEnchantment(new HeadhunterEnchant(this, CustomEnchantIds.HEADHUNTER));
        CustomEnchantManager.registerEnchantment(new HealingEnchant(this, CustomEnchantIds.HEALING));
        CustomEnchantManager.registerEnchantment(new HeavyEnchant(this, CustomEnchantIds.HEAVY));
        CustomEnchantManager.registerEnchantment(new ImplantsEnchant(this, CustomEnchantIds.IMPLANTS));
        CustomEnchantManager.registerEnchantment(new JackpotEnchant(this, CustomEnchantIds.JACKPOT));
        CustomEnchantManager.registerEnchantment(new JetpackEnchant(this, CustomEnchantIds.JETPACK));
        CustomEnchantManager.registerEnchantment(new LifestealEnchant(this, CustomEnchantIds.LIFESTEAL));
        CustomEnchantManager.registerEnchantment(new LightningEnchant(this, CustomEnchantIds.LIGHTNING));
        CustomEnchantManager.registerEnchantment(new LuckyCharmEnchant(this, CustomEnchantIds.LUCKYCHARM));
        CustomEnchantManager.registerEnchantment(new LumberjackEnchant(this, CustomEnchantIds.LUMBERJACK));
        CustomEnchantManager.registerEnchantment(new MagmaWalkerEnchant(this, CustomEnchantIds.MAGMAWALKER));
        CustomEnchantManager.registerEnchantment(new MeditationEnchant(this, CustomEnchantIds.MEDITATION));
        CustomEnchantManager.registerEnchantment(new MissileEnchant(this, CustomEnchantIds.MISSILE));
        CustomEnchantManager.registerEnchantment(new MolotovEnchant(this, CustomEnchantIds.MOLOTOV));
        CustomEnchantManager.registerEnchantment(new MoltenEnchant(this, CustomEnchantIds.MOLTEN));
        CustomEnchantManager.registerEnchantment(new OverloadEnchant(this, CustomEnchantIds.OVERLOAD));
        CustomEnchantManager.registerEnchantment(new ParachuteEnchant(this, CustomEnchantIds.PARACHUTE));
        CustomEnchantManager.registerEnchantment(new ParalyzeEnchant(this, CustomEnchantIds.PARALYZE));
        CustomEnchantManager.registerEnchantment(new PiercingEnchant(this, CustomEnchantIds.PIERCING));
        CustomEnchantManager.registerEnchantment(new PoisonousCloudEnchant(this, CustomEnchantIds.POISONOUSCLOUD));
        CustomEnchantManager.registerEnchantment(new ProwlEnchant(this, CustomEnchantIds.PROWL));
        CustomEnchantManager.registerEnchantment(new QuickeningEnchant(this, CustomEnchantIds.QUICKENING));
        CustomEnchantManager.registerEnchantment(new RadarEnchant(this, CustomEnchantIds.RADAR));
        CustomEnchantManager.registerEnchantment(new ReviveEnchant(this, CustomEnchantIds.REVIVE));
        CustomEnchantManager.registerEnchantment(new SelfDestructEnchant(this, CustomEnchantIds.SELFDESTRUCT));
        CustomEnchantManager.registerEnchantment(new ShieldedEnchant(this, CustomEnchantIds.SHIELDED));
        CustomEnchantManager.registerEnchantment(new ShrinkEnchant(this, CustomEnchantIds.SHRINK));
        CustomEnchantManager.registerEnchantment(new ShuffleEnchant(this, CustomEnchantIds.SHUFFLE));
        CustomEnchantManager.registerEnchantment(new SmeltingEnchant(this, CustomEnchantIds.SMELTING));
        CustomEnchantManager.registerEnchantment(new SoulboundEnchant(this, CustomEnchantIds.SOULBOUND));
        CustomEnchantManager.registerEnchantment(new SpiderEnchant(this, CustomEnchantIds.SPIDER));
        CustomEnchantManager.registerEnchantment(new StompEnchant(this, CustomEnchantIds.STOMP));
        CustomEnchantManager.registerEnchantment(new TankEnchant(this, CustomEnchantIds.TANK));
        CustomEnchantManager.registerEnchantment(new TelepathyEnchant(this, CustomEnchantIds.TELEPATHY));
        CustomEnchantManager.registerEnchantment(new VacuumEnchant(this, CustomEnchantIds.VACUUM));
        CustomEnchantManager.registerEnchantment(new VampireEnchant(this, CustomEnchantIds.VAMPIRE));
        CustomEnchantManager.registerEnchantment(new VolleyEnchant(this, CustomEnchantIds.VOLLEY));
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void registerEnchantment(CustomEnchant enchant) {
        Enchantment.registerEnchantment(new EnchantmentWrapper(enchant.getId()) {
            @Override
            public String getName() {
                return enchant.getName();
            }
        });
        enchants.put(enchant.getId(), enchant);

        plugin.getLogger().info("Custom Enchantment '" + enchant.getName() + "' registered with id " + enchant.getId());
    }

    public static void unregisterEnchantment(int id) {
        CustomEnchant enchant = enchants.remove(id);
        if (enchant != null) {
            plugin.getLogger().info("Custom Enchantment '" + enchant.getName() + "' unregistered with id " + id);
        }
    }

    public static CustomEnchant getEnchantment(int id) {
        return enchants.get(id);
    }

    public static Map<Integer, CustomEnchant> getEnchantments() {
        return enchants;
    }
}

abstract class CustomEnchant {
    private final int id;
    private final String name;
    private final int level;

    public CustomEnchant(int id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}

class AttackerDeterrentEnchant extends CustomEnchant {
    public AttackerDeterrentEnchant(int id, String name, int level) {
        super(id, name, level);
    }
}