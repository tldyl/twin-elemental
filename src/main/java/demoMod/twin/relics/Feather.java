package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;

public class Feather extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("Feather");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/feather.png"));
    public static final Texture OUTLINE_IMG = new Texture(TwinElementalMod.getResourcePath("relics/feather_outline.png"));

    private AbstractCard card;
    private boolean activated = false;

    public Feather() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        card = targetCard;
        if (targetCard.hasTag(CardTagsEnum.COPORATE) && targetCard.costForTurn == 0 && !activated) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            card.returnToHand = true;
            activated = true;
        }
    }

    @Override
    public void onPlayerEndTurn() {
        activated = false;
    }

    @Override
    public void onDrawOrDiscard() {
        if (card != null) {
            card.returnToHand = false;
            card = null;
        }
    }
}
