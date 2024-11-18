package demoMod.twin.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.helpers.PowerRegionLoader;

public class DustPower extends TwoAmountPower {
    public static final String POWER_ID = TwinElementalMod.makeID("DustPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public DustPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount2 += stackAmount;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount);
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (oldStance != newStance && this.amount > 0) {
            this.flash();
            this.amount--;
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.amount = this.amount2;
    }
}
