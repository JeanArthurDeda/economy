package economic;

import core.seri.Seri;
import ecosystem.entities.categories.Transactional;
import ecosystem.entities.transactional.Bank;

public class Account implements Seri {
    public Bank mBank;
    public Transactional mEntity;
    public double mValue;

    public Account(Bank Bank, Transactional entity, double value){
        mBank = Bank;
        mEntity = entity;
        mValue = value;
    }
}
