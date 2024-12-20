package demoMod.twin.characters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPlayer;
import basemod.animations.G3DJAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.blights.FirstPosition;
import demoMod.twin.cards.twin.Defend_T;
import demoMod.twin.cards.twin.DivideLabour;
import demoMod.twin.cards.twin.Strike_T;
import demoMod.twin.cards.twin.Switch;
import demoMod.twin.enums.AbstractCardEnum;
import demoMod.twin.enums.AbstractPlayerEnum;
import demoMod.twin.patches.CharacterSelectScreenPatch;
import demoMod.twin.relics.FetterOfElement;
import demoMod.twin.stances.Blaze;
import demoMod.twin.stances.Freeze;
import demoMod.twin.ui.EnergyOrbTwin;
import demoMod.twin.vfx.VictoryEffect;

import java.util.ArrayList;
import java.util.List;

public class ZetsuEnnNoTwins extends CustomPlayer {
    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final Texture ORB_FLASH_VFX = new Texture(TwinElementalMod.getResourcePath("char/orb/vfx.png"));

    public ZetsuEnnNoTwins(String name, PlayerClass setClass) {
        super(name, setClass, new EnergyOrbTwin(), new G3DJAnimation(null, null));
        this.initializeClass(TwinElementalMod.getResourcePath("char/character.png"), TwinElementalMod.getResourcePath("char/shoulder2.png"), TwinElementalMod.getResourcePath("char/shoulder.png"), TwinElementalMod.getResourcePath("char/corpse.png"), this.getLoadout(), 0.0F, -20F, 250.0F, 270.0F, new EnergyManager(3));
        this.stance = new Blaze();
        if (CharacterSelectScreenPatch.reskinIndex == 0) {
            this.loadAnimation(TwinElementalMod.getResourcePath("char/break_fire_and_ice.atlas"), TwinElementalMod.getResourcePath("char/break_fire_and_ice.json"), 3.0F);
            this.state.setAnimation(0, "fire_idle", true);
            this.state.setTimeScale(2.0F);
        } else {
            this.img = new Texture(TwinElementalMod.getResourcePath("char/reskin_esaw.png"));
        }
        if (ModHelper.enabledMods.size() > 0 && (ModHelper.isModEnabled("Diverse") || ModHelper.isModEnabled("Chimera") || ModHelper.isModEnabled("Blue Cards"))) {
            this.masterMaxOrbs = 1;
        }
    }

    @Override
    public void playDeathAnimation() {
        if (this.img != null) {
            this.img.dispose();
        }
        this.img = TwinElementalMod.betaDefeatArt ? new Texture(TwinElementalMod.getResourcePath("char/corpse.png")) : new Texture(TwinElementalMod.getResourcePath("char/corpse2.png"));
        ReflectionHacks.setPrivate(this, AbstractPlayer.class, "renderCorpse", true);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(Strike_T.ID);
        ret.add(Strike_T.ID);
        ret.add(Strike_T.ID);
        ret.add(Strike_T.ID);
        ret.add(Defend_T.ID);
        ret.add(Defend_T.ID);
        ret.add(Defend_T.ID);
        ret.add(Defend_T.ID);
        ret.add(DivideLabour.ID);
        ret.add(Switch.ID);
        return ret;
    }

    @Override
    public Texture getEnergyImage() {
        return ORB_FLASH_VFX;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> ret = new ArrayList<>();
        ret.add(FetterOfElement.ID);
        return ret;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION, 88, 88, 0, 99, 5, this,
                getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.TWIN;
    }

    @Override
    public Color getCardRenderColor() {
        return TwinElementalMod.mainTwinColor;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Switch();
    }

    @Override
    public Color getCardTrailColor() {
        return TwinElementalMod.mainTwinColor.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 8;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
        CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F);
        CardCrawlGame.sound.playA("ATTACK_HEAVY", -0.3F);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new ZetsuEnnNoTwins("ZetsuEnnNoTwins", AbstractPlayerEnum.TWIN);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
        return Color.SKY;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    @Override
    public void onStanceChange(String id) {
        if (Blaze.STANCE_ID.equals(id)) {
            getBlight(FirstPosition.ID).counter = 0;
        } else if (Freeze.STANCE_ID.equals(id)) {
            getBlight(FirstPosition.ID).counter = 1;
        }
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage(TwinElementalMod.getResourcePath("scenes/twinBg.png"));
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(TwinElementalMod.getResourcePath("scenes/twin1.png"), "ATTACK_HEAVY"));
        panels.add(new CutscenePanel(TwinElementalMod.getResourcePath("scenes/twin2.png")));
        panels.add(new CutscenePanel(TwinElementalMod.getResourcePath("scenes/twin3.png")) {
            @Override
            public void render(SpriteBatch sb) {
                super.render(sb);
                panels.get(1).render(sb);
            }
        });
        panels.add(new CutscenePanel(TwinElementalMod.getResourcePath("scenes/twin4.png")));
        return panels;
    }

    @Override
    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        if (effects.isEmpty()) {
            effects.add(new VictoryEffect());
        }
    }

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("ZetsuEnnNoTwins");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];
    }
}
