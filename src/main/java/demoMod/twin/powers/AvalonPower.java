package demoMod.twin.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;

public class AvalonPower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("AvalonPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public AvalonPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        loadRegion("flameBarrier");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[0], this.amount * 6, this.amount);
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (AbstractDungeon.player.hand.size() >= 6) {
            AbstractCard card = AbstractDungeon.player.hand.group.get(5);
            if (card.canUse(AbstractDungeon.player, null) && card.hasEnoughEnergy()) {
                card.glowColor = Color.GOLD.cpy();
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.player.hand.group.indexOf(card) == 5) {
            this.flash();
            addToBot(new GainBlockAction(owner, owner, this.amount * 6));
            addToBot(new DrawCardAction(this.amount));
        }
    }
}