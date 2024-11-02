package demoMod.twin.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.interfaces.OnDomainTriggerSubscriber;
import demoMod.twin.powers.DomainPower;

import java.util.ArrayList;
import java.util.List;

public class BoostAction extends AbstractGameAction {
    @Override
    public void update() {
        List<DomainPower> domainPowers = new ArrayList<>();
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof DomainPower) {
                power.atStartOfTurn();
                domainPowers.add((DomainPower) power);
            }
        }
        if (!domainPowers.isEmpty()) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof OnDomainTriggerSubscriber) {
                    ((OnDomainTriggerSubscriber) power).onDomainTrigger(domainPowers);
                }
            }
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card instanceof OnDomainTriggerSubscriber) {
                    ((OnDomainTriggerSubscriber) card).onDomainTrigger(domainPowers);
                }
            }
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof OnDomainTriggerSubscriber) {
                    ((OnDomainTriggerSubscriber) card).onDomainTrigger(domainPowers);
                }
            }
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (card instanceof OnDomainTriggerSubscriber) {
                    ((OnDomainTriggerSubscriber) card).onDomainTrigger(domainPowers);
                }
            }
        }
        isDone = true;
    }
}
