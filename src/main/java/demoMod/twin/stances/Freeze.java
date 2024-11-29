package demoMod.twin.stances;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.characters.ZetsuEnnNoTwins;
import demoMod.twin.patches.CharacterSelectScreenPatch;

import java.util.stream.Collectors;

public class Freeze extends AbstractStance {
    public static final String STANCE_ID = TwinElementalMod.makeID("Freeze");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    public Freeze() {
        this.name = stanceString.NAME;
        this.ID = STANCE_ID;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    @Override
    public void onEnterStance() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof ZetsuEnnNoTwins) {
            if (CharacterSelectScreenPatch.reskinIndex == 0) {
                AbstractDungeon.player.state.setAnimation(0, "fire_change_ice", false);
                AbstractDungeon.player.state.addAnimation(0, "ice_idle", true, 0.0F);
            } else {
                if (AbstractDungeon.player.img != null) {
                    AbstractDungeon.player.img.dispose();
                }
                AbstractDungeon.player.img = new Texture(TwinElementalMod.getResourcePath("char/reskin_jacob.png"));
            }
        }

        SwitchLeaderAction.updateTwinCardBackground(p.drawPile);
        SwitchLeaderAction.updateTwinCardBackground(p.hand);
        SwitchLeaderAction.updateTwinCardBackground(p.discardPile);
        SwitchLeaderAction.updateTwinCardBackground(p.exhaustPile);
        SwitchLeaderAction.updateTwinCardBackground(p.limbo);

        for (AbstractCard card : p.hand.group.stream().filter(c -> c instanceof AbstractTwinCard).collect(Collectors.toList())) {
            AbstractTwinCard twinCard = (AbstractTwinCard) card;
            twinCard.checkCoporateState();
        }
        Blaze.switchedTimesThisTurn++;
    }

    @Override
    public void onEndOfTurn() {
        Blaze.switchedTimesThisTurn = 0;
    }

    @Override
    public void onExitStance() {

    }
}
