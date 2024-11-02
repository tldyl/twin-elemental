package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;

public class FetterOfDicoaster extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("FetterOfDicoaster");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/fetterOfDicoaster.png"));
    public static final Texture OUTLINE_IMG = new Texture(TwinElementalMod.getResourcePath("relics/fetterOfDicoaster_outline.png"));

    private boolean activated = false;

    public FetterOfDicoaster() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void obtain() {
        this.instantObtain(AbstractDungeon.player, 0, true);
        this.flash();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(FetterOfElement.ID);
    }

    @Override
    public void onChangeStance(AbstractStance prevStance, AbstractStance newStance) {
        if (prevStance != newStance && !activated) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new GainEnergyAction(1));
            addToBot(new DrawCardAction(1));
            activated = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        activated = false;
    }

    @Override
    public void onVictory() {
        activated = false;
    }
}
