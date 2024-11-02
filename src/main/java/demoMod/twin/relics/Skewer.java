package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;

public class Skewer extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("Skewer");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/skewer.png"));
    public static final Texture OUTLINE_IMG = new Texture(TwinElementalMod.getResourcePath("relics/skewer_outline.png"));

    public Skewer() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard != null && targetCard.hasTag(CardTagsEnum.DOMAIN)) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(1));
        }
    }
}
