package economic;

import core.seri.Seri;
import ecosystem.entities.categories.TransactionalEntity;
import ecosystem.entities.transactional.Bank;

public class Account implements Seri {
    public Bank mBank;
    public TransactionalEntity mEntity;
    public double mValue;

    public Account (Bank bank, TransactionalEntity entity, double value){
        mBank = bank;
        mEntity = entity;
        mValue = value;
    }
}
