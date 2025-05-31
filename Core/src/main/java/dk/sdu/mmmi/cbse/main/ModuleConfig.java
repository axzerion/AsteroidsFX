package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
class ModuleConfig {

    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServices,
                     List<IPostEntityProcessingService> postEntityProcessingServices) {
        return new Game(gamePluginServices, entityProcessingServices, postEntityProcessingServices);
    }

    @Bean
    public List<IGamePluginService> IgamePluginServices() {
        var services = ServiceLocator.INSTANCE.locateAll(IGamePluginService.class);
        System.out.println("Located IGamePluginService count: " + services.size());
        return services;
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServiceList() {
        List<IEntityProcessingService> services = ServiceLocator.INSTANCE.locateAll(IEntityProcessingService.class);
        System.out.println("Located IEntityProcessingService count: " + services.size());
        return services;
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        List<IPostEntityProcessingService> services = ServiceLocator.INSTANCE.locateAll(IPostEntityProcessingService.class);
        System.out.println("Located IPostEntityProcessingService count: " + services.size());
        return services;
    }
}
