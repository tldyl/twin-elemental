package demoMod.twin.cards.twin;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.AbstractCardEnum;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.powers.TracePower;
import demoMod.twin.stances.Blaze;
import demoMod.twin.stances.Freeze;

import java.util.Locale;
import java.util.function.Supplier;

public abstract class AbstractTwinCard extends CustomCard {
    protected int coporateCostReduced;
    public int traceAmount;
    public int baseTraceAmount;
    private static final Texture DESCRIPTION_ORB_BLAZE = new Texture(TwinElementalMod.getResourcePath("512/card_small_blaze_orb.png"));
    private static final Texture DESCRIPTION_ORB_FREEZE = new Texture(TwinElementalMod.getResourcePath("512/card_small_freeze_orb.png"));

    public AbstractTwinCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, AbstractCardEnum.TWIN, rarity, target);
        this.isMultiDamage = type == CardType.ATTACK && target == CardTarget.ALL_ENEMY;
        updateBackground();
    }

    @Override
    public void loadCardImage(String img) {
        img += "_" + this.type.name().toLowerCase(Locale.ROOT) + ".png";

        this.textureImg = img;
        super.loadCardImage(img);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(TracePower.POWER_ID)) {
            AbstractPower power = p.getPower(TracePower.POWER_ID);
            traceAmount = baseTraceAmount + power.amount;
        } else {
            traceAmount = baseTraceAmount;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        AbstractPlayer p = AbstractDungeon.player;
        if (p.hasPower(TracePower.POWER_ID)) {
            AbstractPower power = p.getPower(TracePower.POWER_ID);
            traceAmount = baseTraceAmount + power.amount;
        } else {
            traceAmount = baseTraceAmount;
        }
    }

    public abstract Runnable getUpgradeAction();

    public Supplier<AbstractPower> getDomainEffect() {
        return null;
    }

    public void upgradeTraceAmount(int amount) {
        this.baseTraceAmount += amount;
        this.traceAmount = this.baseTraceAmount;
    }

    protected Supplier<Boolean> getGlowCondition() {
        return () -> false;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (getGlowCondition().get()) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            getUpgradeAction().run();
        }
    }

    public void updateBackground() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null) {
            if (p.stance instanceof Blaze && hasTag(CardTagsEnum.PREFER_BLAZE)) {
                this.setBackgroundTexture(TwinElementalMod.getResourcePath(String.format("512/bg_%s_blaze.png", type.name().toLowerCase(Locale.ROOT))), TwinElementalMod.getResourcePath(String.format("1024/bg_%s_blaze.png", type.name().toLowerCase(Locale.ROOT))));
                this.setOrbTexture(TwinElementalMod.getResourcePath("512/card_blaze_orb.png"), TwinElementalMod.getResourcePath("1024/card_blaze_orb.png"));
            } else if (p.stance instanceof Freeze && hasTag(CardTagsEnum.PREFER_FREEZE)) {
                this.setBackgroundTexture(TwinElementalMod.getResourcePath(String.format("512/bg_%s_freeze.png", type.name().toLowerCase(Locale.ROOT))), TwinElementalMod.getResourcePath(String.format("1024/bg_%s_freeze.png", type.name().toLowerCase(Locale.ROOT))));
                this.setOrbTexture(TwinElementalMod.getResourcePath("512/card_freeze_orb.png"), TwinElementalMod.getResourcePath("1024/card_freeze_orb.png"));
            } else {
                this.setBackgroundTexture(TwinElementalMod.getResourcePath(String.format("512/bg_%s_twin.png", type.name().toLowerCase(Locale.ROOT))), TwinElementalMod.getResourcePath(String.format("1024/bg_%s_twin.png", type.name().toLowerCase(Locale.ROOT))));
                this.setOrbTexture(TwinElementalMod.getResourcePath("512/card_icebreaker_orb.png"), TwinElementalMod.getResourcePath("1024/card_icebreaker_orb.png"));
            }
        }
    }

    @Override
    public void renderSmallEnergy(SpriteBatch sb, TextureAtlas.AtlasRegion region, float x, float y) {
        sb.setColor(ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor"));
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null) {
            if (p.stance instanceof Blaze && hasTag(CardTagsEnum.PREFER_BLAZE)) {
                sb.draw(DESCRIPTION_ORB_BLAZE, this.current_x + x * Settings.scale * this.drawScale + region.offsetX * Settings.scale, this.current_y + y * Settings.scale * this.drawScale - 28.0F * Settings.scale, 0.0F, 0.0F, (float)region.packedWidth, (float)region.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
            } else if (p.stance instanceof Freeze && hasTag(CardTagsEnum.PREFER_FREEZE)) {
                sb.draw(DESCRIPTION_ORB_FREEZE, this.current_x + x * Settings.scale * this.drawScale + region.offsetX * Settings.scale, this.current_y + y * Settings.scale * this.drawScale - 28.0F * Settings.scale, 0.0F, 0.0F, (float)region.packedWidth, (float)region.packedHeight, this.drawScale * Settings.scale, this.drawScale * Settings.scale, 0.0F, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
            } else {
                super.renderSmallEnergy(sb, region, x, y);
            }
            return;
        }
        super.renderSmallEnergy(sb, region, x, y);
    }

    @Override
    public abstract void use(AbstractPlayer p, AbstractMonster m);

    @Override
    public void triggerWhenDrawn() {
        if (hasTag(CardTagsEnum.COPORATE) && coporateCostReduced > 0) {
            setCostForTurn(cost - coporateCostReduced);
        }
    }

    @Override
    public void onRetained() {
        triggerWhenDrawn();
    }

    public void checkCoporateState() {
        if (hasTag(CardTagsEnum.COPORATE)) {
            coporateCostReduced++;
            setCostForTurn(costForTurn - 1);
        }
    }

    public void resetCoporateState() {
        if (hasTag(CardTagsEnum.COPORATE)) {
            coporateCostReduced = 0;
        }
    }
}
