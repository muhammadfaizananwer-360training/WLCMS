package com.softech.common.event;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public class EventArg<T> {
    private final String name;
    private final T data;
    private boolean validated;

    public EventArg(T data) {
        this.data = data;
        this.name = null;
    }

    public EventArg(String name, T data) {
        this.name = name;
        this.data = data;
    }
    
    

    public String getName() {
        return name;
    }

    public T getData() {
        return data;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
    
    
}
