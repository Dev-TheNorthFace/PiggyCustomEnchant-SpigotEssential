package DaPigGuy.PiggyCustomEnchants.enchants.TickingEnchantment;

public class TickingEnchantment extends CustomEnchant {

    private final TickingTrait tickingTrait;

    public TickingEnchantment(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.tickingTrait = new TickingTrait();
    }

    public void applyTickingEffects() {
        tickingTrait.applyTickingEffects();
    }
}