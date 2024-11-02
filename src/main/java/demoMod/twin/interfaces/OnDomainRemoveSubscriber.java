package demoMod.twin.interfaces;

import demoMod.twin.powers.DomainPower;

public interface OnDomainRemoveSubscriber {
    void onDomainRemove(DomainPower domainPower);
}
