package ru.otus.bankomat;

import java.util.List;

public interface Bankomat {
    void deposit(List<NominalType> banknotes);
    long balance();
    void withdraw(int amount);
}
