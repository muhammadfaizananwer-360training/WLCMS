package com.softech.common.delegate;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public interface Function2<Arg1,Arg2,To> {
    To apply(Arg1 arg1,Arg2 arg2);
}
