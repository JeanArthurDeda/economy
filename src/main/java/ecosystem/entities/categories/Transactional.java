package ecosystem.entities.categories;


import core.Entity;
import ecosystem.entities.core.SeriEntities;

import java.util.HashMap;

abstract public class Transactional extends Entity {
    // Assets
    public double mCapital;
    public SeriEntities mAssets = new SeriEntities();
    // Liabilities
    public SeriEntities mCredits = new SeriEntities();
    // Expenses
    public transient HashMap<Class<Entity>, Double> mExpensesRatios = new HashMap<>();

    public Transactional(){
        super();
    }

    public enum State{
        ALIVE,
        DEAD
    }


    public State Tick (long time){
        return State.ALIVE;
    }
}
