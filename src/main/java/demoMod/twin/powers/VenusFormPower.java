package demoMod.twin.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;

public class VenusFormPower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("VenusFormPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public boolean switched = false;

    public VenusFormPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        loadRegion("echo");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount, DESC[switched ? 2 : 1], this.amount, DESC[switched ? 1 : 2]);
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (oldStance != newStance) {
            this.flash();
            switched = !switched;
            updateDescription();
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (card != null) {
            if (AbstractDungeon.player.hand.group.indexOf(card) == getStrengthIndex() && type == DamageInfo.DamageType.NORMAL) {
                return damage + (float) this.amount;
            }
        }
        return super.atDamageGive(damage, type, card);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card != null) {
            if (AbstractDungeon.player.hand.group.indexOf(card) == getDexterityIndex()) {
                return blockAmount + (float) this.amount;
            }
        }
        return this.modifyBlock(blockAmount);
    }

    private int getStrengthIndex() {
        return switched ? AbstractDungeon.player.hand.size() - 1 : 0;
    }

    private int getDexterityIndex() {
        return switched ? 0 : AbstractDungeon.player.hand.size() - 1;
    }
}
