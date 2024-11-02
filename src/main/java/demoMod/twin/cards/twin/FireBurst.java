package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class FireBurst extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FireBurst");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 5;

    public FireBurst() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 5;
        this.selfRetain = true;
        this.tags.add(CardTagsEnum.COPORATE);
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
        return () -> upgradeMagicNumber(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.damage >= 50) {
            addToBot(new VFXAction(p, new ScreenOnFireEffect(), 0.5F));
        }
        for (AbstractMonster m1 : AbstractDungeon.getMonsters().monsters) {
            if (!m1.isDeadOrEscaped()) {
                addToBot(new VFXAction(new ExplosionSmallEffect(m1.hb.cX, m1.hb.cY), 0.1F));
            }
        }
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
        resetCoporateState();
    }

    private int getAttacksPlayedThisCombat() {
        return (int) AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().filter(card -> card.type == CardType.ATTACK).count();
    }

    @Override
    public void applyPowers() {
        this.baseDamage = getAttacksPlayedThisCombat() * this.magicNumber;
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }
}
