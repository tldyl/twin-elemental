package demoMod.twin.cards.twin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.BoostAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class Starmunicate extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Starmunicate");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Starmunicate";
    private static final TextureAtlas.AtlasRegion UPGRADE_IMG = new TextureAtlas.AtlasRegion(new Texture(TwinElementalMod.getResourcePath("cards/Starmunicate+_attack.png")), 0, 0, 250, 190);

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;

    public Starmunicate() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.tags.add(CardTagsEnum.COPORATE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeDamage(3);
            this.portrait = UPGRADE_IMG;
            this.textureImg = TwinElementalMod.getResourcePath("cards/Starmunicate+_attack.png");
        };
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (Blaze.switchedTimesThisTurn > 0) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
            addToBot(new BoostAction());
        }
        resetCoporateState();
    }
}
