package demoMod.twin.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.cards.twin.GatherDomain;

public class HandWarmer extends CustomPotion {
    public static final String ID = TwinElementalMod.makeID("HandWarmer");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.COMMON;
    private static final PotionSize POTION_SIZE = PotionSize.H;
    private static final PotionColor POTION_COLOR = PotionColor.ELIXIR;

    public HandWarmer() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = TwinElementalMod.mainTwinColor;
        this.liquidColor = Color.RED.cpy();
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new GainEnergyAction(1));
        AbstractTwinCard card = new GatherDomain();
        card.traceAmount = getPotency();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, card.getDomainEffect().get()));
    }

    @Override
    public void initializeData() {
        this.description = String.format(potionStrings.DESCRIPTIONS[0], getPotency());
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new HandWarmer();
    }
}
