package demoMod.twin.cards.twin;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FrozenSpear extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FrozenSpear");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/FrozenSpear";
    private static final TextureAtlas.AtlasRegion spearRegion = new TextureAtlas.AtlasRegion(new Texture(TwinElementalMod.getResourcePath("vfx/frozenSpear.png")), 0, 0, 74, 312);

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 3;

    public FrozenSpear() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 3;
        this.baseMagicNumber = this.magicNumber = 3;
        this.tags.add(CardTagsEnum.COPORATE);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeDamage(1);
    }

    @Override
    public void applyPowers() {
        int weakAmount = 0;
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
            if (mo.hasPower(WeakPower.POWER_ID)) {
                weakAmount += mo.getPower(WeakPower.POWER_ID).amount;
            }
        }
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + String.format(cardStrings.EXTENDED_DESCRIPTION[0], weakAmount / 2);
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int weakAmount = 0;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
            if (m.hasPower(WeakPower.POWER_ID)) {
                weakAmount += m.getPower(WeakPower.POWER_ID).amount;
            }
        }
        super.calculateCardDamage(mo);
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription + String.format(cardStrings.EXTENDED_DESCRIPTION[0], weakAmount / 2);
        this.initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Freeze) {
            addToBot(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
            CardCrawlGame.sound.playA("ATTACK_HEAVY", -0.5F);
            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.SKY.cpy(), 0.4F));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(0.2F, 0.35F, 1.0F, 0.5F)));
            ReflectionHacks.setPrivateStatic(CollectorStakeEffect.class, "img", spearRegion);
            AtomicInteger count = new AtomicInteger(0);
            int weakAmount = 0;
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                if (mo.hasPower(WeakPower.POWER_ID)) {
                    weakAmount += mo.getPower(WeakPower.POWER_ID).amount;
                }
            }
            for (int i=0;i<this.magicNumber + weakAmount / 2;i++) {
                calculateCardDamage(m);
                int damage = this.damage;
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        TwinElementalMod.addParallel(new AbstractGameAction() {
                            private final AbstractGameEffect effect = new CollectorStakeEffect(m.hb.cX + MathUtils.random(-50.0F, 50.0F) * Settings.scale, m.hb.cY + MathUtils.random(-60.0F, 60.0F) * Settings.scale);
                            private boolean start = true;

                            @Override
                            public void update() {
                                if (start) {
                                    start = false;
                                    ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "color", new Color(MathUtils.random(0.0F, 0.1F), MathUtils.random(0.2F, 0.6F), MathUtils.random(0.7F, 1.0F), 0.0F));
                                    ReflectionHacks.setPrivate(effect, CollectorStakeEffect.class, "targetScale", 1.1F);
                                    AbstractDungeon.effectList.add(effect);
                                }
                                isDone = effect.isDone;
                                if (isDone) {
                                    count.incrementAndGet();
                                    addToTop(new DamageAction(m, new DamageInfo(p, damage, FrozenSpear.this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
                                    if (count.get() >= magicNumber) {
                                        ReflectionHacks.setPrivateStatic(CollectorStakeEffect.class, "img", ImageMaster.vfxAtlas.findRegion("combat/stake"));
                                    }
                                }
                            }
                        });
                        isDone = true;
                    }
                });
                addToBot(new AbstractGameAction() {
                    private boolean start = true;

                    @Override
                    public void update() {
                        if (start) {
                            start = false;
                            this.duration = 0.04F;
                        }
                        this.tickDuration();
                    }
                });
            }
        } else {
            if (m != null) {
                for (int i=0;i<this.magicNumber;i++) {
                    addToBot(new VFXAction(new ThrowDaggerEffect(m.hb.cX, m.hb.cY)));
                    addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
                }
            }
        }
        resetCoporateState();
    }
}
