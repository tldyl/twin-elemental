package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;

public class CrimsonCherry extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("CrimsonCherry");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/crimsonCherry.png"));

    public CrimsonCherry() {
        super(ID, IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onShuffle() {
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new DrawCardAction(2));
    }
}
