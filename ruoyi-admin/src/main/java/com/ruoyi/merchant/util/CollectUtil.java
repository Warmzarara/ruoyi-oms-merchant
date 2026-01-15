package com.ruoyi.merchant.util;


import cn.hutool.core.collection.CollStreamUtil;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectUtil extends CollStreamUtil {

    public static <E, T> Set<T> toSet(Collection<E> collection, Function<E, T> function) {
        if (collection == null || collection.isEmpty()) {
            return new HashSet<>();
        }
        return collection.stream().filter(Objects::nonNull).map(function).collect(Collectors.toSet());
    }

    public static <E, T> List<T> toList(Collection<E> collection, Function<E, T> function) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static <E, T> List<T> toDistinctList(Collection<E> collection, Function<E, T> function) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }
        return collection.stream().map(function).filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    public static <E> List<E> filterList(Collection<E> collection, Predicate<E> p) {
        return filterList(collection, p, Function.identity());
    }

    public static <E, D> List<D> filterList(Collection<E> collection, Predicate<E> p, Function<E, D> map) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }
        return collection.stream().filter(p).map(map).collect(Collectors.toList());
    }

    public static <K, E> Map<K, E> toMap(Collection<E> editList, Function<E, K> getId) {
        return toMap(editList, getId, Function.identity());
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> editList, Function<E, K> getId, Function<E, V> getValue) {
        if (editList == null || editList.isEmpty()) {
            return new HashMap<>();
        }
        return editList.stream().collect(Collectors.toMap(getId, getValue, (k, v) -> v));
    }

    public static <K, E> Map<K, List<E>> group(List<E> coll, Function<E, K> fun) {
        if (coll == null || coll.isEmpty()) {
            return new HashMap<>();
        }
        return coll.stream().collect(Collectors.groupingBy(fun));
    }

    public static <E, K, G> Map<K, List<G>> mapGroup(List<E> coll, Function<E, G> f, Function<G, K> fun) {
        if (coll == null || coll.isEmpty()) {
            return new HashMap<>();
        }
        return coll.stream().map(f).collect(Collectors.groupingBy(fun));
    }

    public static <E, T> List<T> filterRepeat(List<E> columnList, Function<E, T> function) {
        ArrayList<T> res = new ArrayList<>();
        if (columnList == null || columnList.isEmpty()) {
            return res;
        }
        HashSet<T> objects = new HashSet<>();
        columnList.forEach(c -> {
            T apply = function.apply(c);
            if (!objects.add(apply)) {
                res.add(apply);
            }
        });
        return res;
    }
}
