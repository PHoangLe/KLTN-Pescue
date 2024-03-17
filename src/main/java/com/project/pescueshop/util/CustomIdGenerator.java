package com.project.pescueshop.util;

import com.project.pescueshop.model.annotation.Name;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public String generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        Class<?> clazz = o.getClass();
        Name annotation = clazz.getAnnotation(Name.class);
        if (annotation == null) {
            throw new IllegalArgumentException("No class annotated with @Name found.");
        }

        String prefix = "";
        prefix = annotation.prefix();

        // Add your custom logic here to generate an ID
        return generateCustomId(prefix);
    }

    private String generateCustomId(String prefix) {
        return prefix + "_" + generateValue() + "_" + getRandomKey();
    }

    private Long generateValue() {
        return System.currentTimeMillis();
    }

    private String getRandomKey() {
        String dictionary = "aAbB9cCdDeE0fFgGhHiI1jJkKlLmM2nNoOpP3qQrRsStT4uU5vV6wWy7zZ8";
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++){
            builder.append(dictionary.charAt(random.nextInt(0, dictionary.length())));
        }

        return builder.toString();
    }
}
