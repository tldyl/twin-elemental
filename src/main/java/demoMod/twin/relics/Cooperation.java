package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.tempCards.Cooperate;

public class Cooperation extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("Cooperation");

    public Cooperation() {
        super(ID, "circlet.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractCard card = new Cooperate();
        card.upgrade();
        addToBot(new MakeTempCardInHandAction(card));
    }
}
