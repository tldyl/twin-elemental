package demoMod.twin.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.scene.SlowFireParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class VictoryEffect extends AbstractGameEffect {
    private final List<AbstractGameEffect> vfxList = new ArrayList<>();

    private float effectTimer = 0.02F;

    public VictoryEffect() {
        Vector2 vector2 = new Vector2(VictorySquareEffect.img.getWidth() / 4.0F, 0.0F);
        for (int i=0;i<8;i++) {
            float offsetX = vector2.x;
            float offsetY = vector2.y;
            vfxList.add(new VictorySquareEffect(Settings.WIDTH / 2.0F + offsetX, Settings.HEIGHT / 2.0F + offsetY));
            vector2.rotate(45.0F);
        }
    }

    @Override
    public void update() {
        effectTimer -= Gdx.graphics.getDeltaTime();
        if (effectTimer <= 0 && vfxList.size() <= 158) {
            effectTimer = 0.02F;
            vfxList.add(new VictorySnowParticleEffect());
            vfxList.add(new VictoryFlameParticleEffect());
            AbstractGameEffect effect = new SlowFireParticleEffect();
            ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "scale", MathUtils.random(0.15F, 1.5F) * Settings.scale);
            ReflectionHacks.setPrivate(effect, SlowFireParticleEffect.class, "vY2", MathUtils.random(25.0F, 450.0F) * Settings.scale);
            ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "color", new Color(236.0F / 255.0F, 89.0F / 255.0F, 34.0F / 255.0F, 0.0F));
            vfxList.add(effect);
        }
        vfxList.forEach(AbstractGameEffect::update);
        vfxList.removeIf(e -> e.isDone);
    }

    @Override
    public void render(SpriteBatch sb) {
        vfxList.forEach(e -> e.render(sb));
    }

    @Override
    public void dispose() {

    }
}
