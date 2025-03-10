package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Use the accountDAO to register a new account
     * Username must not be blank
     * Password must be at least 4 characters
     * Account must not already exist
     * @param account an account object
     * @return account if it was successfully persisted, null if it was not successfully persisted
     */
    public Account registerNewAccount(Account account) {
        if (account.getUsername().isBlank())
            return null;
        if (account.getPassword().isBlank() || account.getPassword().length() < 4)
            return null;
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            return null;
        }
        else return accountDAO.addAccount(account);
    }

    /**
     * Use the accountDAO to verify a login attempt
     * Username must match an existing account
     * Password must be correct
     * @param account an account object
     * @return account if it was successfully persisted, null if it was not successfully persisted
     */
    public Account verifyLogin(Account account) {
        Account storedAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (storedAccount == null)
            return null;
        else if (account.getPassword().equals(storedAccount.getPassword()))
            return storedAccount;
        else return null;
    }
}
