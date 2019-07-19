package com.git.hui.fix.core.parser;


/**
 * Created by @author yihui in 14:40 19/7/19.
 */
public abstract class AbstractArgParser implements IArgParser {
    protected static final String NULL_TAG = "null";

    @Override
    public int compareTo(IArgParser o) {
        if (o == null) {
            return -1;
        }

        if (order() == o.order()) {
            return 0;
        }

        return order() > o.order() ? 1 : -1;
    }
}
