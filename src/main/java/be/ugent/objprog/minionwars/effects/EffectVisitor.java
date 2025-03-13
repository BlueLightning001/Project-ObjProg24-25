package be.ugent.objprog.minionwars.effects;

import be.ugent.objprog.minionwars.minions.Minion;

public interface EffectVisitor {
    void visit(Minion minion);
}
