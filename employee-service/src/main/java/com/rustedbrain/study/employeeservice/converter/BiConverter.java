package com.rustedbrain.study.employeeservice.converter;

import org.springframework.core.convert.converter.Converter;

public interface BiConverter<S, T> extends Converter<S, T> {

    S convertFrom(T department);
}
