package demoMod.twin.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;

import java.util.HashMap;
import java.util.Map;

public class PowerRegionLoader {
    private static final Map<String, Texture> powerRegionMap = new HashMap<>();

    public static void load(AbstractPower power) {
        load(power, power.getClass().getSimpleName());
    }

    public static void load(AbstractPower power, String id) {
        String region32 = id.replace("Power", "") + "32";
        String region84 = id.replace("Power", "") + "84";
        if (!powerRegionMap.containsKey(region32)) {
            powerRegionMap.put(region32, new Texture(TwinElementalMod.getResourcePath("powers/" + region32 + ".png")));
        }
        if (!powerRegionMap.containsKey(region84)) {
            powerRegionMap.put(region84, new Texture(TwinElementalMod.getResourcePath("powers/" + region84 + ".png")));
        }
        Texture texture32 = powerRegionMap.get(region32);
        Texture texture84 = powerRegionMap.get(region84);
        power.region48 = new TextureAtlas.AtlasRegion(texture32, 0, 0, texture32.getWidth(), texture32.getHeight());
        power.region128 = new TextureAtlas.AtlasRegion(texture84, 0, 0, texture84.getWidth(), texture84.getHeight());
    }
}
