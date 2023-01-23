package ru.otus.homework;


import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> navigableMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return getImmutableEntryWithCopiedKey(navigableMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getImmutableEntryWithCopiedKey(navigableMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        navigableMap.put(customer, data);
    }

    private static Map.Entry<Customer, String> getImmutableEntryWithCopiedKey(Map.Entry<Customer, String> entry) {
        return entry != null ? Map.entry(getCustomerCopy(entry.getKey()), entry.getValue()) : null;
    }

    private static Customer getCustomerCopy(Customer src) {
        return src != null ? new Customer(src.getId(), src.getName(), src.getScores()) : null;
    }
}
