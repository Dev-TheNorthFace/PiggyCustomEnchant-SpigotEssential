package DaPigGuy.PiggyCustomEnchants.enchants.ToggleableEnchantment;

public class ToggleableEnchantment extends CustomEnchant {

    private final ToggleTrait toggleTrait;

    public ToggleableEnchantment(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.toggleTrait = new ToggleTrait();
    }

    public void toggleEffect() {
        toggleTrait.toggleEffect();
    }
}