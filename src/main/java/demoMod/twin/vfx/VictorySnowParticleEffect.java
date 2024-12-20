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

public class VictorySnowParticleEffect extends AbstractGameEffect {
    private static final Texture[] images = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_snow_1.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_snow_2.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/victory_snow_3.png"))
    };

    private final Texture img;
    private float x;
    private float y;
    private float dv;
    private final float drv;

    public VictorySnowParticleEffect() {
        this.img = images[MathUtils.random(images.length - 1)];
        x = MathUtils.random(Settings.WIDTH * 0.1F, Settings.WIDTH * 1.3F);
        y = MathUtils.random(Settings.HEIGHT * 0.9F, Settings.HEIGHT * 1.2F);
        dv = MathUtils.random(15.0F, 105.0F) * Settings.scale;
        drv = MathUtils.random(18.0F, 60.0F);
        scale = Settings.scale * MathUtils.random(0.1F, 0.3F);
        if (scale / Settings.scale >= 0.2F) {
            dv *= 2.0F;
        }
        duration = startingDuration = MathUtils.random(5.0F, 9.0F);
        color = Color.WHITE.cpy();
    }

    @Override
    public void update() {
        x -= dv * Gdx.graphics.getDeltaTime();
        y -= dv * Gdx.graphics.getDeltaTime();
        rotation += drv * Gdx.graphics.getDeltaTime();
        if (this.duration > this.startingDuration / 2.0F) {
            this.color.a = Interpolation.fade.apply(0.9F, 0.0F, (this.duration - this.startingDuration / 2.0F) / (this.startingDuration / 2.0F));
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 0.9F, this.duration / (this.startingDuration / 2.0F));
        }

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
