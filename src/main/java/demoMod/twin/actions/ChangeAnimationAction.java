package demoMod.twin.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ChangeAnimationAction extends AbstractGameAction {
    private final String animationName;

    public ChangeAnimationAction(String animationName) {
        this.animationName = animationName;
    }

    @Override
    public void update() {
        AbstractDungeon.player.state.setAnimation(0, this.animationName, true);
        isDone = true;
    }
}
