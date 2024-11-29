package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.BoostAction;
import demoMod.twin.powers.LoseTracePower;
import demoMod.twin.powers.TracePower;
import demoMod.twin.stances.Blaze;

public class MagicBlock extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("MagicBlock");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/MagicBlock";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public MagicBlock() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 8;
        this.baseMagicNumber = this.magicNumber = 2;
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
        addToBot(new GainBlockAction(p, p, this.block));
        if (p.stance instanceof Blaze) {
            for (int i=0;i<this.magicNumber;i++) {
                addToBot(new BoostAction());
            }
        } else {
            addToBot(new ApplyPowerAction(p, p, new TracePower(p, this.magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseTracePower(p, this.magicNumber)));
        }
    }
}
