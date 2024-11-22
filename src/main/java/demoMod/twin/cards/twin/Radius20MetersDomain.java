package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.PlayCardInCardGroupAction;
import demoMod.twin.enums.CardTagsEnum;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Radius20MetersDomain extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Radius20MetersDomain");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/Radius20MetersDomain";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;

    public Radius20MetersDomain() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.tags.add(CardTagsEnum.COPORATE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        List<AbstractCard> domainCards = p.drawPile.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
        int playSize = this.magicNumber;
        if (costForTurn == 0) {
            playSize++;
        }
        if (domainCards.size() > playSize) {
            Collections.shuffle(domainCards, new Random(AbstractDungeon.cardRandomRng.random.nextLong()));
            domainCards = domainCards.subList(0, playSize);
        }
        domainCards.forEach(card -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                addToTop(new PlayCardInCardGroupAction(card, p.drawPile, AbstractDungeon.getRandomMonster(), card1 -> {}));
                isDone = true;
            }
        }));
        resetCoporateState();
    }
}
