package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

public class Icicle extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Icicle");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Icicle() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.baseBlock = 7;
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(2);
            upgradeBlock(2);
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Freeze) {
            addToTop(new GainBlockAction(p, p, this.block));
        }
        addToTop(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        if (m != null) {
            addToTop(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
        }
    }
}
