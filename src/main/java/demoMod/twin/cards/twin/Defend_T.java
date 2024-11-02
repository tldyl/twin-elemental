package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

public class Defend_T extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Defend_T");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Defend_T";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public Defend_T() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 4;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTags.STARTER_DEFEND);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.stance instanceof Freeze) {
            this.baseBlock += this.magicNumber;
        }
        super.applyPowers();
        if (AbstractDungeon.player.stance instanceof Freeze) {
            this.baseBlock -= this.magicNumber;
        }
        isBlockModified = this.baseBlock != this.block;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.stance instanceof Freeze) {
            this.baseBlock += this.magicNumber;
        }
        super.calculateCardDamage(mo);
        if (AbstractDungeon.player.stance instanceof Freeze) {
            this.baseBlock -= this.magicNumber;
        }
        isBlockModified = this.baseBlock != this.block;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeBlock(2);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
    }
}
