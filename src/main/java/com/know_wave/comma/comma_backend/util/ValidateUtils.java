package com.know_wave.comma.comma_backend.util;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

import static com.know_wave.comma.comma_backend.util.ExceptionMessageSource.NOT_FOUND_VALUE;

public class ValidateUtils {

    public static void throwIfEmpty(Collection<?> collection) {
        Assert.notEmpty(collection, NOT_FOUND_VALUE);
    }

    public static void throwIfOptionalEmpty(Optional<?> Optional) {
        Assert.isTrue(Optional.isPresent(), NOT_FOUND_VALUE);
    }
}
