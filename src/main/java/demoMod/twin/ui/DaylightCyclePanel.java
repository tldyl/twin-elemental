package demoMod.twin.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.powers.DaylightCyclePower;

public class DaylightCyclePanel {
    private static Texture bg;
    private static Texture hand;
    private static Texture day;
    private static Texture night;
    private static Texture ring;
    private static Texture gear1;
    private static Texture gear2;

    private static float angle;
    public static float targetAngle;
    private static Color color;

    public static void init() {
        bg = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/bg.png"));
        hand = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/hand.png"));
        day = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/day.png"));
        night = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/night.png"));
        ring = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/ring.png"));
        gear1 = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/gear1.png"));
        gear2 = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/gear2.png"));
        color = Color.WHITE.cpy();
    }

    public static void onAfterUseCard(AbstractCard card, UseCardAction action) {
        targetAngle -= 30.0F;
    }

    public static void update() {
        angle = MathHelper.orbLerpSnap(angle, targetAngle);
    }

    public static void render(SpriteBatch sb) {
        if (!AbstractDungeon.player.hasPower(DaylightCyclePower.POWER_ID)) {
            return;
        }
        color.a = 1.0F;
        sb.setColor(color);
        sb.draw(bg, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        sb.draw(gear2, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        sb.draw(gear1, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        sb.draw(ring, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        color.a = 1.0F - MathUtils.cosDeg(angle / 2.0F);
        color.a /= 2.0F;
        sb.setColor(color);
        sb.draw(night, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        color.a = 1.0F - color.a;
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(day, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        sb.setBlendFunction(770, 771);
        color.a = 1.0F;
        sb.setColor(color);
        sb.draw(hand, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                angle, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
    }
}
