package root.http;

import root.Main;
import root.async.Future;
import root.async.FutureImpl;
import root.os.ResponseHolder;

import java.util.HashMap;
import java.util.Map;

public class WSClient {

    public WSClient() {
        new Thread(this::epoll).start();
    }

    private Map<Integer, FutureImpl<Response>> callbacks = new HashMap<>();

    public Future<Response> sendRequest(Request request) {
        Integer id = Main.OS.sendRequest(request);
        FutureImpl<Response> result = new FutureImpl<>();
        callbacks.put(id, result);
        return result;
    }

    private void epoll() {
        try {
            while(true) {
                ResponseHolder holder = Main.OS.epoll();
                FutureImpl<Response> callback = callbacks.get(holder.getId());
                if (callback != null) {
                    callbacks.remove(holder.getId());
                    callback.complete(holder.getResponse());
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

