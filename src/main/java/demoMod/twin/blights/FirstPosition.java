package demoMod.twin.blights;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import demoMod.twin.TwinElementalMod;

public class FirstPosition extends AbstractBlight {
    public static final String ID = TwinElementalMod.makeID("FirstPosition");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static Texture IMG_BLAZE = new Texture(TwinElementalMod.getResourcePath("relics/firstPositionBlaze.png"));
    public static Texture IMG_FREEZE = new Texture(TwinElementalMod.getResourcePath("relics/firstPositionFreeze.png"));

    public FirstPosition() {
        super(ID, NAME, DESCRIPTION, "durian.png", true);
        this.increment = 0;
        this.counter = 0;
        this.img.dispose();
        this.outlineImg.dispose();
        this.img = IMG_BLAZE;
        this.outlineImg = this.img;
    }

    @Override
    public void update() {
        super.update();
        if (counter == 0) {
            this.img = IMG_BLAZE;
        } else {
            this.img = IMG_FREEZE;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
    }

    static {
        RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        NAME = relicStrings.NAME;
        DESCRIPTION = relicStrings.DESCRIPTIONS[0];
    }
}
