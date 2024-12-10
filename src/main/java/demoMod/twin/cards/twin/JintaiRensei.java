package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RepairPower;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.enums.CardTagsEnum;

public class JintaiRensei extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("JintaiRensei");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/JintaiRensei";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 5;

    public JintaiRensei() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.tags.add(CardTagsEnum.COPORATE);
        this.baseMagicNumber = this.magicNumber = 4;
        this.exhaust = true;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> upgradeMagicNumber(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new OfferingEffect(), 1.0F));
        addToBot(new LoseHPAction(p, p, p.currentHealth / 2));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p.currentHealth / 2 > 0) {
                    addToTop(new ApplyPowerAction(p, p, new RepairPower(p, p.lastDamageTaken)));
                }
                isDone = true;
            }
        });
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.increaseMaxHp(magicNumber, true);
                isDone = true;
            }
        });
        resetCoporateState();
    }
}
