package com.softech.common.delegate;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface Function1WithException<Arg,To,Ex extends Throwable> {
    To apply(Arg arg) throws Ex;
}
