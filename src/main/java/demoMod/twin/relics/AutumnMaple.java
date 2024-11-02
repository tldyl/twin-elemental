package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import demoMod.twin.TwinElementalMod;

public class AutumnMaple extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("AutumnMaple");

    public AutumnMaple() {
        super(ID, "circlet.png", RelicTier.BOSS, LandingSound.MAGICAL);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        this.counter++;
        if (this.counter == 11) {
            this.beginLongPulse();
        }
        if (this.counter >= 12) {
            this.flash();
            this.stopPulse();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new RelicAboveCreatureAction(p, this));
            addToBot(new ApplyPowerAction(p, p, new BufferPower(p, 1)));
            this.counter = 0;
        }
    }
}
