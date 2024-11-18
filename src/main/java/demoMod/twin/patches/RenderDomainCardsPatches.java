package demoMod.twin.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import demoMod.twin.ui.DomainCardsPanel;
import javassist.CtBehavior;

import java.util.ArrayList;

public class RenderDomainCardsPatches {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class RenderDomainCardsPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void PrefixPatchRender(AbstractPlayer player, SpriteBatch sb) {
            DomainCardsPanel.inst.render(sb);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "renderHealth");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
