package fr.swansky.simplybot.core;

public interface TokenProvider<T> {

    T getToken();

    static <T> TokenProvider<T> directProvider(T value) {
        return () -> value;
    }
}
