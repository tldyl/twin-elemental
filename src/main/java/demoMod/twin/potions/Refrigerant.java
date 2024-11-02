package demoMod.twin.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.enums.CardTagsEnum;

public class Refrigerant extends CustomPotion {
    public static final String ID = TwinElementalMod.makeID("Refrigerant");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
    private static final String NAME = potionStrings.NAME;
    private static final PotionRarity RARITY = PotionRarity.UNCOMMON;
    private static final PotionSize POTION_SIZE = PotionSize.BOTTLE;
    private static final PotionColor POTION_COLOR = PotionColor.ELIXIR;

    public Refrigerant() {
        super(NAME, ID, RARITY, POTION_SIZE, POTION_COLOR);
        this.isThrown = false;
        this.targetRequired = false;
        this.labOutlineColor = TwinElementalMod.mainTwinColor;
        this.liquidColor = Color.BLUE.cpy();
    }

    @Override
    public void initializeData() {
        this.description = potionStrings.DESCRIPTIONS[0];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(BaseMod.getKeywordProper(potionStrings.DESCRIPTIONS[1]), BaseMod.getKeywordDescription("twin:coporate")));
    }

    @Override
    public void use(AbstractCreature target) {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.hasTag(CardTagsEnum.COPORATE) && card instanceof AbstractTwinCard) {
                AbstractTwinCard twinCard = (AbstractTwinCard) card;
                while (twinCard.costForTurn > 0) {
                    twinCard.checkCoporateState();
                }
                twinCard.superFlash();
                twinCard.applyPowers();
            }
        }
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new Refrigerant();
    }
}
