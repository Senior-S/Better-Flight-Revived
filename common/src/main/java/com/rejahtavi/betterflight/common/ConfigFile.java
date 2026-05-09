package com.rejahtavi.betterflight.common;

import dev.architectury.platform.Platform;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class ConfigFile {
    private final Path path;
    private final Properties values = new Properties();

    public ConfigFile(String fileName) {
        this.path = Platform.getConfigFolder().resolve(fileName);
    }

    public void load() {
        if (!Files.exists(path)) {
            return;
        }
        try (Reader reader = Files.newBufferedReader(path)) {
            values.load(reader);
        } catch (IOException ignored) {
        }
    }

    public int readInt(String key, int fallback, int min, int max) {
        int value = fallback;
        String raw = values.getProperty(key);
        if (raw != null) {
            try {
                value = Integer.parseInt(raw.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        value = Math.clamp(value, min, max);
        values.setProperty(key, Integer.toString(value));
        return value;
    }

    public double readDouble(String key, double fallback, double min, double max) {
        double value = fallback;
        String raw = values.getProperty(key);
        if (raw != null) {
            try {
                value = Double.parseDouble(raw.trim());
            } catch (NumberFormatException ignored) {
            }
        }
        value = Math.clamp(value, min, max);
        values.setProperty(key, Double.toString(value));
        return value;
    }

    public boolean readBoolean(String key, boolean fallback) {
        String raw = values.getProperty(key);
        boolean value = raw == null ? fallback : Boolean.parseBoolean(raw.trim());
        values.setProperty(key, Boolean.toString(value));
        return value;
    }

    public List<String> readList(String key, List<String> fallback) {
        String raw = values.getProperty(key);
        List<String> value = raw == null || raw.isBlank()
                ? fallback
                : Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(entry -> !entry.isEmpty())
                .toList();
        values.setProperty(key, String.join(",", value));
        return value;
    }

    public <T extends Enum<T>> T readEnum(String key, T fallback, Class<T> type) {
        T value = fallback;
        String raw = values.getProperty(key);
        if (raw != null) {
            try {
                value = Enum.valueOf(type, raw.trim());
            } catch (IllegalArgumentException ignored) {
            }
        }
        values.setProperty(key, value.name());
        return value;
    }

    public void put(String key, Object value) {
        values.setProperty(key, String.valueOf(value));
    }

    public void save() {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                values.store(writer, null);
            }
        } catch (IOException ignored) {
        }
    }
}
