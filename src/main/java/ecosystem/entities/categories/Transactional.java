package ecosystem.entities.categories;


import core.Entity;
import core.seri.wrapers.SeriMap;
import ecosystem.entities.core.SeriEntities;

abstract public class Transactional extends Entity {
    // Assets
    public double mCapital;
    public SeriEntities mAssets = new SeriEntities();
    // Liabilities
    public SeriEntities mCredits = new SeriEntities();
    // Expenses
    public transient SeriMap<Class<Entity>, Double> mExpensesRatios = new SeriMap<>();

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
