package root.async;

import java.util.function.Function;

public interface Future<T> {
    boolean isComplete();
    T get();
    <R> Future<R> map(Function<? super T, ? extends R> var1);
}
