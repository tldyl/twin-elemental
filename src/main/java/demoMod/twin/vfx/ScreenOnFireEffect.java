package demoMod.twin.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;

public class ScreenOnFireEffect extends AbstractGameEffect {
    private float timer = 0.0F;

    public ScreenOnFireEffect() {
        this.duration = 3.0F;
        this.startingDuration = this.duration;
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play("GHOST_FLAMES");
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.PURPLE));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            AbstractDungeon.effectsQueue.add(getPurpleFireEffect());
            this.timer = 0.05F;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    private AbstractGameEffect getPurpleFireEffect() {
        AbstractGameEffect effect = new GiantFireEffect();
        Color fireColor = Color.WHITE.cpy();
        fireColor.a = 0.0F;
        fireColor.r -= MathUtils.random(0.2F);
        fireColor.g = 0.1F;
        fireColor.b -= fireColor.r - MathUtils.random(0.0F, 0.2F);
        ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "color", fireColor);
        return effect;
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    @Override
    public void dispose() {
    }
}