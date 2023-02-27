package ru.otus.bankomat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class MoneyStorageManager {
    private final NavigableMap<NominalType, MoneyStorage> storage;

    private MoneyStorageManager() {
        storage = new TreeMap<>(Comparator.comparingInt(NominalType::getAmount));
        for (var nominalType : NominalType.values()) {
            storage.put(nominalType, MoneyStorage.newInstance(nominalType));
        }
    }

    public static MoneyStorageManager newInstance() {
        return new MoneyStorageManager();
    }

    public void deposit(List<NominalType> banknotes) {
        if (banknotes == null || banknotes.isEmpty()) {
            return;
        }

        Map<NominalType, MoneyStorageOperation> detectMap = new HashMap<>(banknotes.size());

        for (var nominalType : banknotes) {
            detectMap.merge(nominalType, MoneyStorageOperation.newIncrementOperation(1), (k, v) -> MoneyStorageOperation.newIncrementOperation(v.getCount() + 1));
        }

        applyOperations(detectMap);
    }

    private void applyOperations(Map<NominalType, MoneyStorageOperation> detectMap) {
        for (var entry : detectMap.entrySet()) {
            var nominalType = entry.getKey();
            var moneyStorageOperation = entry.getValue();

            storage.get(nominalType).applyOperation(moneyStorageOperation);
        }
    }

    public long balance() {
        long sum = 0;
        for (var moneyStorage : storage.values()) {
            sum += moneyStorage.getSum();
        }

        return sum;
    }

    public void withdraw(int amount) {
        if (amount == 0) {
            throw new IllegalArgumentException("The operation will not be completed: sum must be greater than 0");
        }

        if (amount % storage.firstKey().getAmount() != 0) {
            throw new IllegalArgumentException("The operation will not be completed: you can only enter amounts that are multiples of " + storage.firstKey().getAmount());
        }

        if (amount > balance()) {
            throw new IllegalStateException("The operation will not be completed: not enough money");
        }

        Map<NominalType, MoneyStorageOperation> decrementMap = buildDecrementMap(amount);

        if (decrementMap == null) {
            throw new IllegalStateException("The operation will not be completed: no suitable combination");
        }

        applyOperations(decrementMap);
    }

    private Map<NominalType, MoneyStorageOperation> buildDecrementMap(int amount) {
        Map<NominalType, MoneyStorageOperation> decrementMap = new HashMap<>(storage.size());
        NominalType nextType = storage.lastKey();
        while (amount > 0 && nextType != null) {
            var currentType = nextType;
            nextType = storage.lowerKey(nextType);

            var moneyStorage = storage.get(currentType);

            if (currentType.getAmount() > amount || moneyStorage.getSum() == 0) {
                continue;
            }

            int count = amount / currentType.getAmount();

            if (count == 0) {
                continue;
            }

            if (count > moneyStorage.getCount()) {
                count = moneyStorage.getCount();
            }

            amount -= count * currentType.getAmount();
            decrementMap.put(currentType, MoneyStorageOperation.newDecrementOperation(count));
        }

        if (amount != 0) {
            return null;
        }

        return decrementMap;
    }

    public void printStorageInfo() {
        System.out.println("Total sum: " + balance());
        storage.forEach((nominalType, moneyStorage) -> System.out.println("Nominal: " + nominalType + ", count: " + moneyStorage.getCount() + ", sum: " + moneyStorage.getSum()));
    }
}
