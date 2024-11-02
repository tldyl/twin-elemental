package demoMod.twin.interfaces;

import demoMod.twin.powers.DomainPower;

import java.util.List;

public interface OnDomainTriggerSubscriber {
    void onDomainTrigger(List<DomainPower> domains);
}
