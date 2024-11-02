package demoMod.twin.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;

public class LoseTracePower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("LoseTracePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public LoseTracePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.name = NAME;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.updateDescription();
        loadRegion("flex");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        addToBot(new ReducePowerAction(owner, owner, TracePower.POWER_ID, this.amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
