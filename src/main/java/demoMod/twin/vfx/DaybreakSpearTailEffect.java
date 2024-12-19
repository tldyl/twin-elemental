package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class DaybreakSpearTailEffect extends AbstractGameEffect {
    private static final Texture[] images = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("vfx/spearTail1.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/spearTail2.png")),
            new Texture(TwinElementalMod.getResourcePath("vfx/spearTail3.png"))
    };

    private final Texture img;
    private float x;
    private float y;
    private final float dv;

    public DaybreakSpearTailEffect(float startX, float startY) {
        this.img = images[MathUtils.random(0, images.length - 1)];
        this.x = startX + 64.0F * Settings.scale;
        this.y = startY + 64.0F * Settings.scale;
        dv = MathUtils.random(90.0F, 420.0F) * Settings.scale;
        color = Color.WHITE.cpy();
        this.duration = this.startingDuration = 1.0F;
        scale = Settings.scale * MathUtils.random(0.2F, 1.0F);
        rotation = AbstractDungeon.player.flipHorizontal ? 135.0F : 225.0F;
    }

    @Override
    public void update() {
        super.update();
        x -= (AbstractDungeon.player.flipHorizontal ? -dv : dv) * Gdx.graphics.getDeltaTime();
        y += dv * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img, x - img.getWidth() / 2.0F, y - img.getHeight() / 2.0F,
                img.getWidth() / 2.0F, img.getHeight() / 2.0F,
                img.getWidth(), img.getHeight(),
                scale * MathUtils.random(0.95F, 1.05F), scale * MathUtils.random(0.95F, 1.05F),
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
