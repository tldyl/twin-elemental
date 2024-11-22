package demoMod.twin.cards.twin;

import basemod.abstracts.CustomSavable;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monopoly extends AbstractTwinCard implements CustomSavable<Map<String, List<Integer>>> {
    public static final String ID = TwinElementalMod.makeID("Monopoly");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Monopoly";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public Map<String, List<Integer>> cardSave = new HashMap<>();

    public Monopoly() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.handCardSelectScreen.selectedCards.clear();
        addToBot(new ExhaustAction(this.magicNumber, false, true, true));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard card : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    if (!cardSave.containsKey(card.cardID)) {
                        cardSave.put(card.cardID, new ArrayList<>());
                    }
                    cardSave.get(card.cardID).add(card.timesUpgraded);
                }
                for (AbstractCard card : p.masterDeck.group) {
                    if (card.uuid.equals(uuid) && card instanceof Monopoly) {
                        Monopoly monopoly = (Monopoly) card;
                        monopoly.cardSave.putAll(cardSave);
                        for (Map.Entry<String, List<Integer>> e : cardSave.entrySet()) {
                            AbstractCard cardsToPreview = CardLibrary.getCard(e.getKey());
                            if (cardsToPreview == null) {
                                cardsToPreview = new Madness();
                            }
                            MultiCardPreview.add(monopoly, cardsToPreview.makeCopy());
                            break;
                        }
                        break;
                    }
                }
                isDone = true;
            }
        });
    }

    @Override
    public Map<String, List<Integer>> onSave() {
        return cardSave;
    }

    @Override
    public void onLoad(Map<String, List<Integer>> cardIds) {
        if (cardIds != null) {
            cardSave = cardIds;
            for (Map.Entry<String, List<Integer>> e : cardIds.entrySet()) {
                AbstractCard cardsToPreview = CardLibrary.getCard(e.getKey());
                if (cardsToPreview == null) {
                    cardsToPreview = new Madness();
                }
                MultiCardPreview.add(this, cardsToPreview.makeCopy());
                break;
            }
        } else {
            cardSave = new HashMap<>();
        }
    }
}
