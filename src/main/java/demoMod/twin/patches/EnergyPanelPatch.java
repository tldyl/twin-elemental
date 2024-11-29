package demoMod.twin.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import demoMod.twin.powers.NoGainEnergyPower;

public class EnergyPanelPatch {
    @SpirePatch(
            clz = EnergyPanel.class,
            method = "addEnergy"
    )
    public static class PatchAddEnergy {
        public static SpireReturn<Void> Prefix(int e) {
            if (AbstractDungeon.player.hasPower(NoGainEnergyPower.POWER_ID)) {
                AbstractDungeon.player.getPower(NoGainEnergyPower.POWER_ID).flash();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
