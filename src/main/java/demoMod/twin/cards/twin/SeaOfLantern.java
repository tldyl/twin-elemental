package demoMod.twin.cards.twin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.tempCards.Cooperate;
import demoMod.twin.enums.AttackEffectEnum;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Freeze;

public class SeaOfLantern extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("SeaOfLantern");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/SeaOfLantern";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(TwinElementalMod.getResourcePath("cards/SeaOfLantern+_attack.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 2;

    public SeaOfLantern() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 14;
        this.baseMagicNumber = this.magicNumber = 1;
        this.cardsToPreview = new Cooperate();
        this.cardsToPreview.upgrade();
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = cardStrings.EXTENDED_DESCRIPTION[0];
        this.initializeTitle();
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(4);
            upgradeMagicNumber(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.portrait = UPGRADE_IMG;
            this.textureImg = TwinElementalMod.getResourcePath("cards/SeaOfLantern+_attack.png");
            this.initializeDescription();
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AttackEffectEnum.SEA_OF_LANTERN));
        if (p.stance instanceof Freeze) {
            addToBot(new GainEnergyAction(upgraded ? 2 : 1));
        } else {
            addToBot(new MakeTempCardInHandAction(this.cardsToPreview, this.magicNumber));
        }
    }
}
