package demoMod.twin.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.interfaces.OnDomainRemoveSubscriber;
import demoMod.twin.interfaces.OnDomainTriggerSubscriber;

import java.util.Collections;

public class DomainPower extends TwoAmountPower {
    private final String rawDescription;
    private final Runnable effect;

    public DomainPower(AbstractCreature owner, int amount, String id, String name, Runnable effect, String description, int magicNumber) {
        this.ID = id;
        this.name = name;
        this.owner = owner;
        this.amount = amount;
        this.effect = effect;
        this.rawDescription = description;
        this.amount2 = magicNumber;
        updateDescription();
        loadRegion("afterImage");
    }

    @Override
    public void updateDescription() {
        this.description = String.format(this.rawDescription, this.amount2);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        effect.run();
        addToBot(new ReducePowerAction(owner, owner, this, 1));
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
    }
}
