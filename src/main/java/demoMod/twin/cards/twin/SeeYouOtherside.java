package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;

public class SeeYouOtherside extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("SeeYouOtherside");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SeeYouOtherside";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public SeeYouOtherside() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 4;
        this.baseMagicNumber = 4;
        this.tags.add(CardTagsEnum.COPORATE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeBlock(2);
            upgradeMagicNumber(2);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        if (costForTurn == 0) {
            this.baseBlock += this.magicNumber;
        }
        resetCoporateState();
    }
}
