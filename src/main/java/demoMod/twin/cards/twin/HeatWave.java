package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.vfx.HeatWaveEffect;

public class HeatWave extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("HeatWave");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/HeatWave";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    public HeatWave() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTagsEnum.COPORATE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(3);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(p, new HeatWaveEffect(), 1.2F));
        CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.8F);
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mo, p, new VulnerablePower(mo, this.magicNumber, false)));
            }
        }
        resetCoporateState();
    }
}
