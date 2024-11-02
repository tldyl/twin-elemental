package demoMod.twin.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectCardInCardGroupAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(TwinElementalMod.makeID("SelectCardInCardGroupAction")).TEXT;
    private final Predicate<AbstractCard> condition;
    private final Consumer<AbstractCard> action;
    private final CardGroup cardGroup;

    public SelectCardInCardGroupAction(int amount, Predicate<AbstractCard> condition, Consumer<AbstractCard> action, CardGroup cardGroup) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = (this.startDuration = Settings.ACTION_DUR_FAST);
        this.condition = condition;
        this.action = action;
        this.cardGroup = cardGroup;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration && AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID) {
            CardGroup temp;
            if (this.cardGroup.isEmpty()) {
                this.isDone = true;
                return;
            }
            temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.cardGroup.group) {
                if (this.condition.test(c)) temp.addToTop(c);
            }
            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.gridSelectScreen.open(temp, this.amount, true, String.format(TEXT[0], this.amount));
            tickDuration();
            return;
        }
        /*
            本来感觉应该和预见选牌一样啊
            但是战斗内的Action的update时机和战斗外的不同 战斗内的只有在!AbstractDungeon.isScreenUp的时候才update
            然而这个会一直在update 所以为了防止点了设置键之类的之后立刻进入下面的if 要多加几个判断
        */
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.GRID &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MASTER_DECK_VIEW &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.SETTINGS &&
                AbstractDungeon.screen != AbstractDungeon.CurrentScreen.MAP) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.action.accept(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            if (AbstractDungeon.player != null) {
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            AbstractDungeon.overlayMenu.cancelButton.hide();
            isDone = true;
        }
    }
}
