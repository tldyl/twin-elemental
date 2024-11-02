package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.powers.RedMoonPower;
import demoMod.twin.stances.Blaze;

public class FireWheel extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("FireWheel");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 3;

    public FireWheel() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 6;
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(CardTagsEnum.COPORATE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
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
        if (p.stance instanceof Blaze) {
            addToBot(new VFXAction(p, new FlameBarrierEffect(p.hb.cX, p.hb.cY), 0.3F));
        }
        for (int i=0;i<3;i++) {
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    AbstractMonster mo = AbstractDungeon.getRandomMonster();
                    if (mo != null) {
                        calculateCardDamage(mo);
                        AbstractGameEffect effect = new FireballEffect(p.hb.cX, p.hb.cY, mo.hb.cX, mo.hb.cY);
                        effect.duration = 0.5F;
                        effect.startingDuration = 0.5F;
                        if (p.stance instanceof Blaze) {
                            addToTop(new ApplyPowerAction(mo, p, new RedMoonPower(mo, FireWheel.this.magicNumber)));
                        }
                        addToTop(new ApplyPowerAction(mo, p, new VulnerablePower(mo, 1, false)));
                        addToTop(new DamageAction(mo, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.FIRE));
                        if (p.stance instanceof Blaze) {
                            addToTop(new SFXAction("GHOST_ORB_IGNITE_1", 0.3F));
                        }
                        addToTop(new VFXAction(p, effect, 0.1F));
                    }
                    isDone = true;
                }
            });
        }
        resetCoporateState();
    }
}
