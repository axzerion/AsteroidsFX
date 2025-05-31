package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModuleConfig {
    
    public ModuleConfig() {
    }

    @Bean
    public Game game() {
        return new Game();
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServiceList() {
        return ServiceLocator.INSTANCE.locateAll(IEntityProcessingService.class);
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return ServiceLocator.INSTANCE.locateAll(IGamePluginService.class).stream()
                .filter(plugin -> !plugin.getClass().getModule().getName().equals("enemysystem"))
                .toList();
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices() {
        return ServiceLocator.INSTANCE.locateAll(IPostEntityProcessingService.class);
    }
}
