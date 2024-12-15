package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class CosmosDomainEffect extends AbstractGameEffect {
    private final float x;
    private final float y;
    private Texture img = new Texture(TwinElementalMod.getResourcePath("vfx/cosmosDomain.png"));
    private boolean playSound = true;

    public CosmosDomainEffect(float x, float y, Color color) {
        this.rotation = MathUtils.random(360.0F);
        this.scale = 0.0F;
        this.x = x;
        this.y = y;
        this.duration = this.startingDuration = 2.0F;
        this.color = color;
        this.renderBehind = MathUtils.randomBoolean();
        color.a = 1.0F;
    }

    @Override
    public void update() {
        if (playSound) {
            CardCrawlGame.sound.playA("TWIN_OUTBREAK", 0.0F);
            playSound = false;
        }
        float progress = (2.0F - this.duration) / 2.0F;
        this.scale = (float) (Math.pow(progress * 3.2F, 10)) * 5.0F + (float) Math.cos(progress * 1.57F);
        this.duration -= Gdx.graphics.getDeltaTime();
        this.rotation += 360.0F * Gdx.graphics.getDeltaTime();
        this.color.a = this.duration / this.startingDuration;
        if (this.duration < 0.0F) {
            this.isDone = true;
            dispose();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x - img.getWidth() / 2.0F, this.y - img.getHeight() / 2.0F, img.getWidth() / 2.0F, img.getHeight() / 2.0F, img.getWidth(), img.getHeight(), this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        if (this.img != null) {
            this.img.dispose();
            this.img = null;
        }
    }
}
