package ru.otus.config;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.EquationPreparer;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;
import ru.otus.services.IOService;
import ru.otus.services.PlayerService;

@AppComponentsContainerConfig(order = 2)
public class ExtendsConfig {
    @AppComponent(order = 0, name = "gameProcessor2")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
