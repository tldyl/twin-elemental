package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class WolfPath extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("WolfPath");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/WolfPath";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public WolfPath() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.baseDamage = 8;
        this.baseBlock = 8;
        this.baseMagicNumber = this.magicNumber = 2;
        this.returnToHand = true;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(2);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.target = AbstractDungeon.player.stance instanceof Blaze ? CardTarget.ENEMY : CardTarget.SELF;
    }

    @Override
    public void triggerWhenDrawn() {
        this.target = AbstractDungeon.player.stance instanceof Blaze ? CardTarget.ENEMY : CardTarget.SELF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Blaze) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            this.baseBlock += this.magicNumber;
        } else {
            addToBot(new GainBlockAction(p, p, this.block));
            this.baseDamage += this.magicNumber;
        }
        this.cost = 1;
        if (this.cost != this.costForTurn) {
            isCostModified = true;
        }
        this.costForTurn = 1;
    }
}
