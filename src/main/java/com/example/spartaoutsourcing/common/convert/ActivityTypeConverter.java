package com.example.spartaoutsourcing.common.convert;

import com.example.spartaoutsourcing.domain.activity.enums.ActivityType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class ActivityTypeConverter implements Converter<String, ActivityType> {

    @Override
    public ActivityType convert(String source) {
        return ActivityType.valueOf(source.toUpperCase());
    }
}
