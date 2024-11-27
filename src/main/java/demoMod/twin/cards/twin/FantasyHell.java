package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.vfx.ScreenOnFireEffect;

import java.util.function.Supplier;

public class FantasyHell extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FantasyHell");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/FantasyHell";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 3;

    public FantasyHell() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 3;
        this.baseMagicNumber = this.magicNumber = 6;
        this.baseTraceAmount = this.traceAmount = 3;
        this.tags.add(CardTagsEnum.DOMAIN);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(1);
            upgradeTraceAmount(1);
        };
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> addToBot(new GainEnergyAction(1)), cardStrings.EXTENDED_DESCRIPTION[0], 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new ScreenOnFireEffect(), 1.0F));
        for (int i=0;i<this.magicNumber;i++) {
            addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE, true));
        }
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
