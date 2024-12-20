package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class VictoryFlameParticleEffect extends AbstractGameEffect {
    private static final Texture[] images = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_flame_1.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_flame_2.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_flame_3.png"))
    };

    private final Texture img;
    private float x;
    private float y;
    private final float dv;
    private final float drv;

    public VictoryFlameParticleEffect() {
        this.img = images[MathUtils.random(images.length - 1)];
        x = MathUtils.random(-Settings.WIDTH * 0.3F, Settings.WIDTH * 0.9F);
        y = MathUtils.random(-Settings.HEIGHT * 0.3F, -Settings.HEIGHT * 0.2F);
        dv = MathUtils.random(75.0F, 175.0F) * Settings.scale;
        drv = MathUtils.random(-15.0F, 15.0F);
        scale = Settings.scale * MathUtils.random(0.1F, 0.3F);
        duration = startingDuration = MathUtils.random(5.0F, 10.0F);
        color = Color.WHITE.cpy();
    }

    @Override
    public void update() {
        x += dv * Gdx.graphics.getDeltaTime();
        y += dv * Gdx.graphics.getDeltaTime();
        rotation += drv * Gdx.graphics.getDeltaTime();
        this.color.a = Interpolation.fade.apply(0.0F, 0.9F, this.duration / this.startingDuration);

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img, x - img.getWidth() / 2.0F, y - img.getHeight() / 2.0F,
                img.getWidth() / 2.0F, img.getHeight() / 2.0F,
                img.getWidth(), img.getHeight(),
                scale * MathUtils.random(0.99F, 1.01F), scale * MathUtils.random(0.99F, 1.01F),
                rotation,
                0, 0,
                img.getWidth(), img.getHeight(),
                false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {

    }
}
