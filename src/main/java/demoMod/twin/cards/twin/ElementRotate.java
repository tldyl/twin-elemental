package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class ElementRotate extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("ElementRotate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/ElementRotate";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public ElementRotate() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
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
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        if (p.stance instanceof Blaze) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber)));
        } else {
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, this.magicNumber)));
        }
        addToBot(new SwitchLeaderAction());
    }
}
