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
import demoMod.twin.helpers.PowerRegionLoader;

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
        PowerRegionLoader.load(this);
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
            int index = AbstractDungeon.player.hand.group.indexOf(card);
            return damage + getBuffAmount(index, true);
        }
        return super.atDamageGive(damage, type, null);
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        if (card != null) {
            int index = AbstractDungeon.player.hand.group.indexOf(card);
            return blockAmount + getBuffAmount(index, false);
        }
        return this.modifyBlock(blockAmount);
    }

    public int getBuffAmount(int cardIndex, boolean isStrength) {
        if (isStrength) {
            int lowerBound = switched ? AbstractDungeon.player.hand.size() / 2 : 0;
            int upperBound = switched ? AbstractDungeon.player.hand.size() - 1 : AbstractDungeon.player.hand.size() / 2;
            if (cardIndex >= lowerBound && cardIndex <= upperBound) {
                return this.amount - (switched ? upperBound - cardIndex : cardIndex);
            }
        } else {
            int lowerBound = switched ? 0 : AbstractDungeon.player.hand.size() / 2;
            int upperBound = switched ? AbstractDungeon.player.hand.size() / 2 : AbstractDungeon.player.hand.size() - 1;
            if (cardIndex >= lowerBound && cardIndex <= upperBound) {
                return this.amount - (switched ? cardIndex : upperBound - cardIndex);
            }
        }
        return 0;
    }

    public int getBuffAmount(int cardIndex) {
        int lowerBound = switched ? AbstractDungeon.player.hand.size() / 2 : 0;
        int upperBound = switched ? AbstractDungeon.player.hand.size() - 1 : AbstractDungeon.player.hand.size() / 2;
        if (cardIndex >= lowerBound && cardIndex <= upperBound) {
            return this.amount - (switched ? upperBound - cardIndex : cardIndex);
        } else {
            lowerBound = switched ? 0 : AbstractDungeon.player.hand.size() / 2;
            upperBound = switched ? AbstractDungeon.player.hand.size() / 2 : AbstractDungeon.player.hand.size() - 1;
            if (cardIndex >= lowerBound && cardIndex <= upperBound) {
                return this.amount - (switched ? cardIndex : upperBound - cardIndex);
            }
        }
        return 0;
    }
}
