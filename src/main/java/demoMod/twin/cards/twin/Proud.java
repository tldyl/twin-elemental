package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import demoMod.twin.TwinElementalMod;

public class Proud extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Proud");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Proud";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public Proud() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeBaseCost(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber, new AbstractGameAction() {
            {duration = Settings.ACTION_DUR_XFAST;}

            @Override
            public void update() {
                if (duration == Settings.ACTION_DUR_XFAST) {
                    if (p.hand.isEmpty()) {
                        this.isDone = true;
                    } else if (p.hand.size() == 1) {
                        AbstractCard tmpCard = p.hand.getBottomCard();

                        this.addToBot(new NewQueueCardAction(tmpCard, true, false, true));

                        for (int i = 0; i < 1; i++) {
                            AbstractCard tmp = tmpCard.makeSameInstanceOf();
                            tmp.purgeOnUse = true;
                            this.addToBot(new NewQueueCardAction(tmp, true, false, true));
                        }

                        this.isDone = true;
                    } else {
                        AbstractDungeon.handCardSelectScreen.open(cardStrings.EXTENDED_DESCRIPTION[0], 1, false, false);
                        this.tickDuration();
                    }
                } else {
                    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                        AbstractCard tmpCard = AbstractDungeon.handCardSelectScreen.selectedCards.getBottomCard();

                        AbstractDungeon.getCurrRoom().souls.remove(tmpCard);
                        this.addToBot(new NewQueueCardAction(tmpCard, true, false, true));

                        for (int i = 0; i < 1; i++) {
                            AbstractCard tmp = tmpCard.makeSameInstanceOf();
                            tmp.purgeOnUse = true;
                            this.addToBot(new NewQueueCardAction(tmp, true, false, true));
                        }

                        AbstractDungeon.handCardSelectScreen.selectedCards.clear();
                        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                    }

                    this.tickDuration();
                }
            }
        }));
        addToBot(new ApplyPowerAction(p, p, new NoDrawPower(p)));
    }
}
