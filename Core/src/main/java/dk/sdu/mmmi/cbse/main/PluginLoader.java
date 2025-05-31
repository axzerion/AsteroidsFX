package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

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

    public static List<IGamePluginService> loadPlugins(String pluginDir, String moduleName) {
        try {
            Path pluginPath = Path.of(pluginDir);
            ModuleFinder finder = ModuleFinder.of(pluginPath);
            ModuleLayer parent = ModuleLayer.boot();

            Configuration config = parent.configuration()
                    .resolve(finder, ModuleFinder.of(), Set.of(moduleName));

            // Build a URLClassLoader to isolate the plugin layer
            List<URL> jarUrls = Files.list(pluginPath)
                    .filter(path -> path.toString().endsWith(".jar"))
                    .map(PluginLoader::toURL)
                    .collect(Collectors.toList());

            ClassLoader pluginClassLoader = new URLClassLoader(
                    jarUrls.toArray(new URL[0]),
                    null 
            );

            ModuleLayer layer = parent.defineModulesWithOneLoader(config, pluginClassLoader);

            ServiceLoader<IGamePluginService> loader = ServiceLoader.load(layer, IGamePluginService.class);
            return loader.stream().map(ServiceLoader.Provider::get).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load plugin module: " + moduleName, e);
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
