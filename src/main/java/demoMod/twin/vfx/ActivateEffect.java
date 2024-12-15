package demoMod.twin.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class ActivateEffect extends AbstractGameEffect {
    private static final TextureRegion[] img = new TextureRegion[] {
            new TextureRegion(new Texture(TwinElementalMod.getResourcePath("vfx/activate1.png"))),
            new TextureRegion(new Texture(TwinElementalMod.getResourcePath("vfx/activate2.png"))),
            new TextureRegion(new Texture(TwinElementalMod.getResourcePath("vfx/activate3.png"))),
            new TextureRegion(new Texture(TwinElementalMod.getResourcePath("vfx/activate4.png")))
    };

    private final float currentX;
    private float currentY;
    private final float targetY;

    private final int imgIndex;

    public ActivateEffect(float startX, float startY) {
        this.color = Color.WHITE.cpy();
        this.currentX = startX;
        this.currentY = startY;
        this.targetY = startY + 200.0F * Settings.scale;
        this.duration = this.startingDuration = 0.7F;
        imgIndex = MathUtils.random(3);
    }

    @Override
    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.playA("HEAL_3", 0.5F);
        }
        super.update();
        this.currentY = MathHelper.orbLerpSnap(this.currentY, this.targetY);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img[imgIndex], currentX - img[imgIndex].getRegionWidth() / 2.0F, currentY - img[imgIndex].getRegionHeight() / 2.0F,
                img[imgIndex].getRegionWidth() / 2.0F, img[imgIndex].getRegionHeight() / 2.0F,
                img[imgIndex].getRegionWidth(), img[imgIndex].getRegionHeight(),
                Settings.scale, Settings.scale, 0.0F);
        sb.draw(img[imgIndex], currentX - img[imgIndex].getRegionWidth() / 2.0F, currentY - img[imgIndex].getRegionHeight() / 2.0F,
                img[imgIndex].getRegionWidth() / 2.0F, img[imgIndex].getRegionHeight() / 2.0F,
                img[imgIndex].getRegionWidth(), img[imgIndex].getRegionHeight(),
                Settings.scale, Settings.scale, 0.0F);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {

    }
}
