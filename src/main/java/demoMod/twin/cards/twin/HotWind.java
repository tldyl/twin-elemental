package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class HotWind extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("HotWind");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/HotWind";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 3;

    public HotWind() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 16;
        this.baseMagicNumber = this.magicNumber = 3;
        this.tags.add(CardTagsEnum.COPORATE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(5);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + String.format(cardStrings.EXTENDED_DESCRIPTION[0], Blaze.switchedTimesThisTurn);
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + String.format(cardStrings.EXTENDED_DESCRIPTION[0], Blaze.switchedTimesThisTurn);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        if (Blaze.switchedTimesThisTurn > 0 && p.stance instanceof Blaze) {
            addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber * Blaze.switchedTimesThisTurn)));
        }
        resetCoporateState();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }
}
