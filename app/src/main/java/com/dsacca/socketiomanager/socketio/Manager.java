package com.dsacca.socketiomanager.socketio;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Manager {

    private final static String TAG = "Manager";
    private Socket mSocket;
    private String uri;
    private HashMap<String, Emitter.Listener> listeners;

    public Manager(String uri) {
        this.uri = uri;
        listeners = new HashMap<>();
        initSocket();
    }

    private void initSocket() {
        try {
            mSocket = IO.socket(uri);
        } catch (URISyntaxException e) {
            Log.e(TAG, "String could not be parsed as a URI reference.");
        }
    }

    public void setEventListener(String eventName, Emitter.Listener emitterListener) {
        mSocket.on(eventName, emitterListener);
        listeners.put(eventName, emitterListener);
    }

    public void attemptSend(String eventName, Object... args) {
        mSocket.emit(eventName, args);
    }

    public void startConnection() {
        mSocket.connect();
    }

    public void stopConnection() {
        mSocket.disconnect();
        removeListeners();
    }

    private void removeListeners() {
        for (Map.Entry<String, Emitter.Listener> entry : listeners.entrySet()) {
            mSocket.off(entry.getKey(), entry.getValue());
        }
    }
}
