package demoMod.twin.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.stances.Freeze;

import java.util.stream.Collectors;

public class NiflheimrPower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("NiflheimrPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    private final boolean upgraded;

    public NiflheimrPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        loadRegion("flameBarrier");
        this.upgraded = upgraded;
        if (upgraded) {
            this.ID += "+";
            this.name += "+";
        }
    }

    @Override
    public void updateDescription() {
        if (upgraded) {
            this.description = String.format(DESC[1], this.amount, this.amount);
        } else {
            this.description = String.format(DESC[0], this.amount);
        }
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (oldStance != newStance && newStance instanceof Freeze) {
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                addToBot(new ApplyPowerAction(m, owner, new StrengthPower(m, -this.amount)));
                if (!m.hasPower(ArtifactPower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(m, owner, new GainStrengthPower(m, this.amount)));
                }
                if (upgraded) {
                    addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, this.amount, owner instanceof AbstractMonster)));
                }
            }
        }
    }
}
