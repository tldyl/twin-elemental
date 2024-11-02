package demoMod.twin.stances;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.AppendAnimationAction;
import demoMod.twin.actions.ChangeAnimationAction;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.characters.ZetsuEnnNoTwins;

import java.util.stream.Collectors;

public class Blaze extends AbstractStance {
    public static int switchedTimesThisTurn = 0;

    public static final String STANCE_ID = TwinElementalMod.makeID("Blaze");
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);

    public Blaze() {
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
            AbstractDungeon.actionManager.addToBottom(new ChangeAnimationAction(false, "ice_off"));
            AbstractDungeon.actionManager.addToBottom(new AppendAnimationAction("ice_change_fire", false));
            AbstractDungeon.actionManager.addToBottom(new AppendAnimationAction("ice_off_idle", false));
            AbstractDungeon.actionManager.addToBottom(new AppendAnimationAction("fire_on", false));
            AbstractDungeon.actionManager.addToBottom(new AppendAnimationAction("fire_idle", true));
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
        switchedTimesThisTurn++;
    }

    @Override
    public void onEndOfTurn() {
        switchedTimesThisTurn = 0;
    }

    @Override
    public void onExitStance() {

    }
}
