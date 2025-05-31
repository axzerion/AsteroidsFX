package dk.sdu.mmmi.cbse.main;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginLoader {

    public static Set<String> discoverModules(String pluginDir) {
        try {
            Path pluginPath = Path.of(pluginDir);
            ModuleFinder finder = ModuleFinder.of(pluginPath);
            Set<ModuleReference> modules = finder.findAll();
            return modules.stream()
                    .map(moduleRef -> moduleRef.descriptor().name())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new RuntimeException("Failed to discover plugin modules", e);
        }
    }

    public static <T> List<T> loadPlugins(String pluginDir, Set<String> moduleNames, Class<T> serviceType) {
        try {
            Path pluginPath = Path.of(pluginDir);
            ModuleFinder finder = ModuleFinder.of(pluginPath);
            ModuleLayer parent = ModuleLayer.boot();

            Configuration config = parent.configuration().resolve(finder, ModuleFinder.of(), moduleNames);

            List<URL> jarUrls = Files.list(pluginPath)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .map(PluginLoader::toURL)
                    .collect(Collectors.toList());

            ClassLoader pluginClassLoader = new URLClassLoader(
                    jarUrls.toArray(new URL[0]),
                    null
            );

            ModuleLayer layer = parent.defineModulesWithOneLoader(config, pluginClassLoader);

            ServiceLoader<T> loader = ServiceLoader.load(layer, serviceType);
            return loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load plugins for service: " + serviceType.getName(), e);
        }
    }

    private static URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid path: " + path, e);
        }
    }
}
