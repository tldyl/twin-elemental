package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.stances.Blaze;

public class Maplooming extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Maplooming");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public Maplooming() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 6;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (Blaze.switchedTimesThisTurn < this.magicNumber) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        return super.canUse(p ,m) && Blaze.switchedTimesThisTurn >= this.magicNumber;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = DESCRIPTION + String.format(cardStrings.EXTENDED_DESCRIPTION[0], Blaze.switchedTimesThisTurn);
        this.initializeDescription();
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(-1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
    }
}
