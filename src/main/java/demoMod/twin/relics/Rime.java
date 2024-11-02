package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.powers.TracePower;

public class Rime extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("Rime");

    public Rime() {
        super(ID, "circlet.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RelicAboveCreatureAction(p, this));
        addToBot(new ApplyPowerAction(p, p, new TracePower(p, 1)));
    }
}