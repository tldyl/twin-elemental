package demoMod.twin.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.helpers.PowerRegionLoader;
import demoMod.twin.interfaces.OnDomainRemoveSubscriber;
import demoMod.twin.interfaces.OnDomainTriggerSubscriber;
import demoMod.twin.ui.DomainCardsPanel;

import java.util.Collections;
import java.util.UUID;

public class DomainPower extends TwoAmountPower implements CloneablePowerInterface {
    private final String rawDescription;
    private final Runnable effect;
    private final AbstractTwinCard domainCard;

    public DomainPower(AbstractTwinCard domainCard, AbstractCreature owner, int amount, String id, String name, Runnable effect, String description, int magicNumber) {
        this.ID = id;
        this.domainCard = domainCard;
        this.name = name;
        this.owner = owner;
        this.amount = amount;
        this.effect = effect;
        this.rawDescription = description;
        this.amount2 = magicNumber;
        updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void onInitialApplication() {
        DomainCardsPanel.inst.addDomainCard(ID, domainCard);
    }

    @Override
    public void updateDescription() {
        this.description = String.format(this.rawDescription, this.amount2);
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        DomainCardsPanel.inst.stackDomainEffect(ID, stackAmount);
    }

    @Override
    public void atStartOfTurn() {
        if (owner.hasPower(ThenWeReunionPower.POWER_ID)) {
            return;
        }
        triggerEffect();
    }

    public void triggerEffect() {
        this.flash();
        effect.run();
        addToBot(new ReducePowerAction(owner, owner, this, 1));
        DomainCardsPanel.inst.reduceDomainAmount(ID);
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof OnDomainTriggerSubscriber) {
                ((OnDomainTriggerSubscriber) power).onDomainTrigger(Collections.singletonList(this));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card instanceof OnDomainTriggerSubscriber) {
                ((OnDomainTriggerSubscriber) card).onDomainTrigger(Collections.singletonList(this));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card instanceof OnDomainTriggerSubscriber) {
                ((OnDomainTriggerSubscriber) card).onDomainTrigger(Collections.singletonList(this));
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card instanceof OnDomainTriggerSubscriber) {
                ((OnDomainTriggerSubscriber) card).onDomainTrigger(Collections.singletonList(this));
            }
        }
    }

    @Override
    public void onRemove() {
        for (AbstractPower power : owner.powers) {
            if (power instanceof OnDomainRemoveSubscriber) {
                ((OnDomainRemoveSubscriber) power).onDomainRemove(this);
            }
        }
        DomainCardsPanel.inst.removeDomainCard(ID);
    }

    @Override
    public AbstractPower makeCopy() {
        return new DomainPower(domainCard, owner, amount, domainCard.cardID + "-" + UUID.randomUUID(), name, effect, description, amount2);
    }
}
