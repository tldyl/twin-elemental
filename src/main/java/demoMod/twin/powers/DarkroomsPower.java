package demoMod.twin.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.interfaces.OnDomainTriggerSubscriber;
import demoMod.twin.stances.Blaze;

import java.util.List;

public class DarkroomsPower extends TwoAmountPower implements OnDomainTriggerSubscriber {
    public static final String POWER_ID = TwinElementalMod.makeID("DarkroomsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public DarkroomsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = 1;
        this.updateDescription();
        loadRegion("flameBarrier");
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount2++;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount, this.amount2);
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        this.type = newStance instanceof Blaze ? PowerType.BUFF : PowerType.DEBUFF;
    }

    @Override
    public void onDomainTrigger(List<DomainPower> domains) {
        this.flash();
        if (AbstractDungeon.player.stance instanceof Blaze) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, this.amount * domains.size())));
        } else {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -this.amount2 * domains.size())));
        }
    }

    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
