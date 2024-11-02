package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.twin.AbstractTwinCard;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.powers.DomainPower;

import java.util.ArrayList;
import java.util.List;

public class CrossStar extends CustomRelic implements CustomSavable<List<String>> {
    public static final String ID = TwinElementalMod.makeID("CrossStar");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/crossStar.png"));
    public static final Texture OUTLINE_IMG = new Texture(TwinElementalMod.getResourcePath("relics/crossStar_outline.png"));

    private List<String> cardIds = new ArrayList<>();

    public CrossStar() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onVictory() {
        cardIds.clear();
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof DomainPower) {
                String cardId = power.ID.split("-")[0];
                if (!cardIds.contains(cardId)) {
                    cardIds.add(cardId);
                }
            }
        }
    }

    @Override
    public void atBattleStart() {
        if (!cardIds.isEmpty()) {
            this.flash();
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new RelicAboveCreatureAction(p, this));
            for (String cardId : cardIds) {
                AbstractCard card = CardLibrary.getCard(cardId);
                if (!(card instanceof AbstractTwinCard) || !card.hasTag(CardTagsEnum.DOMAIN)) {
                    continue;
                }
                AbstractTwinCard twinCard = (AbstractTwinCard) card;
                twinCard.traceAmount = 1;
                twinCard.magicNumber = 1;
                addToBot(new ApplyPowerAction(p, p, twinCard.getDomainEffect().get()));
            }
        }
    }

    @Override
    public List<String> onSave() {
        return cardIds;
    }

    @Override
    public void onLoad(List<String> cardIds) {
        if (cardIds != null) {
            this.cardIds = cardIds;
        }
    }
}
