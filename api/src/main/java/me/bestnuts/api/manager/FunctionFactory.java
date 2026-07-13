package me.bestnuts.api.manager;

import me.bestnuts.api.bukkit.register.VehicleFunctionHook;
import me.bestnuts.api.model.vehicle.component.bone.VehicleEntity;
import me.bestnuts.api.model.vehicle.component.function.VehicleFunction;
import me.bestnuts.api.model.vehicle.data.FunctionFactorySender;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FunctionFactory implements Factory<FunctionFactorySender, List<VehicleFunction>> {

    private static final Pattern mainPattern = Pattern.compile("(\\w+):\\{(.+)}");
    private static final Pattern pairPattern = Pattern.compile("(\\w+):([^,\\s]+)");

    @Override
    public @NotNull List<VehicleFunction> generate(@NotNull FunctionFactorySender sender) {
        List<VehicleFunction> functions = new ArrayList<>();
        ConfigurationSection section = sender.section();
        for (String key : section.getKeys(false)) {
            int delay = Integer.parseInt(key);
            for (String input : section.getStringList(key)) {
                VehicleFunction function = getFunction(sender.entity(), delay, input);
                if (function == null) continue;
                functions.add(function);
            }
        }

        return functions;
    }

    private @Nullable VehicleFunction getFunction(@NotNull VehicleEntity entity, int delay, @NotNull String input) {
        Matcher mainMatcher = mainPattern.matcher(input);
        if (!mainMatcher.find()) return null;
        String name = mainMatcher.group(1);
        String content = mainMatcher.group(2);
        Map<String, String> param = new HashMap<>();
        Matcher pairMatcher = pairPattern.matcher(content);
        while (pairMatcher.find()) {
            String key = pairMatcher.group(1).trim();
            String value = pairMatcher.group(2).trim();
            param.put(key, value);
        }
        FunctionCreator creator = VehicleFunctionHook.getHook(name);
        if (creator == null) return null;
        return creator.create(entity, delay, param);
    }
}
