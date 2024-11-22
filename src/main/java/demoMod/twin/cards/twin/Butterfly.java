package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;

public class Butterfly extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Butterfly");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Butterfly";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public Butterfly() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 4;
        this.exhaust = true;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int drawAmount = this.magicNumber;
        int energyAmount = upgraded ? 4 : 3;
        drawAmount -= p.hand.size() - 1;
        energyAmount -= p.hand.size() - 1;
        if (drawAmount < 1) drawAmount = 1;
        if (energyAmount < 1) energyAmount = 1;
        addToBot(new DrawCardAction(drawAmount));
        addToBot(new GainEnergyAction(energyAmount));
    }
}
