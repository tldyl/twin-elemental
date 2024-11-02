package demoMod.twin.relics;

import basemod.Pair;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import demoMod.twin.TwinElementalMod;

public class DistantSavior extends CustomRelic implements CustomSavable<Pair<String, Integer>> {
    public static final String ID = TwinElementalMod.makeID("DistantSavior");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/distantSavior.png"));

    private boolean cardSelected = true;
    private Pair<String, Integer> cardSave;

    public DistantSavior() {
        super(ID, IMG, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (cardSave != null && this.counter < 3) {
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractCard card = CardLibrary.getCard(cardSave.getKey());
            if (card == null) {
                card = new Madness();
            }
            for (int i=0;i<cardSave.getValue();i++) {
                card.upgrade();
            }
            addToBot(new MakeTempCardInHandAction(card));
            this.counter++;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public void onEquip() {
        this.cardSelected = false;
        if (AbstractDungeon.player.masterDeck.size() > 0) {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, this.DESCRIPTIONS[1], false, false, false, false);
        }
    }

    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            cardSave = new Pair<>(card.cardID, card.timesUpgraded);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            this.initializeTips();
        }
    }

    public void setDescriptionAfterLoading() {
        AbstractCard card = CardLibrary.getCard(cardSave.getKey());
        if (card == null) {
            return;
        }
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public Pair<String, Integer> onSave() {
        return cardSave;
    }

    @Override
    public void onLoad(Pair<String, Integer> save) {
        cardSelected = true;
        if (save != null) {
            this.cardSave = save;
            setDescriptionAfterLoading();
        }
    }
}
