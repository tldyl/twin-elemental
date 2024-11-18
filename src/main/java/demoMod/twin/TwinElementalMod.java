package demoMod.twin;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import demoMod.twin.blights.FirstPosition;
import demoMod.twin.cards.tempCards.Cooperate;
import demoMod.twin.cards.twin.*;
import demoMod.twin.characters.ZetsuEnnNoTwins;
import demoMod.twin.dynamicVariables.TraceAmount;
import demoMod.twin.enums.AbstractCardEnum;
import demoMod.twin.enums.AbstractPlayerEnum;
import demoMod.twin.potions.AcceleratePotion;
import demoMod.twin.potions.AgilePotion;
import demoMod.twin.potions.HandWarmer;
import demoMod.twin.potions.Refrigerant;
import demoMod.twin.relics.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SpireInitializer
public class TwinElementalMod implements EditCharactersSubscriber,
        EditStringsSubscriber,
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        PostDungeonInitializeSubscriber,
        PostUpdateSubscriber,
        PostRenderSubscriber,
        AddAudioSubscriber,
        ScreenPostProcessor {

    public static final String ATTACK_CARD = "512/bg_attack_twin.png";
    public static final String SKILL_CARD = "512/bg_skill_twin.png";
    public static final String POWER_CARD = "512/bg_power_twin.png";
    private static final String ENERGY_ORB = "512/card_icebreaker_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";
    private static final String ATTACK_CARD_PORTRAIT = "1024/bg_attack_twin.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/bg_skill_twin.png";
    private static final String POWER_CARD_PORTRAIT = "1024/bg_power_twin.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_icebreaker_orb.png";

    public static Color mainTwinColor = new Color(0.75686F, 0.9294F, 1.0F, 1.0F);
    private static final List<AbstractGameAction> actionList = new ArrayList<>();
    private static final List<AbstractGameAction> parallelActions = new ArrayList<>();
    public static final List<Consumer<SpriteBatch>> renderable = new ArrayList<>();
    private FrameBuffer fbo;

    public static final List<BiConsumer<SpriteBatch, TextureRegion>> postProcessQueue = new ArrayList<>();

    public static void initialize() {
        new TwinElementalMod();
        BaseMod.addColor(AbstractCardEnum.TWIN,
                mainTwinColor, mainTwinColor, mainTwinColor, mainTwinColor, mainTwinColor, mainTwinColor, mainTwinColor,
                getResourcePath(ATTACK_CARD), getResourcePath(SKILL_CARD),
                getResourcePath(POWER_CARD), getResourcePath(ENERGY_ORB),
                getResourcePath(ATTACK_CARD_PORTRAIT), getResourcePath(SKILL_CARD_PORTRAIT),
                getResourcePath(POWER_CARD_PORTRAIT), getResourcePath(ENERGY_ORB_PORTRAIT), getResourcePath(CARD_ENERGY_ORB));
    }

    public static String makeID(String name) {
        return "Twin:" + name;
    }

    public static String getResourcePath(String path) {
        return "TwinImages/" + path;
    }

    public TwinElementalMod() {
        BaseMod.subscribe(this);
        ScreenPostProcessorManager.addPostProcessor(this);
    }

    private static String getAudioPath(String audioName) {
        return "TwinAudio/sfx/" + audioName + ".wav";
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("TWIN_FREEZE_1", getAudioPath("freeze1"));
        BaseMod.addAudio("TWIN_FREEZE_2", getAudioPath("freeze2"));
        BaseMod.addAudio("TWIN_FREEZE_3", getAudioPath("freeze3"));
        BaseMod.addAudio("TWIN_FREEZE_SHATTER_1", getAudioPath("freeze_shatter1"));
        BaseMod.addAudio("TWIN_FREEZE_SHATTER_2", getAudioPath("freeze_shatter2"));
        BaseMod.addAudio("TWIN_FREEZE_SHATTER_3", getAudioPath("freeze_shatter3"));
        BaseMod.addAudio("TWIN_ICE_SPLASH", getAudioPath("ice_blast_projectile_spell_01"));
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new TraceAmount());
        BaseMod.addCard(new Strike_T());
        BaseMod.addCard(new Defend_T());
        BaseMod.addCard(new DivideLabour());
        BaseMod.addCard(new Switch());
        BaseMod.addCard(new Icicle());
        BaseMod.addCard(new Flachop());
        BaseMod.addCard(new Abycold());
        BaseMod.addCard(new HeatWave());
        BaseMod.addCard(new Starmunicate());
        BaseMod.addCard(new HemIn());
        BaseMod.addCard(new Activate());
        BaseMod.addCard(new Skate());
        BaseMod.addCard(new LightingSlash());
        BaseMod.addCard(new RedMoon());
        BaseMod.addCard(new FrozenWind());
        BaseMod.addCard(new HotDomain());
        BaseMod.addCard(new ColdDomain());
        BaseMod.addCard(new WindDomain());
        BaseMod.addCard(new GatherDomain());
        BaseMod.addCard(new Shining());
        BaseMod.addCard(new Karma());
        BaseMod.addCard(new SeeYouOtherside());
        BaseMod.addCard(new Snowstorm());
        BaseMod.addCard(new Firacier());
        BaseMod.addCard(new FrozenSpear());
        BaseMod.addCard(new FireWheel());
        BaseMod.addCard(new GeminiRotate());
        BaseMod.addCard(new IcyBurst());
        BaseMod.addCard(new FireBurst());
        BaseMod.addCard(new Radius20MetersDomain());
        BaseMod.addCard(new DeathFreeze());
        BaseMod.addCard(new ElementRotate());
        BaseMod.addCard(new BurningSky());
        BaseMod.addCard(new Monopoly());
        BaseMod.addCard(new SeaOfLantern());
        BaseMod.addCard(new FantasyLantern());
        BaseMod.addCard(new Hikari());
        BaseMod.addCard(new Darkrooms());
        BaseMod.addCard(new TwinMoons());
        BaseMod.addCard(new MagicBlock());
        BaseMod.addCard(new FantasyStar());
        BaseMod.addCard(new Stasis());
        BaseMod.addCard(new SnowZone());
        BaseMod.addCard(new HotWind());
        BaseMod.addCard(new HolyCross());
        BaseMod.addCard(new Niflheimr());
        BaseMod.addCard(new FrozenOcean());
        BaseMod.addCard(new WolfPath());
        BaseMod.addCard(new PolarWind());
        BaseMod.addCard(new Volcano());
        BaseMod.addCard(new Shifting());
        BaseMod.addCard(new Proud());
        BaseMod.addCard(new KuuhakuDomain());
        BaseMod.addCard(new Avalon());
        BaseMod.addCard(new Yuusha());
        BaseMod.addCard(new DaylightCycle());
        BaseMod.addCard(new Perceive());
        BaseMod.addCard(new Dust());
        BaseMod.addCard(new MissingSurge());
        BaseMod.addCard(new Hanabi());
        BaseMod.addCard(new Asahi());
        BaseMod.addCard(new DaybreakSpear());
        BaseMod.addCard(new KingIceBound());
        BaseMod.addCard(new FantasyHell());
        BaseMod.addCard(new DomainGospel());
        BaseMod.addCard(new DomainRevelation());
        BaseMod.addCard(new Maplooming());
        BaseMod.addCard(new Butterfly());
        BaseMod.addCard(new CosmosDomain());
        BaseMod.addCard(new SpinningRadiance());
        BaseMod.addCard(new JintaiRensei());
        BaseMod.addCard(new Seasons());
        BaseMod.addCard(new Haru());
        BaseMod.addCard(new ThenWeReunion());
        BaseMod.addCard(new VenusForm());

        BaseMod.addCard(new Cooperate());
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new ZetsuEnnNoTwins("ZetsuEnnNoTwins", AbstractPlayerEnum.TWIN), getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"), AbstractPlayerEnum.TWIN);
    }

    public static String getLanguageString() {
        String language;
        switch (Settings.language) {
            case ZHS:
                language = "zhs";
                break;
                /*
            case KOR:
                language = "kor";
                break;
            case JPN:
                language = "jpn";
                break;
                */
            default:
                language = "eng";
        }
        return language;
    }

    @Override
    public void receiveEditKeywords() {
        final Gson gson = new Gson();
        String language;
        language = getLanguageString();
        final String json = Gdx.files.internal("localizations/" + language + "/Twin-KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword("twin", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new FetterOfElement(), AbstractCardEnum.TWIN);
        BaseMod.addRelicToCustomPool(new FetterOfDicoaster(), AbstractCardEnum.TWIN);
        BaseMod.addRelicToCustomPool(new Cooperation(), AbstractCardEnum.TWIN);
        BaseMod.addRelicToCustomPool(new Rime(), AbstractCardEnum.TWIN);
        BaseMod.addRelicToCustomPool(new Skewer(), AbstractCardEnum.TWIN);
        BaseMod.addRelicToCustomPool(new GuideOfMoonlight(), AbstractCardEnum.TWIN);
        BaseMod.addRelic(new CrimsonCherry(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new Feather(), AbstractCardEnum.TWIN);
        BaseMod.addRelic(new AutumnMaple(), RelicType.SHARED);
        BaseMod.addRelic(new Razer(), RelicType.SHARED);
        BaseMod.addRelic(new DistantSavior(), RelicType.SHARED);
        BaseMod.addRelicToCustomPool(new CrossStar(), AbstractCardEnum.TWIN);
    }

    @Override
    public void receiveEditStrings() {
        String language;
        language = getLanguageString();

        String cardStrings = Gdx.files.internal("localizations/" + language + "/Twin-CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CardStrings.class, cardStrings);
        String relicStrings = Gdx.files.internal("localizations/" + language + "/Twin-RelicStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
        String powerStrings = Gdx.files.internal("localizations/" + language + "/Twin-PowerStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PowerStrings.class, powerStrings);
        String charStrings = Gdx.files.internal("localizations/" + language + "/Twin-CharacterStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(CharacterStrings.class, charStrings);
        String stanceStrings = Gdx.files.internal("localizations/" + language + "/Twin-StanceStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(StanceStrings.class, stanceStrings);
        String uiStrings = Gdx.files.internal("localizations/" + language + "/Twin-UIStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(UIStrings.class, uiStrings);
        String potionStrings = Gdx.files.internal("localizations/" + language + "/Twin-PotionStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
        String eventStrings = Gdx.files.internal("localizations/" + language + "/Twin-EventStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addPotion(AgilePotion.class, new Color(0.0F, 0.4F, 1.0F, 1.0F), Color.SKY, Color.WHITE, AgilePotion.ID, AbstractPlayerEnum.TWIN);
        BaseMod.addPotion(HandWarmer.class, new Color(0.9F, 0.3F, 0.2F, 1.0F), Color.RED, null, HandWarmer.ID, AbstractPlayerEnum.TWIN);
        BaseMod.addPotion(Refrigerant.class, new Color(0.0F, 0.4F, 1.0F, 1.0F), Color.SKY, null, Refrigerant.ID, AbstractPlayerEnum.TWIN);
        BaseMod.addPotion(AcceleratePotion.class, new Color(0.5F, 0.4F, 1.0F, 1.0F), Color.SKY, null, AcceleratePotion.ID, AbstractPlayerEnum.TWIN);
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        for (Consumer<SpriteBatch> item : renderable) {
            item.accept(sb);
        }
        renderable.clear();
    }

    public static void addToBot(AbstractGameAction action) {
        actionList.add(action);
    }

    public static void addParallel(AbstractGameAction action) {
        parallelActions.add(action);
    }

    @Override
    public void receivePostUpdate() {
        if (!actionList.isEmpty()) {
            actionList.get(0).update();
            if (actionList.get(0).isDone) {
                actionList.remove(0);
            }
        }
        if (!parallelActions.isEmpty()) {
            for (AbstractGameAction action : parallelActions) {
                action.update();
            }
            parallelActions.removeIf(action -> action.isDone);
        }
    }

    @Override
    public void receivePostDungeonInitialize() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p instanceof ZetsuEnnNoTwins) {
            if (!p.hasBlight(FirstPosition.ID)) {
                new FirstPosition().instantObtain(p, p.blights.size(), true);
            }
        }
    }

    @Override
    public void postProcess(SpriteBatch sb, TextureRegion region, OrthographicCamera camera) {
        if (!postProcessQueue.isEmpty()) {
            if (fbo == null) {
                fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            }
            for (BiConsumer<SpriteBatch, TextureRegion> consumer : postProcessQueue) {
                sb.end();
                fbo.begin();
                sb.begin();
                consumer.accept(sb, region);
                sb.end();
                fbo.end();
                region = new TextureRegion(fbo.getColorBufferTexture(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                region.flip(false, true);
                sb.begin();
            }
            postProcessQueue.clear();
        }
        sb.draw(region, 0, 0);
        sb.setProjectionMatrix(camera.combined);
    }
}
