package demoMod.twin.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.AttackEffectEnum;

public class FlashAtkImgEffectPatch {
    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "loadImage"
    )
    public static class PatchLoadImage {
        private static final Texture FLACHOP_IMG = new Texture(TwinElementalMod.getResourcePath("vfx/flachop.png"));
        private static final Texture CROSS_STAR_IMG = new Texture(TwinElementalMod.getResourcePath("vfx/crossStar.png"));
        private static final Texture RED_MOON_IMG = new Texture(TwinElementalMod.getResourcePath("vfx/redMoon.png"));
        private static final Texture SEA_OF_LANTERN_IMG = new Texture(TwinElementalMod.getResourcePath("vfx/seaOfLantern.png"));

        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(FlashAtkImgEffect effect) {
            AbstractGameAction.AttackEffect attackEffect = ReflectionHacks.getPrivate(effect, FlashAtkImgEffect.class, "effect");

            if (attackEffect == AttackEffectEnum.FLACHOP) {
                return SpireReturn.Return(getRegion(FLACHOP_IMG));
            } else if (attackEffect == AttackEffectEnum.CROSS_STAR) {
                ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "rotation", MathUtils.random(360.0F));
                float x = ReflectionHacks.getPrivate(effect, FlashAtkImgEffect.class, "x");
                float y = ReflectionHacks.getPrivate(effect, FlashAtkImgEffect.class, "y");
                x += MathUtils.random(-30.0F, 30.0F) * Settings.scale;
                y += MathUtils.random(-40.0F, 40.0F) * Settings.scale;
                ReflectionHacks.setPrivate(effect, FlashAtkImgEffect.class, "x", x);
                ReflectionHacks.setPrivate(effect, FlashAtkImgEffect.class, "y", y);
                return SpireReturn.Return(getRegion(CROSS_STAR_IMG));
            } else if (attackEffect == AttackEffectEnum.RED_MOON) {
                return SpireReturn.Return(getRegion(RED_MOON_IMG));
            } else if (attackEffect == AttackEffectEnum.SEA_OF_LANTERN) {
                return SpireReturn.Return(getRegion(SEA_OF_LANTERN_IMG));
            }
            return SpireReturn.Continue();
        }

        private static TextureAtlas.AtlasRegion getRegion(Texture texture) {
            return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        }
    }

    @SpirePatch(
            clz = FlashAtkImgEffect.class,
            method = "playSound"
    )
    public static class PatchPlaySound {
        public static SpireReturn<Void> Prefix(FlashAtkImgEffect effect, AbstractGameAction.AttackEffect attackEffect) {
            if (attackEffect == AttackEffectEnum.FLACHOP) {
                CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F);
                CardCrawlGame.sound.playA("ATTACK_HEAVY", -0.3F);
                return SpireReturn.Return(null);
            } else if (attackEffect == AttackEffectEnum.RED_MOON) {
                CardCrawlGame.sound.play("ATTACK_FIRE");
                return SpireReturn.Return(null);
            } else if (attackEffect == AttackEffectEnum.SEA_OF_LANTERN) {
                CardCrawlGame.sound.playA("BLOCK_ATTACK", -0.5F);
                CardCrawlGame.sound.play("ATTACK_FIRE");
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
