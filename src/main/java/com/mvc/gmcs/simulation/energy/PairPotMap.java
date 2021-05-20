package com.mvc.gmcs.simulation.energy;

import java.util.HashMap;

public class PairPotMap<K,V> extends HashMap<K,V> {
    @Override
    public V get(Object key) {
        if (super.get(key)==null && key instanceof String) {
            String yek = new StringBuilder((String) key).reverse().toString();
            return super.get(yek);
        }
        return super.get(key);
    }
}
