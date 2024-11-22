package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.stances.Blaze;
import demoMod.twin.vfx.HeatWaveEffect;

import java.util.function.Supplier;

public class HotDomain extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("HotDomain");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/HotDomain";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    public HotDomain() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 12;
        this.baseMagicNumber = this.magicNumber = 6;
        this.baseTraceAmount = this.traceAmount = 4;
        this.tags.add(CardTagsEnum.DOMAIN);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!(p.stance instanceof Blaze)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        return super.canUse(p, m) && p.stance instanceof Blaze;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(4);
            upgradeMagicNumber(2);
        };
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> addToBot(new DamageAllEnemiesAction(p, this.magicNumber, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.FIRE)), cardStrings.EXTENDED_DESCRIPTION[0], this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(p, new HeatWaveEffect(), 1.2F));
        CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.8F);
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
