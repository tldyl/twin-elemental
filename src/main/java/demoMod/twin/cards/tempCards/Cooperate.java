package demoMod.twin.cards.tempCards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.cards.twin.AbstractTwinCard;

public class Cooperate extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Cooperate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Cooperate";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public Cooperate() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.selfRetain = true;
        this.exhaust = true;
        this.color = CardColor.COLORLESS;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SwitchLeaderAction());
        if (this.upgraded) {
            addToBot(new DrawCardAction(1));
        }
    }
}
