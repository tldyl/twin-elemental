package demoMod.twin.powers;

import basemod.BaseMod;
import basemod.cardmods.EtherealMod;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DomainRevelationPower extends AbstractPower {
    public static final String POWER_ID = TwinElementalMod.makeID("DomainRevelationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    private final boolean upgraded;

    public DomainRevelationPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;
        if (upgraded) {
            this.ID += "+";
            this.name += "+";
        }
        this.updateDescription();
        loadRegion("hello");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESC[upgraded ? 1 : 0], this.amount);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        List<AbstractCard> domainCards = BaseMod.getCustomCardsToAdd().stream().filter(c -> c.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
        List<AbstractCard> cardsToAdd = new ArrayList<>();
        for (int i=0;i<this.amount;i++) {
            cardsToAdd.add(domainCards.get(AbstractDungeon.cardRandomRng.random(0, domainCards.size() - 1)).makeCopy());
        }
        if (upgraded) {
            cardsToAdd.forEach(AbstractCard::upgrade);
        }
        cardsToAdd.forEach(c -> {
            CardModifierManager.addModifier(c, new ExhaustMod());
            CardModifierManager.addModifier(c, new EtherealMod());
            addToBot(new MakeTempCardInHandAction(c));
        });
    }
}
