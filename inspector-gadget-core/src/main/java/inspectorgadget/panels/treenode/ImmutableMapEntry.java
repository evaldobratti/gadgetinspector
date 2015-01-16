package inspectorgadget.panels.treenode;

import java.util.Map;

public class ImmutableMapEntry<K, V> implements Map.Entry<K, V> {

    private K key;
    private V value;

    public ImmutableMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return getKey() + " = " + getValue();
    }
}
