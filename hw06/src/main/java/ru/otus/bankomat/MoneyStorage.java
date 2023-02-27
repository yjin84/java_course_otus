package ru.otus.bankomat;

public class MoneyStorage {
    private final NominalType nominalType;
    private int count;
    private long sum;

    private MoneyStorage(NominalType nominalType) {
        this.nominalType = nominalType;
        this.count = 0;
        this.sum = 0;
    }

    public static MoneyStorage newInstance(NominalType nominalType) {
        return new MoneyStorage(nominalType);
    }

    public long getSum() {
        return sum;
    }

    public int getCount() {
        return count;
    }

    public boolean applyOperation(MoneyStorageOperation operation) {
        int newCount = operation.apply(count);

        if (newCount < 0) {
            return false;
        }

        count = newCount;

        recalculateSum();

        return true;
    }

    private void recalculateSum() {
        sum = nominalType.getAmount() * count;
    }
}
