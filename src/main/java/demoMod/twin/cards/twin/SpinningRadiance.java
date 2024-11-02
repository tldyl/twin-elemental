package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.PlayCardInCardGroupAction;
import demoMod.twin.enums.CardTagsEnum;

public class SpinningRadiance extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("SpinningRadiance");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 3;

    public SpinningRadiance() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeBaseCost(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.hand.size() > 0) {
            addToBot(new DiscardAction(p, p, p.hand.size(), true));
            addToBot(new DrawCardAction(p.hand.size(), new AbstractGameAction() {
                @Override
                public void update() {
                    DrawCardAction.drawnCards.forEach(c -> {
                        if (c.hasTag(CardTagsEnum.DOMAIN)) {
                            addToTop(new PlayCardInCardGroupAction(c, p.hand, AbstractDungeon.getRandomMonster(), null));
                        }
                    });
                    isDone = true;
                }
            }));
        }
    }
}
