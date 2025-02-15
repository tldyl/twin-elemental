package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.AttackEffectEnum;
import demoMod.twin.powers.RedMoonPower;

public class RedMoon extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("RedMoon");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/RedMoon";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public RedMoon() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 5;
        this.baseMagicNumber = this.magicNumber = 5;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(2);
            upgradeMagicNumber(2);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffectEnum.RED_MOON));
        addToBot(new ApplyPowerAction(m, p, new RedMoonPower(m, this.magicNumber)));
    }
}
