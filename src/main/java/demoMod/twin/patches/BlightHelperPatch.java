package demoMod.twin.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import demoMod.twin.blights.FirstPosition;

public class BlightHelperPatch {
    @SpirePatch(
            clz = BlightHelper.class,
            method = "getBlight"
    )
    public static class PatchBlight {
        public static SpireReturn<AbstractBlight> Prefix(String id) {
            if (FirstPosition.ID.equals(id)) {
                return SpireReturn.Return(new FirstPosition());
            }
            return SpireReturn.Continue();
        }
    }
}
