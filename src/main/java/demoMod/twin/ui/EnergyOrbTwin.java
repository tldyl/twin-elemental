package demoMod.twin.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.stances.Freeze;

public class EnergyOrbTwin implements EnergyOrbInterface {
    private static Texture[] blazeLayers = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("char/orb/bl1.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/bl2.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/bl3.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/bl4.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/bl5.png"))

    };
    private static int[] rotationSpeed = new int[]{
            0, 1, -1, -2, -3
    };
    private static float[] rotation = new float[] {
            0, 0, 0, 0, 0
    };
    private static Texture[] freezeLayers = new Texture[]{
            new Texture(TwinElementalMod.getResourcePath("char/orb/fl1.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/fl2.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/fl3.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/fl4.png")),
            new Texture(TwinElementalMod.getResourcePath("char/orb/fl5.png"))
    };

    private float currentBlazeAlpha = 1.0F;
    private float targetBlazeAlpha = 1.0F;

    public EnergyOrbTwin() {

    }

    @Override
    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        Color renderColor = (enabled ? Color.WHITE : Color.GRAY).cpy();
        renderColor.a = currentBlazeAlpha;
        sb.setColor(renderColor);
        sb.setBlendFunction(770, 1);
        int index = 0;
        for (Texture texture : blazeLayers) {
            sb.draw(texture, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 1.5F * Settings.scale, 1.5F * Settings.scale, rotation[index], 0, 0, 128, 128, false, false);
            index++;
        }
        renderColor.a = 1.0F - renderColor.a;
        sb.setColor(renderColor);
        index = 0;
        for (Texture texture : freezeLayers) {
            sb.draw(texture, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, 1.5F * Settings.scale, 1.5F * Settings.scale, -rotation[index], 0, 0, 128, 128, false, false);
            index++;
        }
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void updateOrb(int orbCount) {
        if (AbstractDungeon.player.stance instanceof Freeze) {
            targetBlazeAlpha = 0.0F;
        } else {
            targetBlazeAlpha = 1.0F;
        }
        currentBlazeAlpha = MathHelper.slowColorLerpSnap(currentBlazeAlpha, targetBlazeAlpha);
        for (int i=0;i<rotation.length;i++) {
            rotation[i] += rotationSpeed[i] * 12.0F * Gdx.graphics.getDeltaTime();
            rotation[i] %= 360.0F;
        }
    }
}
