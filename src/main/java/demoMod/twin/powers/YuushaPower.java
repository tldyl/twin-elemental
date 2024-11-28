package demoMod.twin.powers;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.helpers.PowerRegionLoader;
import demoMod.twin.interfaces.OnDomainRemoveSubscriber;

public class YuushaPower extends AbstractPower implements OnDomainRemoveSubscriber {
    public static final String POWER_ID = TwinElementalMod.makeID("YuushaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESC = powerStrings.DESCRIPTIONS;

    public YuushaPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        PowerRegionLoader.load(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESC[0];
    }

    @Override
    public void onDomainRemove(DomainPower domainPower) {
        this.flash();
        String cardId = domainPower.ID.split("-")[0];
        AbstractCard cardToPlay = null;
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.cardID.equals(cardId)) {
                cardToPlay = card;
                break;
            }
        }
        if (cardToPlay instanceof AbstractTwinCard) {
            AbstractDungeon.player.drawPile.removeCard(cardToPlay);
            addToBot(new NewQueueCardAction(cardToPlay, true, false, true));
        }
    }
}
