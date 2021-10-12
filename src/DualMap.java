import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class DualMap<Key, Value> implements Map<Key, Value> {
    private final Map<Key, Value> main = new HashMap<>();
    private final Map<Value, Key> revers = new HashMap<>();

    @Override
    public int size() {
        return main.size();
    }

    @Override
    public boolean isEmpty() {
        return main.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return main.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return revers.containsKey(o);
    }

    @Override
    public Value get(Object o) {
        return main.get(o);
    }

    @Override
    public Value put(Key o, Value o2) {
        Value v = main.put(o, o2);
        if (v != null) {
            revers.remove(v);
        }
        Key k = revers.put(o2, o);
        if(k != null){
            main.remove(k);
        }
        return v;
    }

    @Override
    public Value remove(Object o) {
        Value r = main.remove(o);
        revers.remove(r);
        return r;
    }

    @Override
    public void putAll(Map<? extends Key, ? extends Value> map) {
        for(Map.Entry<? extends Key, ? extends Value> entry: map.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        main.clear();
        revers.clear();
    }

    @Override
    public Set keySet() {
        return main.keySet();
    }

    @Override
    public Set values() {
        return revers.keySet();
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return main.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o.getClass().equals(DualMap.class)){
            DualMap other = (DualMap)o;
            if(other.main.equals(main) && other.revers.equals(revers)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (main.hashCode() + revers.hashCode()) / 2;
    }

    @Override
    public void forEach(BiConsumer action) {
        main.forEach(action);
        revers.clear();
        for(Map.Entry<Key, Value> entry: main.entrySet()){
            revers.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean b = main.remove(key, value);
        revers.remove(value, key);
        return b;
    }

    @Override
    public boolean replace(Key key, Value oldValue, Value newValue) {
        boolean b = main.replace(key, oldValue, newValue);
        if(b) {
            revers.remove(oldValue);
            revers.put(newValue, key);
        }
        return b;
    }

    @Override
    public Value replace(Key key, Value value) {
        Value b = main.replace(key, value);
        if(b != null) {
            revers.remove(b);
            revers.put(value, key);
        }
        return b;
    }

    public Key getKey(Value value){
        return revers.get(value);
    }

    public Entry<Key, Value> randomEntry(){
        int peak = (int)(Math.random() * size());
        for(Map.Entry<Key, Value> entry: entrySet()){
            if(peak-- == 0){
                return entry;
            }
        }
        return entrySet().iterator().next();
    }
}
