package com.softech.common.event;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface ICallbackHandler<T> {
    void handle(EventArg<T> arg);
    void error(Exception ex,EventArg<T> arg); 
}
