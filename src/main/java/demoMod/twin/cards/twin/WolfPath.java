package demoMod.twin.cards.twin;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

public class WolfPath extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("WolfPath");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public WolfPath() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeBaseCost(0);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance instanceof Blaze) {
            addToBot(new DrawCardAction(BaseMod.MAX_HAND_SIZE - p.hand.size(), new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractCard card : p.hand.group) {
                        addToTop(new AbstractGameAction() {
                            @Override
                            public void update() {
                                if (card.type != CardType.ATTACK) {
                                    AbstractDungeon.player.hand.moveToDiscardPile(card);
                                    card.triggerOnManualDiscard();
                                    GameActionManager.incrementDiscard(false);
                                }
                                isDone = true;
                            }
                        });
                    }
                    isDone = true;
                }
            }));
        } else {
            addToBot(new GainEnergyAction(p.energy.energyMaster));
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    for (AbstractCard card : p.hand.group) {
                        addToTop(new AbstractGameAction() {
                            @Override
                            public void update() {
                                if (card.type != CardType.SKILL) {
                                    AbstractDungeon.player.hand.moveToDiscardPile(card);
                                    card.triggerOnManualDiscard();
                                    GameActionManager.incrementDiscard(false);
                                }
                                isDone = true;
                            }
                        });
                    }
                    isDone = true;
                }
            });
        }
    }
}
