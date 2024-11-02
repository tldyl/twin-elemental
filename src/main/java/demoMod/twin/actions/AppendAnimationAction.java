package demoMod.twin.actions;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AppendAnimationAction extends AbstractGameAction {
    private final String animationName;
    private final boolean loop;
    private boolean firstUpdate = true;
    public AppendAnimationAction(String animationName, boolean loop) {
        this.animationName = animationName;
        this.loop = loop;
    }

    @Override
    public void update() {
        if (firstUpdate) {
            firstUpdate = false;
            isDone = loop;
            AbstractDungeon.player.state.addAnimation(0, this.animationName, loop, 0.0F);
            AbstractDungeon.player.state.addListener(new AnimationState.AnimationStateListener() {
                @Override
                public void event(int i, Event event) {

                }

                @Override
                public void complete(int i, int i1) {
                    System.out.printf("%s completed.%n", animationName);
                    isDone = true;
                    addToTop(new AbstractGameAction() {
                        @Override
                        public void update() {
                            AbstractDungeon.player.state.clearListeners();
                            isDone = true;
                        }
                    });
                }

                @Override
                public void start(int i) {

                }

                @Override
                public void end(int i) {

                }
            });
        }
    }
}
