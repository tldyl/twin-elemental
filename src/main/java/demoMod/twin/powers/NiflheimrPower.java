package demoMod.twin.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.helpers.PowerRegionLoader;
import demoMod.twin.stances.Freeze;
import demoMod.twin.vfx.IceAttackEffect;

import java.util.stream.Collectors;

public class NiflheimrPower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("NiflheimrPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    private final boolean upgraded;

    public NiflheimrPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        PowerRegionLoader.load(this);
        this.upgraded = upgraded;
        if (upgraded) {
            this.ID += "+";
            this.name += "+";
        }
    }

    @Override
    public void updateDescription() {
        if (upgraded) {
            this.description = String.format(DESC[1], this.amount);
        } else {
            this.description = DESC[0];
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                if (mo.hasPower(WeakPower.POWER_ID)) {
                    int weakAmount = mo.getPower(WeakPower.POWER_ID).amount;
                    if (weakAmount > 0) {
                        addToBot(new SFXAction("TWIN_FREEZE_" + MathUtils.random(1, 3)));
                        if (!mo.isDeadOrEscaped()) {
                            addToBot(new VFXAction(p, new IceAttackEffect(mo, 0.6F), 0.0F));
                        }
                        addToBot(new AbstractGameAction() {
                            {duration = 0.4F;}

                            @Override
                            public void update() {
                                tickDuration();
                            }
                        });
                        addToBot(new LoseHPAction(mo, p, weakAmount));
                        AbstractGameAction sfxAction = new SFXAction("TWIN_FREEZE_SHATTER_" + MathUtils.random(1, 3));
                        sfxAction.actionType = AbstractGameAction.ActionType.DAMAGE;
                        addToBot(sfxAction);
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
            }
        }
    }

    @Override
    public void onChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (oldStance != newStance && newStance instanceof Freeze && upgraded) {
            this.flash();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                addToBot(new ApplyPowerAction(m, owner, new WeakPower(m, this.amount, owner instanceof AbstractMonster)));
            }
        }
    }
}
