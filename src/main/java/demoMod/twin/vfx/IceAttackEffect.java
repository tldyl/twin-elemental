package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class IceAttackEffect extends AbstractGameEffect {
    private static final Texture img = new Texture(TwinElementalMod.getResourcePath("vfx/ice.png"));

    private final AbstractCreature target;

    public IceAttackEffect(AbstractCreature target, float duration) {
        this.duration = duration;
        this.target = target;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(new TextureRegion(img), target.hb.cX - img.getWidth() / 2.0F, target.hb.cY - img.getHeight() / 2.0F, img.getWidth() / 2.0F, img.getHeight() / 2.0F, img.getWidth(), img.getHeight(), Settings.scale, Settings.scale, 0.0F);
    }

    @Override
    public void dispose() {

    }
}
