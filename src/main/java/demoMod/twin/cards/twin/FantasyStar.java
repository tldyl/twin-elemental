package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SelectCardInCardGroupAction;
import demoMod.twin.enums.CardTagsEnum;

public class FantasyStar extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FantasyStar");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public FantasyStar() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 8;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeBlock(4);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        tmpGroup.group.addAll(p.drawPile.group);
        tmpGroup.group.addAll(p.discardPile.group);
        addToBot(new SelectCardInCardGroupAction(this.magicNumber, card -> card.hasTag(CardTagsEnum.DOMAIN), card -> {
            if (p.drawPile.contains(card)) {
                p.drawPile.removeCard(card);
            } else {
                p.discardPile.removeCard(card);
            }
            p.hand.addToHand(card);
        }, tmpGroup));
    }
}
