package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.powers.DarkroomsPower;

import java.util.function.Supplier;

public class Darkrooms extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Darkrooms");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Darkrooms";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public Darkrooms() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseTraceAmount = this.traceAmount = 5;
        this.tags.add(CardTagsEnum.DOMAIN);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(1);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p != null && !p.hasPower(DarkroomsPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new DarkroomsPower(p, 1)));
        }
        return () -> DomainGenerator.getDomain(this, p, () -> addToBot(new ApplyPowerAction(p, p, new DarkroomsPower(p, this.magicNumber))), cardStrings.EXTENDED_DESCRIPTION[0], this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
        if (!p.hasPower(DarkroomsPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(p, p, new DarkroomsPower(p, 1)));
        }
    }
}
