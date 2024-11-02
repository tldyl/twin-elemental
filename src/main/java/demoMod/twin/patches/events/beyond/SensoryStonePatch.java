package demoMod.twin.patches.events.beyond;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.EventStrings;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.characters.ZetsuEnnNoTwins;

public class SensoryStonePatch {
    public static final String ID = TwinElementalMod.makeID("SensoryStone");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    @SpirePatch(
            clz = SensoryStone.class,
            method = "getRandomMemory"
    )
    public static class PatchGetRandomMemory {
        public static SpireReturn<Void> Prefix(SensoryStone event) {
            if (AbstractDungeon.player instanceof ZetsuEnnNoTwins) {
                event.imageEventText.updateBodyText(eventStrings.DESCRIPTIONS[0]);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
