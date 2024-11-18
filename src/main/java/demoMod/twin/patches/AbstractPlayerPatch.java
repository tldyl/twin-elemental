package demoMod.twin.patches;

import basemod.BaseMod;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.cards.twin.Monopoly;
import demoMod.twin.ui.DomainCardsPanel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractPlayerPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PatchPreBattlePrep {
        public static void Postfix(AbstractPlayer p) {
            DomainCardsPanel.inst = new DomainCardsPanel();
            BaseMod.subscribe(DomainCardsPanel.inst);
            List<AbstractCard> monopolies = p.masterDeck.group.stream().filter(card -> card instanceof Monopoly).collect(Collectors.toList());
            monopolies.forEach(card -> {
                Monopoly monopoly = (Monopoly) card;
                for (Map.Entry<String, List<Integer>> e : monopoly.cardSave.entrySet()) {
                    for (Integer upgradeTimes : e.getValue()) {
                        AbstractCard cardToPlay = CardLibrary.getCard(e.getKey()).makeCopy();
                        for (int i=0;i<upgradeTimes;i++) {
                            cardToPlay.upgrade();
                        }
                        AbstractDungeon.player.limbo.group.add(cardToPlay);
                        cardToPlay.current_y = -200.0F * Settings.scale;
                        cardToPlay.target_x = (float)Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
                        cardToPlay.target_y = (float)Settings.HEIGHT / 2.0F;
                        cardToPlay.targetAngle = 0.0F;
                        cardToPlay.lighten(false);
                        cardToPlay.drawScale = 0.12F;
                        cardToPlay.targetDrawScale = 0.75F;
                        cardToPlay.applyPowers();
                        AbstractMonster target = AbstractDungeon.getRandomMonster();
                        if (target == null || target.isDying || target.isDeadOrEscaped()) {
                            AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(cardToPlay, true, false, true));
                        } else {
                            AbstractDungeon.actionManager.addToTop(new NewQueueCardAction(cardToPlay, target, false, true));
                        }
                        AbstractDungeon.actionManager.addToTop(new UnlimboAction(cardToPlay));
                    }
                }
                monopoly.cardSave.clear();
                MultiCardPreview.clear(monopoly);
            });
            monopolies = p.drawPile.group.stream().filter(card -> card instanceof Monopoly).collect(Collectors.toList());
            monopolies.forEach(card -> {
                Monopoly monopoly = (Monopoly) card;
                monopoly.cardSave.clear();
                MultiCardPreview.clear(monopoly);
            });
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "onVictory"
    )
    public static class PatchOnVictory {
        public static void Postfix(AbstractPlayer p) {
            DomainCardsPanel.inst.clearDomainCards();
            BaseMod.unsubscribe(DomainCardsPanel.inst);
        }
    }
}
