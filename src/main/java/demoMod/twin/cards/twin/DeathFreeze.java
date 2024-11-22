package demoMod.twin.cards.twin;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.stances.Freeze;
import demoMod.twin.vfx.IceAttackEffect;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DeathFreeze extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("DeathFreeze");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/DeathFreeze";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    public DeathFreeze() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 2;
        this.baseTraceAmount = this.traceAmount = 3;
        this.tags.add(CardTagsEnum.DOMAIN);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!(p.stance instanceof Freeze)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        return super.canUse(p, m) && p.stance instanceof Freeze;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeTraceAmount(1);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> {
            int weakAmount = 0;
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                if (mo.hasPower(WeakPower.POWER_ID)) {
                    weakAmount += mo.getPower(WeakPower.POWER_ID).amount;
                }
            }
            if (weakAmount > 0) {
                addToBot(new GainBlockAction(p, p, weakAmount));
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
                addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(weakAmount, true), DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
                AbstractGameAction sfxAction = new SFXAction("TWIN_FREEZE_SHATTER_" + MathUtils.random(1, 3));
                sfxAction.actionType = AbstractGameAction.ActionType.DAMAGE;
                addToBot(sfxAction);
                for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                    if (!mo.isDeadOrEscaped()) {
                        mo.tint.changeColor(Color.BLUE.cpy(), 0.7F);
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
        }, cardStrings.EXTENDED_DESCRIPTION[0], -1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
            addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, this.magicNumber, false)));
        }
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
