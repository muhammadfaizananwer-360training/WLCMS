package com.softech.ls360.lcms.contentbuilder.utils;

import com.softech.common.delegate.Function1;
import com.softech.common.delegate.Function2;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Sheikh Abdul Wahid
 */
public class CollectionHelper {

    public static <In, For, Result> Collection<Result> getFoundItems(Collection<For> findForCol, Collection<In> findInCol, Function2<For, In, Result> calculator) {
        Collection<Result> items = new HashSet<>();
        for (For foor : findForCol) {
            Result foundItem = null;
            for (In in : findInCol) {
                foundItem = calculator.apply(foor, in);
                if (foundItem != null) {
                    break;
                }
            }

            if (foundItem != null) {
                items.add(foundItem);
            }
        }
        return items;
    }

    public static <Col, Result> Collection<Result> filterCollection(Collection<Col> col, Function1<Col, Result> filter) {
        Collection<Result> items = new HashSet<>();
        Result foundItem = null;
        for (Col in : col) {
            foundItem = filter.apply(in);
            if (foundItem != null) {
                items.add(foundItem); 
            }
        }
        return items;
    }

    public static <In, For> Collection<For> getMissingItems(Collection<For> findForCol, Collection<In> findInCol, Function2<For, In, Boolean> camparator) {
        Collection<For> items = new HashSet<>();
        for (For foor : findForCol) {
            boolean found = false;
            for (In in : findInCol) {
                found = camparator.apply(foor, in);
                if (found) {
                    break;
                }
            }

            if (!found) {
                items.add(foor);
            }
        }
        return items;
    }

}
