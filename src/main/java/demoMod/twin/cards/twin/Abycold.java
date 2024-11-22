package demoMod.twin.cards.twin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.vfx.HeavySnowEffect;
import demoMod.twin.vfx.IceAttackEffect;

public class Abycold extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Abycold");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Abycold";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    public Abycold() {
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
        addToBot(new VFXAction(p, new HeavySnowEffect(p.flipHorizontal), 0.7F, true));
        addToBot(new SFXAction("TWIN_FREEZE_" + MathUtils.random(1, 3)));
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new VFXAction(p, new IceAttackEffect(mo, 0.9F), 0.0F));
            }
        }
        addToBot(new AbstractGameAction() {
            {duration = 0.7F;}

            @Override
            public void update() {
                tickDuration();
            }
        });
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        AbstractGameAction sfxAction = new SFXAction("TWIN_FREEZE_SHATTER_" + MathUtils.random(1, 3));
        sfxAction.actionType = AbstractGameAction.ActionType.DAMAGE;
        addToBot(sfxAction);
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                mo.tint.changeColor(Color.BLUE.cpy(), 0.7F);
                addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false)));
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        mo.tint.changeColor(Color.WHITE.cpy(), 40.0F);
                        isDone = true;
                    }
                });
            }
        }
        resetCoporateState();
    }
}
