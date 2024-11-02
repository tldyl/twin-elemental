package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.PlayCardInCardGroupAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class KuuhakuDomain extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("KuuhakuDomain");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 0;

    public KuuhakuDomain() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTagsEnum.DOMAIN);
        this.isInnate = true;
        this.exhaust = true;
        this.baseTraceAmount = this.traceAmount = 2;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeTraceAmount(1);
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> {
            List<AbstractCard> domainCards = p.discardPile.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
            int playSize = 1;
            if (domainCards.size() > playSize) {
                Collections.shuffle(domainCards, new Random(AbstractDungeon.cardRandomRng.random.nextLong()));
                domainCards = domainCards.subList(0, playSize);
            }
            domainCards.forEach(card -> addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    addToTop(new PlayCardInCardGroupAction(card, p.discardPile, AbstractDungeon.getRandomMonster(), card1 -> {}));
                    isDone = true;
                }
            }));
        }, cardStrings.EXTENDED_DESCRIPTION[0], 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
    }
}
