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

public class SpinningRadianceParticleEffect extends AbstractGameEffect {
    private static final Texture[] images = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("vfx/spin1.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/spin2.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/spin3.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/spin4.png"))
    };

    private final Texture img;
    private float x;
    private final float y;
    private float dv;
    private final float drv;

    public SpinningRadianceParticleEffect(int index) {
        this.img = images[Math.min(index, images.length - 1)];
        boolean reverse = MathUtils.randomBoolean();
        x = MathUtils.random(-Settings.WIDTH * 0.3F, Settings.WIDTH * 0.7F);
        y = MathUtils.random(Settings.HEIGHT * 0.1F, Settings.HEIGHT * 0.95F);
        dv = MathUtils.random(15.0F, 210.0F) * Settings.scale;
        drv = MathUtils.random(18.0F, 60.0F);
        if (reverse) {
            x += Settings.WIDTH * 0.6F;
            dv *= -1.0F;
        }
        scale = Settings.scale * MathUtils.random(0.3F, 1.0F);
        duration = startingDuration = MathUtils.random(3.0F, 6.0F);
        color = new Color(MathUtils.random(0.55F, 0.6F), MathUtils.random(0.8F, 1.0F), MathUtils.random(0.95F, 1.0F), 0.0F);
    }

    @Override
    public void update() {
        x += dv * Gdx.graphics.getDeltaTime();
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
        sb.draw(img, x - 200.0F, y - 200.0F, 200.0F, 200.0F, 400, 400, scale * MathUtils.random(0.99F, 1.01F), scale * MathUtils.random(0.99F, 1.01F), rotation, 0, 0, 400, 400, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {

    }
}
