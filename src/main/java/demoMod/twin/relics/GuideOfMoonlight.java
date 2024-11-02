package demoMod.twin.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.PlayCardInCardGroupAction;
import demoMod.twin.enums.CardTagsEnum;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GuideOfMoonlight extends CustomRelic {
    public static final String ID = TwinElementalMod.makeID("GuideOfMoonlight");
    public static final Texture IMG = new Texture(TwinElementalMod.getResourcePath("relics/guideOfMoonlight.png"));
    public static final Texture OUTLINE_IMG = new Texture(TwinElementalMod.getResourcePath("relics/guideOfMoonlight_outline.png"));

    public GuideOfMoonlight() {
        super(ID, IMG, OUTLINE_IMG, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new RelicAboveCreatureAction(p, this));
        List<AbstractCard> domainCards = p.drawPile.group.stream().filter(card -> card.hasTag(CardTagsEnum.DOMAIN)).collect(Collectors.toList());
        int playSize = 2;
        if (domainCards.size() > playSize) {
            Collections.shuffle(domainCards, new Random(AbstractDungeon.cardRandomRng.random.nextLong()));
            domainCards = domainCards.subList(0, playSize);
        }
        domainCards.forEach(card -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                addToTop(new PlayCardInCardGroupAction(card, p.drawPile, AbstractDungeon.getRandomMonster(), card1 -> {}));
                isDone = true;
            }
        }));
    }
}
