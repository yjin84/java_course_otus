package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GsonResourcesFileLoader implements Loader {
    private final String fileName;

    private final Gson gson;

    public GsonResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public List<Measurement> load() {
        try (var isr = new InputStreamReader(GsonResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            return Arrays.asList(gson.fromJson(isr, Measurement[].class));
        } catch (Exception ex) {
            throw new FileProcessException(ex);
        }
    }
}
