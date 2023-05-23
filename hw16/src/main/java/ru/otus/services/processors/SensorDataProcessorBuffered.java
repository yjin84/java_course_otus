package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private final BlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(
                bufferSize,
                Comparator.comparing(SensorData::getMeasurementTime)
        );
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);

        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public synchronized void flush() {
        try {
            List<SensorData> bufferedData = new ArrayList<>(bufferSize);

            SensorData sensorData;
            while ((sensorData = dataBuffer.poll()) != null) {
                bufferedData.add(sensorData);
            }

            if (bufferedData.isEmpty()) {
                return;
            }

            writer.writeBufferedData(bufferedData);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
