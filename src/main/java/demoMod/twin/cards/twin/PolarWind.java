package demoMod.twin.cards.twin;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import demoMod.twin.TwinElementalMod;
import demoMod.twin.actions.XCostAction;
import demoMod.twin.enums.CardTagsEnum;
import demoMod.twin.helpers.DomainGenerator;
import demoMod.twin.stances.Freeze;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PolarWind extends AbstractTwinCard {
    public static final String ID = TwinElementalMod.makeID("PolarWind");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "cards/todo";

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -1;

    public PolarWind() {
        super(ID, NAME, TwinElementalMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET);
        this.exhaust = true;
        this.tags.add(CardTagsEnum.PREFER_FREEZE);
        this.tags.add(CardTagsEnum.DOMAIN);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!(p.stance instanceof Freeze)) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        }
        return super.canUse(p, m) && p.stance instanceof Freeze;
    }

    @Override
    public Runnable getUpgradeAction() {
        return () -> {
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        };
    }

    @Override
    public Supplier<AbstractPower> getDomainEffect() {
        AbstractPlayer p = AbstractDungeon.player;
        return () -> DomainGenerator.getDomain(this, p, () -> {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                addToBot(new ApplyPowerAction(mo, p, new StrengthPower(mo, -1)));
            }
        }, cardStrings.EXTENDED_DESCRIPTION[0], 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new XCostAction(this, effect -> {
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters.stream().filter(monster -> !monster.isDeadOrEscaped()).collect(Collectors.toList())) {
                addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, effect, false)));
            }
            if (upgraded) {
                this.traceAmount = effect;
                addToBot(new ApplyPowerAction(p, p, getDomainEffect().get()));
            }
        }, this.energyOnUse + (upgraded ? 1 : 0)));
    }
}
