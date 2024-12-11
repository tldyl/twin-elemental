package demoMod.twin.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.helpers.PowerRegionLoader;
import demoMod.twin.ui.DaylightCyclePanel;

public class DaylightCyclePower extends TwoAmountPower {
    public static final String POWER_ID = TwinElementalMod.makeID("DaylightCyclePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public static boolean isDay = true;

    public DaylightCyclePower(AbstractCreature owner, int amount) {
        this.name = DESC[2];
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = 12;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void onInitialApplication() {
        DaylightCyclePanel.targetAngle = 0.0F;
        isDay = true;
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        DaylightCyclePanel.update();
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        DaylightCyclePanel.render(sb);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[isDay ? 0 : 1], this.amount);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        this.amount2--;
        if (amount2 == 0) {
            this.amount2 = 12;
            this.playApplyPowerSfx();
            isDay = !isDay;
            this.name = DESC[isDay ? 2 : 3];
        }
        this.updateDescription();
        DaylightCyclePanel.onAfterUseCard(card, action);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL && isDay ? damage + (float)this.amount : damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        if (isDay) {
            return blockAmount;
        }
        return (blockAmount += (float)this.amount) < 0.0F ? 0.0F : blockAmount;
    }
}
