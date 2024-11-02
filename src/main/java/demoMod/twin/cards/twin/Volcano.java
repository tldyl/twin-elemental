package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.BoostAction;
import demoMod.twin.actions.XCostAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class Volcano extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Volcano");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = -1;

    public Volcano() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.tags.add(CardTagsEnum.DOMAIN);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!(p.stance instanceof Blaze)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        return super.canUse(p, m) && p.stance instanceof Blaze;
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
        addToBot(new XCostAction(this, effect -> {
            for (int i=0;i<effect;i++) {
                addToBot(new BoostAction());
            }
        }, this.energyOnUse + (upgraded ? 1 : 0)));
    }
}
