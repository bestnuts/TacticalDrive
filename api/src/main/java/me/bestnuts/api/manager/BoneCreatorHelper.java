package me.bestnuts.api.manager;

import java.util.Map;
import java.util.stream.Collectors;

public final class BoneCreatorHelper {

    public static Map<String, BoneCreator> functionMapping(Map<Class<?>, BoneCreator> map) {
        return map.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                entry -> {
                    BoneType annotation = entry.getKey().getAnnotation(BoneType.class);
                    if (annotation == null) {
                        throw new IllegalStateException(entry.getKey().getSimpleName() + " 클래스에 @BoneType 애노테이션이 누락되었습니다.");
                    }
                    return annotation.value();
                },
                Map.Entry::getValue
        ));
    }
}
