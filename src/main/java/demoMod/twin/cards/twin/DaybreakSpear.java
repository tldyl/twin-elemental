package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;
import demoMod.twin.vfx.DaybreakSpearEffect;

public class DaybreakSpear extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("DaybreakSpear");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 3;

    public DaybreakSpear() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.selfRetain = true;
        this.tags.add(CardTagsEnum.COPORATE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.baseDamage = 30;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (costForTurn != 0) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        }
        return super.canUse(p, m) && costForTurn == 0;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeDamage(10);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Blaze) {
            addToBot(new VFXAction(new ScreenOnFireEffect(), 1.0F));
            if (m != null) {
                addToBot(new VFXAction(new DaybreakSpearEffect(m), 0.1F));
            }
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
        resetCoporateState();
    }
}
