package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class SpinningRadianceEffect extends AbstractGameEffect {
    public SpinningRadianceEffect() {
        duration = 0.5F;
    }

    @Override
    public void update() {
        AbstractDungeon.effectList.add(new SpinningRadianceParticleEffect(MathUtils.random(0, 3)));
        if (duration == 0.5F) {
            CardCrawlGame.sound.playA("INTIMIDATE", -0.6F);
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.GOLD.cpy()));
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0) {
            isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
