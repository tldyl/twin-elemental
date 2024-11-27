package demoMod.twin.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;

public class HeatWaveEffect extends AbstractGameEffect {
    private static ShaderProgram shader;
    private static boolean shaderCompiled = true;

    public HeatWaveEffect() {
        if (shader == null) {
            shader = new ShaderProgram(Gdx.files.internal("TwinShaders/heatWave/vertex.glsl"), Gdx.files.internal("TwinShaders/heatWave/fragment.glsl"));
            if (!shader.isCompiled()) {
                System.out.println("Twin Elemental Mod: [WARN] HeatWaveEffect: shader compile failed.");
                new RuntimeException(shader.getLog()).printStackTrace();
                shaderCompiled = false;
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
        if (shaderCompiled) {
            TwinElementalMod.postProcessQueue.add((sb, region) -> {
                sb.setShader(shader);
                shader.setUniformf("iResolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                shader.setUniformf("iTime", this.startingDuration - this.duration);
                sb.draw(region, 0, 0);
                sb.setShader(null);
            });
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
