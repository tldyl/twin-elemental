package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class HeatWaveEffect extends AbstractGameEffect {
    private static ShaderProgram shader;

    public HeatWaveEffect() {
        if (shader == null) {
            shader = new ShaderProgram(Gdx.files.internal("TwinShaders/heatWave/vertex.glsl"), Gdx.files.internal("TwinShaders/heatWave/fragment.glsl"));
            if (!shader.isCompiled()) {
                throw new RuntimeException(shader.getLog());
            }
        }
        this.duration = this.startingDuration = 2.0F;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0) {
            this.isDone = true;
            return;
        }
        TwinElementalMod.postProcessQueue.add((sb, region) -> {
            sb.setShader(shader);
            shader.setUniformf("iResolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shader.setUniformf("iTime", this.startingDuration - this.duration);
            sb.draw(region, 0, 0);
            sb.setShader(null);
        });
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
