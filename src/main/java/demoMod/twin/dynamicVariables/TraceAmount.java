package demoMod.twin.dynamicVariables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import demoMod.twin.cards.twin.AbstractTwinCard;

public class TraceAmount extends DynamicVariable {
    @Override
    public String key() {
        return "twin:TA";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractTwinCard) {
            AbstractTwinCard twinCard = (AbstractTwinCard) card;
            return twinCard.traceAmount != twinCard.baseTraceAmount;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractTwinCard) {
            AbstractTwinCard twinCard = (AbstractTwinCard) card;
            return twinCard.traceAmount;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractTwinCard) {
            AbstractTwinCard twinCard = (AbstractTwinCard) card;
            return twinCard.baseTraceAmount;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractTwinCard) {
            AbstractTwinCard twinCard = (AbstractTwinCard) card;
            return twinCard.traceAmount > twinCard.baseTraceAmount;
        }
        return false;
    }
}
