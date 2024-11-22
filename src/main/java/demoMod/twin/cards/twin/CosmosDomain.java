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

import java.util.List;
import java.util.stream.Collectors;

public class CosmosDomain extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("CosmosDomain");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/CosmosDomain";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 4;

    public CosmosDomain() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeBaseCost(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractCard> domainCards = p.drawPile.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
                domainCards.forEach(card -> addToTop(new PlayCardInCardGroupAction(card, p.drawPile, AbstractDungeon.getRandomMonster(), c -> c.exhaustOnUseOnce = true)));
                isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractCard> domainCards = p.hand.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
                domainCards.forEach(card -> addToTop(new PlayCardInCardGroupAction(card, p.hand, AbstractDungeon.getRandomMonster(), c -> c.exhaustOnUseOnce = true)));
                isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                List<AbstractCard> domainCards = p.discardPile.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
                domainCards.forEach(card -> addToTop(new PlayCardInCardGroupAction(card, p.discardPile, AbstractDungeon.getRandomMonster(), c -> c.exhaustOnUseOnce = true)));
                isDone = true;
            }
        });
    }
}
