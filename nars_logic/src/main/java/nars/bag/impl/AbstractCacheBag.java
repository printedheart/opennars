package nars.bag.impl;

import nars.budget.Itemized;

import java.util.function.Consumer;

/**
 * Created by me on 9/1/15.
 */
abstract public class AbstractCacheBag<K, V extends Itemized<K>> implements CacheBag<K,V> {


    private Consumer<V> onRemoval;

    public void setOnRemoval(Consumer<V> onRemoval) {
        this.onRemoval = onRemoval;
    }

    public Consumer<V> getOnRemoval() {
        return onRemoval;
    }


}
