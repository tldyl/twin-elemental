package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

public class Karma extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Karma");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public Karma() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 9;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public void upgradeName() {
        this.timesUpgraded++;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeBlock(3);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Freeze) {
            addToBot(new GainBlockAction(p, p, this.block));
        } else {
            addToBot(new DrawCardAction(this.magicNumber));
        }
        addToBot(new SwitchLeaderAction());
    }
}
