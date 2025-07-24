package com.example.banto.Utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DTOMapper {
    public static <T, R> List<R> convertList(Stream<T> stream, Function<T, R> mapper) {
        return stream.map(item -> {
                try {
                    return mapper.apply(item);
                } catch (Exception e) {
                    // 로그만 찍고 계속 진행
                    return null;
                }
            })
            .collect(Collectors.toList());
    }
}