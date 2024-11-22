package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.cards.tempCards.Cooperate;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;

import java.util.function.Supplier;

public class Shifting extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Shifting");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Shifting";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public Shifting() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseTraceAmount = this.traceAmount = 2;
        this.tags.add(CardTagsEnum.DOMAIN);
        this.cardsToPreview = new Cooperate();
        this.cardsToPreview.upgrade();
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeTraceAmount(1);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> addToBot(new MakeTempCardInHandAction(this.cardsToPreview)), cardStrings.EXTENDED_DESCRIPTION[0], 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SwitchLeaderAction());
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
