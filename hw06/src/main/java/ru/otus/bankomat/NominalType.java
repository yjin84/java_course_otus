package ru.otus.bankomat;

public enum NominalType {
    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1_000),
    TWO_THOUSAND(2_000),
    FIVE_THOUSAND(5_000);

    private final int amount;

    NominalType(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
