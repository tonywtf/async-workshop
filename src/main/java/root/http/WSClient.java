package root.http;

import root.Main;
import root.os.ResponseHolder;

import java.util.HashMap;
import java.util.Map;

public class WSClient {

    public WSClient() {
        new Thread(this::epoll).start();
    }

    private Map<Integer, WSCallback> callbacks = new HashMap<>();

    public void sendRequest(Request request, WSCallback callback) {
        Integer id = Main.OS.sendRequest(request);
        callbacks.put(id, callback);
    }

    private void epoll() {
        try {
            while(true) {
                ResponseHolder holder = Main.OS.epoll();
                WSCallback callback = callbacks.get(holder.getId());
                if (callback != null) {
                    callbacks.remove(holder.getId());
                    callback.apply(holder.getResponse());
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

