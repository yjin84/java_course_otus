package ru.otus.homework;


import java.util.*;

public class CustomerService {

    private final Map<Customer, String> sortedMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return getImmutableEntryWithCopiedKey(((TreeMap<Customer, String>) sortedMap).firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getImmutableEntryWithCopiedKey(((TreeMap<Customer, String>) sortedMap).higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        sortedMap.put(customer, data);
    }

    private static Map.Entry<Customer, String> getImmutableEntryWithCopiedKey(Map.Entry<Customer, String> entry) {
        return entry != null ? Map.entry(getCustomerCopy(entry.getKey()), entry.getValue()) : null;
    }

    private static Customer getCustomerCopy(Customer src) {
        return src != null ? new Customer(src.getId(), src.getName(), src.getScores()) : null;
    }
}
