package demoMod.twin.actions;

import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ChangeAnimationAction extends AbstractGameAction {
    private final String[] animationNames;
    private final boolean loop;
    private boolean firstUpdate = true;

    public ChangeAnimationAction(boolean loop, String... animationName) {
        this.animationNames = animationName;
        this.loop = loop;
    }

    @Override
    public void update() {
        if (firstUpdate) {
            firstUpdate = false;
            isDone = loop;
            AbstractDungeon.player.state.setAnimation(0, this.animationNames[0], loop);
            for (int i=1;i<this.animationNames.length;i++) {
                AbstractDungeon.player.state.setAnimation(i, this.animationNames[i], loop);
            }
            AbstractDungeon.player.state.addListener(new AnimationState.AnimationStateListener() {
                @Override
                public void event(int i, Event event) {

                }

                @Override
                public void complete(int i, int i1) {
                    System.out.printf("%s completed.%n", animationNames);
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
