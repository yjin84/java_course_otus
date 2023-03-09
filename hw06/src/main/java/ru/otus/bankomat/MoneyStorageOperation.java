package ru.otus.bankomat;

public class MoneyStorageOperation {
    public enum OperationType {
        INCREMENT,
        DECREMENT;
    }

    private int count;

    private OperationType type;

    private MoneyStorageOperation(int count, OperationType type) {
        this.count = count;
        this.type = type;
    }

    public static MoneyStorageOperation newIncrementOperation(int count) {
        return new MoneyStorageOperation(count, OperationType.INCREMENT);
    }

    public static MoneyStorageOperation newDecrementOperation(int count) {
        return new MoneyStorageOperation(count, OperationType.DECREMENT);
    }

    public int apply(int sourceCount) {
        return type == OperationType.INCREMENT ? sourceCount + count : sourceCount - count;
    }

    public int getCount() {
        return count;
    }
}
