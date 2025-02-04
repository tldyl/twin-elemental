package demoMod.twin.cards.twin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.vfx.IceAttackEffect;

import java.util.stream.Collectors;

public class FrozenWind extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FrozenWind");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/FrozenWind";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public FrozenWind() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int weakAmount = 0;
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                    if (mo.hasPower(WeakPower.POWER_ID)) {
                        weakAmount += mo.getPower(WeakPower.POWER_ID).amount;
                    }
                }
                if (weakAmount > 0) {
                    addToBot(new SFXAction("TWIN_FREEZE_" + MathUtils.random(1, 3)));
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                        if (!mo.isDeadOrEscaped()) {
                            addToBot(new VFXAction(p, new IceAttackEffect(mo, 0.6F), 0.0F));
                        }
                    }
                    addToBot(new AbstractGameAction() {
                        {duration = 0.4F;}

                        @Override
                        public void update() {
                            tickDuration();
                        }
                    });
                    addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(weakAmount * 2, true), DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
                    AbstractGameAction sfxAction = new SFXAction("TWIN_FREEZE_SHATTER_" + MathUtils.random(1, 3));
                    sfxAction.actionType = AbstractGameAction.ActionType.DAMAGE;
                    addToBot(sfxAction);
                    for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                        if (!mo.isDeadOrEscaped()) {
                            mo.tint.changeColor(Color.BLUE.cpy(), 0.4F);
                            addToBot(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    mo.tint.changeColor(Color.WHITE.cpy(), 40.0F);
                                    isDone = true;
                                }
                            });
                        }
                    }
                }
                isDone = true;
            }
        });
    }
}
