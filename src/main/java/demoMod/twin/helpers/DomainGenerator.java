package demoMod.twin.helpers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.powers.DomainPower;

import java.util.UUID;

public class DomainGenerator {
    public static AbstractPower getDomain(AbstractTwinCard card, AbstractCreature owner, Runnable effect, String description, int magicNumber) {
        return new DomainPower(card, owner, card.traceAmount, card.cardID + "-" + UUID.randomUUID(), card.name, effect, description, magicNumber);
    }
}
