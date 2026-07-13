package me.bestnuts.api.bukkit.util;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public final class FunctionParamHelper {

    public static @NotNull Vector getVector(@NotNull String input, @NotNull Vector def) {
        String[] content = input.split(";");
         if (content.length < 3) {
             return def;
         }
         return new Vector(Double.parseDouble(content[0]), Double.parseDouble(content[1]), Double.parseDouble(content[2]));
    }
}
