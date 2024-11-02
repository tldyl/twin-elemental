package demoMod.twin.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.blights.FirstPosition;
import demoMod.twin.stances.Blaze;
import demoMod.twin.stances.Freeze;

public class AbstractDungeonPatch {
    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "resetPlayer"
    )
    public static class PatchResetPlayer {
        @SpireInsertPatch(rloc = 12)
        public static SpireReturn<Void> Insert() {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hasBlight(FirstPosition.ID)) {
                AbstractBlight blight = p.getBlight(FirstPosition.ID);
                if (blight.counter == 0) {
                    p.stance = new Blaze();
                } else {
                    p.stance = new Freeze();
                }
                p.onStanceChange(p.stance.ID);
                TwinElementalMod.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        p.stance.onEnterStance();
                        Blaze.switchedTimesThisTurn = 0;
                        SwitchLeaderAction.updateTwinCardBackground(p.drawPile);
                        SwitchLeaderAction.updateTwinCardBackground(p.hand);
                        SwitchLeaderAction.updateTwinCardBackground(p.discardPile);
                        SwitchLeaderAction.updateTwinCardBackground(p.exhaustPile);
                        SwitchLeaderAction.updateTwinCardBackground(p.limbo);
                        isDone = true;
                    }
                });
                GameActionManager.turn = 1;
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
