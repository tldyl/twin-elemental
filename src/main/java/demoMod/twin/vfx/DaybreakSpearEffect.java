package demoMod.twin.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class DaybreakSpearEffect extends AbstractGameEffect {
    private final AbstractCreature target;

    public DaybreakSpearEffect(AbstractCreature target) {
        this.target = target;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.playA("ATTACK_IRON_2", -0.4F);
        CardCrawlGame.sound.playA("ATTACK_HEAVY", -0.4F);
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(target.hb.cX + 60.0F * Settings.scale, target.hb.cY - 30.0F * Settings.scale, 2500.0F, -2500.0F, 225.0F, 8.0F, Color.GOLD, Color.GOLD));
        isDone = true;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
