package com.softech.common.event;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface ICallbackHandlerWithException<T,Ex extends Throwable> {
    void handle(EventArg<T> arg) throws Ex;
    void error(Exception ex,EventArg<T> arg) throws Ex; 
}
