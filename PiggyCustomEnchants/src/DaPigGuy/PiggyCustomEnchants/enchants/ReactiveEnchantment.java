package DaPigGuy.PiggyCustomEnchants.enchants.ReactiveEnchantment;

public class ReactiveEnchantment extends CustomEnchant {

    private final ReactiveTrait reactiveTrait;

    public ReactiveEnchantment(PiggyCustomEnchants plugin, int id) {
        super(plugin, id);
        this.reactiveTrait = new ReactiveTrait();
    }

    public void applyReactiveEffects() {
        reactiveTrait.applyEffects();
    }
}