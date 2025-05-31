package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
class ModuleConfig {

    public ModuleConfig() {
    }

    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServices,
                     List<IPostEntityProcessingService> postEntityProcessingServices) {
        return new Game(gamePluginServices, entityProcessingServices, postEntityProcessingServices);
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        // Load all discovered plugin modules from the /plugins folder
        Set<String> moduleNames = PluginLoader.discoverModules("plugins");
        return PluginLoader.loadPlugins("plugins", moduleNames, IGamePluginService.class);
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServiceList() {
        Set<String> moduleNames = PluginLoader.discoverModules("plugins");
        return PluginLoader.loadPlugins("plugins", moduleNames, IEntityProcessingService.class);
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        Set<String> moduleNames = PluginLoader.discoverModules("plugins");
        return PluginLoader.loadPlugins("plugins", moduleNames, IPostEntityProcessingService.class);
    }
}