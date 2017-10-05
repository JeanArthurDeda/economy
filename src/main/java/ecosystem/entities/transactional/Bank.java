package ecosystem.entities.transactional;


import core.seri.wrapers.SeriList;
import economic.Account;
import ecosystem.entities.categories.Transactional;

public class Bank extends Transactional {
    public double mReserve;
    public SeriList<Account> mAccounts = new SeriList<>();

    // Accounts
    public Account openAccount (Transactional entity, double value){
        Account account = new Account(this, entity, value);
        mAccounts.add (account);
        return account;
    }
}
