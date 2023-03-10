package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HistoryListener implements Listener, HistoryReader {

    private final Set<Message> history = new HashSet<>();

    @Override
    public void onUpdated(Message msg) {
        var logString = String.format("add msg:%s", msg);
        System.out.println(logString);

        history.add(copyMsg(msg));
    }

    private Message copyMsg(Message msg) {
        var field13 = msg.getField13();
        var objectForMessage = new ObjectForMessage();

        if (field13 != null) {
            objectForMessage.setData(field13.getData() != null ? List.copyOf(field13.getData()) : null);
        }

        return msg.toBuilder().field13(objectForMessage).build();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return history.stream().filter(item -> item.getId() == id).findFirst();
    }
}
