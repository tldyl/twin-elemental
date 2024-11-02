package demoMod.twin.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.stances.Blaze;
import demoMod.twin.stances.Freeze;

public class SwitchLeaderAction extends AbstractGameAction {
    private final AbstractPlayer p = AbstractDungeon.player;

    @Override
    public void update() {
        if (p.stance instanceof Freeze) {
            addToTop(new ChangeStanceAction(new Blaze()));
        } else {
            addToTop(new ChangeStanceAction(new Freeze()));
        }
        isDone = true;
    }

    public static void updateTwinCardBackground(CardGroup group) {
        for (AbstractCard card : group.group) {
            if (card instanceof AbstractTwinCard) {
                ((AbstractTwinCard) card).updateBackground();
            }
        }
    }
}
