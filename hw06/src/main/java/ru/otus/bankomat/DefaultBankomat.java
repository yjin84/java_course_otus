package ru.otus.bankomat;

import java.util.List;

public class DefaultBankomat implements Bankomat {
    private final MoneyStorageManager moneyStorageManager;

    public DefaultBankomat(MoneyStorageManager moneyStorageManager) {
        this.moneyStorageManager = moneyStorageManager;
    }

    public void deposit(List<NominalType> banknotes) {
        moneyStorageManager.deposit(banknotes);
    }

    public long balance() {
        return moneyStorageManager.balance();
    }

    public void withdraw(int amount) {
        moneyStorageManager.withdraw(amount);
    }
}
