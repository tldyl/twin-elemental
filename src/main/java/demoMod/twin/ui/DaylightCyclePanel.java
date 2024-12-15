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
    private static final Gear[] gears = new Gear[10];

    private static float angle;
    public static float targetAngle;
    private static Color color;

    public static void init() {
        bg = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/bg.png"));
        hand = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/hand.png"));
        day = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/day.png"));
        night = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/night.png"));
        ring = new Texture(TwinElementalMod.getResourcePath("vfx/daylightCycle/ring.png"));
        gears[0] = new Gear(10, 425, 209, 1);
        gears[1] = new Gear(9, 99, 281, -1);
        gears[2] = new Gear(8, 198, 376, 1.5f);
        gears[3] = new Gear(7, 407, 305, -1.5f);
        gears[4] = new Gear(6, 333, 174, 0.6f);
        gears[5] = new Gear(5, 334, 413, -1.5f);
        gears[6] = new Gear(4, 122, 395, 2);
        gears[7] = new Gear(3, 100, 329, 1.5f);
        gears[8] = new Gear(2, 93, 217, -1);
        gears[9] = new Gear(1, 155, 102, 3);
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
        for (Gear gear : gears) {
            gear.render(sb);
        }
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
        sb.draw(bg, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                0, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
        sb.draw(hand, Settings.WIDTH / 2.0F - bg.getWidth() / 2.0F, Settings.HEIGHT * 0.75F - bg.getHeight() / 2.0F,
                bg.getWidth() / 2.0F, bg.getHeight() / 2.0F,
                bg.getWidth(), bg.getHeight(),
                Settings.scale, Settings.scale,
                angle, 0, 0,
                bg.getWidth(), bg.getHeight(),
                false, false);
    }

    static class Gear {
        private final Texture img;
        private final float centerX;
        private final float centerY;
        private final float speed;

        public Gear(int imgIndex, float centerX, float centerY, float speed) {
            this.img = new Texture(TwinElementalMod.getResourcePath(String.format("vfx/daylightCycle/gear%d.png", imgIndex)));
            this.centerX = centerX;
            this.centerY = img.getHeight() - centerY;
            this.speed = speed;
        }

        public void render(SpriteBatch sb) {
            sb.draw(img,
                    Settings.WIDTH / 2.0F - (img.getWidth() / 2f - centerX) * Settings.scale - centerX,
                    Settings.HEIGHT * 0.75F - (img.getHeight() / 2f - centerY) * Settings.scale - centerY,
                    centerX, centerY,
                    img.getWidth(), img.getHeight(),
                    Settings.scale, Settings.scale,
                    angle * speed,
                    0, 0,
                    img.getWidth(), img.getHeight(),
                    false, false);
        }
    }
}
