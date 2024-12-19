package demoMod.twin.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;
import demoMod.twin.TwinElementalMod;

public class DaybreakSpearEffect extends AbstractGameEffect {
    private static final Texture DAYBREAK_SPEAR_VFX = new Texture(TwinElementalMod.getResourcePath("vfx/daybreakSpear.png"));

    private final AbstractCreature target;

    public DaybreakSpearEffect(AbstractCreature target) {
        this.target = target;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playA("ATTACK_IRON_2", -0.4F);
        CardCrawlGame.sound.playA("ATTACK_HEAVY", -0.4F);
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(target.hb.cX + 60.0F * Settings.scale, target.hb.cY - 30.0F * Settings.scale, 2500.0F, -2500.0F, 225.0F, 8.0F, Color.GOLD, Color.GOLD) {
            private float tailTimer = 0.025F;
            private int tailCount = MathUtils.random(10, 16);

            @Override
            public void update() {
                super.update();
                tailTimer -= Gdx.graphics.getDeltaTime();
                if (tailTimer <= 0 && tailCount > 0) {
                    tailTimer = 0.025F;
                    tailCount--;
                    float x = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "x");
                    float y = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "y");
                    TwinElementalMod.addVfx(new DaybreakSpearTailEffect(x, y));
                }
            }

            @Override
            public void render(SpriteBatch sb) {
                float x = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "x");
                float y = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "y");
                float scaleX = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "scaleX");
                float scaleY = ReflectionHacks.getPrivate(this, AnimatedSlashEffect.class, "scaleY");
                sb.setColor(this.color);
                sb.setBlendFunction(770, 1);
                sb.draw(DAYBREAK_SPEAR_VFX, x, y, 64.0F, 64.0F, 128.0F, 128.0F, scaleX * 0.7F * MathUtils.random(0.95F, 1.05F) * Settings.scale, scaleY * MathUtils.random(0.95F, 1.05F) * Settings.scale, this.rotation, 0, 0, 128, 128, false, false);
                sb.setBlendFunction(770, 771);
            }
        });
        isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
