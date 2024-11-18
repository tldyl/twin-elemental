package demoMod.twin.ui;

import basemod.interfaces.PostPlayerUpdateSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.cards.twin.AbstractTwinCard;

import java.util.*;

public class DomainCardsPanel implements PostPlayerUpdateSubscriber {
    public static DomainCardsPanel inst = new DomainCardsPanel();

    private AbstractTwinCard hoveredCard;
    private final Map<String, AbstractTwinCard> domainCardMap = new LinkedHashMap<>();

    public void addDomainCard(String powerUUID, AbstractTwinCard domainCard) {
        AbstractTwinCard cpyToAdd = (AbstractTwinCard) domainCard.makeSameInstanceOf();
        domainCardMap.put(powerUUID, cpyToAdd);
    }

    public void stackDomainEffect(String powerUUID, int turns) {
        AbstractTwinCard domainCard = getDomainCardByUUID(powerUUID);
        if (domainCard != null) {
            domainCard.traceAmount += turns;
        }
    }

    public void reduceDomainAmount(String powerUUID) {
        AbstractTwinCard domainCard = getDomainCardByUUID(powerUUID);
        if (domainCard != null) {
            domainCard.traceAmount--;
        }
    }

    public void removeDomainCard(String powerUUID) {
        AbstractTwinCard domainCard = getDomainCardByUUID(powerUUID);
        if (domainCard != null) {
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(domainCard, domainCard.hb.cX, domainCard.hb.cY));
            domainCard.target_x = domainCard.hb.cX;
            domainCard.target_y = domainCard.hb.cY;
            domainCardMap.remove(powerUUID);
        }
    }

    private AbstractTwinCard getDomainCardByUUID(String powerUUID) {
        return domainCardMap.get(powerUUID);
    }

    public void clearDomainCards() {
        for (Map.Entry<String, AbstractTwinCard> e : domainCardMap.entrySet()) {
            AbstractTwinCard domainCard = e.getValue();
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(domainCard, domainCard.hb.cX, domainCard.hb.cY));
            domainCard.target_x = domainCard.hb.cX;
            domainCard.target_y = domainCard.hb.cY;
        }
        domainCardMap.clear();
    }

    @Override
    public void receivePostPlayerUpdate() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractTwinCard newHoveredCard = null;
        float width = Math.min(600f, 150.0f * (domainCardMap.size() - 1)) * Settings.scale;
        float minX = p.drawX - width / 2;
        float stepX = domainCardMap.size() < 2 ? 0 : width / (domainCardMap.size() - 1);
        int i = 0;
        for (Map.Entry<String, AbstractTwinCard> e : domainCardMap.entrySet()) {
            AbstractTwinCard card = e.getValue();
            card.target_x = stepX * i + minX;
            card.target_y = p.hb_y + p.hb_h + 500 * Settings.scale;
            card.targetDrawScale = 0.5f;
            card.hb.update();
            if (card.hb.hovered && (hoveredCard == null || !hoveredCard.hb.hovered)) {
                newHoveredCard = card;
            }
            i++;
        }

        if (newHoveredCard != null) {
            hoveredCard = newHoveredCard;
        }

        if (hoveredCard != null && !hoveredCard.hb.hovered) {
            hoveredCard = null;
        }

        if (hoveredCard != null) {
            hoveredCard.targetDrawScale = 1f;
            hoveredCard.drawScale = 1f;
        }

        for (Map.Entry<String, AbstractTwinCard> e : domainCardMap.entrySet()) {
            e.getValue().update();
        }
    }

    public void render(SpriteBatch sb) {
        List<AbstractTwinCard> tmp = new ArrayList<>(domainCardMap.values());
        Collections.reverse(tmp);
        for (AbstractCard card : tmp) {
            if (card != hoveredCard) {
                card.render(sb);
            }
        }
        if (hoveredCard != null) {
            TwinElementalMod.renderable.add(hoveredCard::render);
        }
    }
}
