package root.async;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FutureImpl<T> implements Future<T> {

    private static List<Function<Integer, Boolean>> mapList = new ArrayList<>();
    public static Thread MapThread = new Thread(FutureImpl::checkMap);

    private boolean _isComplete = false;
    private T _value = null;

    @Override
    public boolean isComplete() {
        return _isComplete;
    }

    @Override
    public T get() {
        if (_isComplete) {
            return _value;
        } else {
            throw new IllegalStateException("You can't get value till future is complete");
        }
    }

    public void complete(T value) {
        _value = value;
        _isComplete = true;
    }

    @Override
    public <R> Future<R> map(Function<? super T, ? extends R> f) {
        FutureImpl<R> result = new FutureImpl<>();

        synchronized (MapThread) {
            mapList.add((ignore) -> {
                if (this.isComplete()) {
                    result.complete(f.apply(this.get()));
                }
                return result.isComplete();
            });
        }

        return result;
    }

    private static void checkMap() {
        while (true) {
            List<Function<Integer, Boolean>> toDelete = new ArrayList<>();
            synchronized (MapThread) {
                for (Function<Integer, Boolean> mapFunction : mapList) {
                    if (mapFunction.apply(10)) {
                        toDelete.add(mapFunction);
                    }
                }

                mapList.removeAll(toDelete);
            }
        }
    }
}
