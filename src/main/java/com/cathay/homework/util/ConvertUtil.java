
package com.cathay.homework.util;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

@Slf4j
public class ConvertUtil {

    private ConvertUtil() {}

    public static <T> T convert(Object source, Class<T> clazz) {

        if (source == null) {
            return null;
        }

        T destination = null;

        try {
            destination = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
        } catch (Exception ex) {
            log.error(
                String.format(
                    "ConvertUtil.convert failed. source: [%s], ClassName: [%s]",
                    Objects.toString(source, "Get source failed."),
                    clazz.getName()),
                ex);
        }

        return destination;
    }

    public static <T> T copyNotNullProperties(Object source, T target) {

        if (source == null) {
            return target;
        }

        try {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (Exception ex) {
            log.error(
                String.format(
                    "ConvertUtil.convert failed. source: [%s], destination: [%s]",
                    Objects.toString(source, "Get source failed."),
                    Objects.toString(target, "Get target failed.")),
                ex);
        }

        return target;
    }

    public static <E,T> List<T> convertList(List<E> sourceList, Class<T> clazz) {

        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(source -> convert(source, clazz))
                .collect(Collectors.toList());
    }

    public static String[] getNullPropertyNames(Object source) {

        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);

        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
