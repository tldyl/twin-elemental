package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HolyCross extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("HolyCross");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/HolyCross";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public HolyCross() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseTraceAmount = this.traceAmount = 3;
        this.tags.add(CardTagsEnum.DOMAIN);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeTraceAmount(1);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> {
            List<AbstractPower> debuffs = p.powers.stream().filter(power -> power.type == AbstractPower.PowerType.DEBUFF).collect(Collectors.toList());
            if (!debuffs.isEmpty()) {
                Collections.shuffle(debuffs, new Random(AbstractDungeon.cardRandomRng.randomLong()));
                addToBot(new RemoveSpecificPowerAction(p, p, debuffs.get(0)));
            }
            List<AbstractCard> statusCards = p.drawPile.group.stream().filter(card -> card.type == CardType.STATUS).collect(Collectors.toList());
            if (!statusCards.isEmpty()) {
                Collections.shuffle(statusCards, new Random(AbstractDungeon.cardRandomRng.randomLong()));
                addToBot(new ExhaustSpecificCardAction(statusCards.get(0), p.drawPile, true));
            }
        }, cardStrings.EXTENDED_DESCRIPTION[0], this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
