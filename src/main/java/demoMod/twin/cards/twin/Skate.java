package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

public class Skate extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Skate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Skate";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public Skate() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 6;
        this.baseMagicNumber = this.magicNumber = 1;
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeBlock(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        if (p.stance instanceof Freeze) {
            addToBot(new GainEnergyAction(1));
            addToBot(new DrawCardAction(this.magicNumber));
            addToBot(new SwitchLeaderAction());
        }
    }
}
