package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.stances.Blaze;

import java.util.function.Supplier;

public class BurningSky extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("BurningSky");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/BurningSky";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public BurningSky() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 9;
        this.baseTraceAmount = this.traceAmount = 4;
        this.tags.add(CardTagsEnum.DOMAIN);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!(p.stance instanceof Blaze)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        return super.canUse(p, m) && p.stance instanceof Blaze;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(3);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber))), cardStrings.EXTENDED_DESCRIPTION[0], this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber)));
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
