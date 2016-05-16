package com.softech.common.delegate;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface Function2WithException<Arg1,Arg2,To,Ex extends Throwable> {
    To apply(Arg1 arg1,Arg2 arg2) throws Ex;
}
