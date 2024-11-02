package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class Strike_T extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Strike_T");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Strike_T";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Strike_T() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 5;
        this.damage = this.baseDamage;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTags.STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public void applyPowers() {
        if (AbstractDungeon.player.stance instanceof Blaze) {
            this.baseDamage += this.magicNumber;
        }
        super.applyPowers();
        if (AbstractDungeon.player.stance instanceof Blaze) {
            this.baseDamage -= this.magicNumber;
        }
        isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (AbstractDungeon.player.stance instanceof Blaze) {
            this.baseDamage += this.magicNumber;
        }
        super.calculateCardDamage(mo);
        if (AbstractDungeon.player.stance instanceof Blaze) {
            this.baseDamage -= this.magicNumber;
        }
        isDamageModified = this.baseDamage != this.damage;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(2);
            upgradeMagicNumber(1);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }
}
