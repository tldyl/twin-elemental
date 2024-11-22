package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.PutSpecifiedCardToHandAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.powers.LoseTracePower;
import demoMod.twin.powers.TracePower;
import demoMod.twin.stances.Freeze;

public class Snowstorm extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Snowstorm");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Snowstorm";

    private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.ENEMY;

    private static final int COST = 1;

    public Snowstorm() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
        addToBot(new PutSpecifiedCardToHandAction(this.magicNumber, card -> card.hasTag(CardTagsEnum.DOMAIN)));
        if (p.stance instanceof Freeze) {
            addToBot(new ApplyPowerAction(p, p, new TracePower(p, this.magicNumber)));
            addToBot(new ApplyPowerAction(p, p, new LoseTracePower(p, this.magicNumber)));
        }
    }
}
