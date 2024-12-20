package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class VictorySquareEffect extends AbstractGameEffect {
    public static final Texture img = new Texture(TwinElementalMod.getResourcePath("vfx/victory_square.png"));

    private final float cX;
    private final float cY;

    public VictorySquareEffect(float cX, float cY) {
        this.cX = cX;
        this.cY = cY;
        this.scale = Settings.scale;
        this.color = Color.WHITE.cpy();
        this.color.a = 0.5F;
    }

    @Override
    public void update() {
        this.rotation += 45.0F * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img, cX - img.getWidth() / 2.0F, cY - img.getHeight() / 2.0F,
                img.getWidth() / 2.0F, img.getHeight() / 2.0F,
                img.getWidth(), img.getHeight(),
                scale * MathUtils.random(0.995F, 1.005F), scale * MathUtils.random(0.995F, 1.005F),
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
