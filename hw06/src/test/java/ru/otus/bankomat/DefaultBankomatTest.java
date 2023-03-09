package ru.otus.bankomat;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBankomatTest {

    private final DefaultBankomat bankomat = new DefaultBankomat(MoneyStorageManager.newInstance());

    @Test
    void depositSuccess() {
        bankomat.deposit(Arrays.asList(NominalType.FIVE_THOUSAND, NominalType.FIVE_HUNDRED, NominalType.FIFTY));
        assertEquals(5550L, bankomat.balance());
    }

    @Test
    void withdrawSuccess() {
        bankomat.deposit(Arrays.asList(
                NominalType.FIVE_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.ONE_THOUSAND,
                NominalType.FIVE_HUNDRED,
                NominalType.ONE_HUNDRED,
                NominalType.FIFTY
        ));

        long initBalance = bankomat.balance();

        int withdrawAmount = 3550;

        long expectedBalance = initBalance - withdrawAmount;

        bankomat.withdraw(withdrawAmount);

        assertEquals(expectedBalance, bankomat.balance());
    }

    @Test
    void withdrawFailOverBalance() {
        bankomat.deposit(Arrays.asList(
                NominalType.FIVE_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.ONE_THOUSAND,
                NominalType.FIVE_HUNDRED,
                NominalType.ONE_HUNDRED,
                NominalType.FIFTY
        ));

        var thrown = assertThrows(IllegalStateException.class, () -> bankomat.withdraw(11000));

        assertTrue(thrown.getMessage().contains("not enough money"));
    }

    @Test
    void withdrawFailWrongAmount() {
        bankomat.deposit(Arrays.asList(
                NominalType.FIVE_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.ONE_THOUSAND,
                NominalType.FIVE_HUNDRED,
                NominalType.ONE_HUNDRED,
                NominalType.FIFTY
        ));

        var thrown = assertThrows(IllegalArgumentException.class, () -> bankomat.withdraw(11005));

        assertTrue(thrown.getMessage().contains("you can only enter amounts that are multiples of"));
    }

    @Test
    void withdrawFailNoSuitableCombination() {
        bankomat.deposit(Arrays.asList(
                NominalType.FIVE_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.TWO_THOUSAND,
                NominalType.FIVE_HUNDRED,
                NominalType.ONE_HUNDRED,
                NominalType.FIFTY
        ));

        var thrown = assertThrows(IllegalStateException.class, () -> bankomat.withdraw(8000));

        assertTrue(thrown.getMessage().contains("no suitable combination"));
    }
}