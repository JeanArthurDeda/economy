package ecosystem.entities.transactional;


import core.SeriList;
import economic.Account;
import ecosystem.entities.categories.TransactionalEntity;

public class Bank extends TransactionalEntity{
    public double mReserve;
    public SeriList<Account> mAccounts = new SeriList<>();

    // Accounts
    public Account openAccount (TransactionalEntity entity, double value){
        Account account = new Account(this, entity, value);
        mAccounts.add (account);
        return account;
    }
}
