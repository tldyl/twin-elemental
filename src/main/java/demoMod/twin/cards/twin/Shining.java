package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.BoostAction;
import demoMod.twin.actions.SwitchLeaderAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.stances.Blaze;

import java.util.function.Supplier;

public class Shining extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("Shining");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public Shining() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.baseBlock = 4;
        this.baseMagicNumber = this.magicNumber = 4;
        this.tags.add(CardTagsEnum.PREFER_BLAZE);
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            upgradeBlock(2);
            upgradeMagicNumber(2);
        };
    }

    @Override
    protected Supplier<Boolean> getGlowCondition() {
        return () -> AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1 < magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (getGlowCondition().get()) {
                    addToTop(new SwitchLeaderAction());
                    if (p.stance instanceof Blaze) {
                        addToTop(new BoostAction());
                    } else {
                        addToTop(new GainEnergyAction(1));
                    }
                }
                isDone = true;
            }
        });
    }
}
