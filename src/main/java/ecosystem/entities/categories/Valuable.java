package ecosystem.entities.categories;


import core.Entity;

abstract public class Valuable extends Entity {
    public double mNumUnits;

    public double getNumUnits (){
        return mNumUnits;
    }

    public void setNumUnits(double value){
        mNumUnits = value;
    }
}
