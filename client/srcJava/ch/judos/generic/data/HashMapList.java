package ch.judos.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * implements HashMap [K,ArrayList[V]]
 *
 * @param <K> key
 * @param <V> value
 */
public class HashMapList<K, V> {

	protected HashMap<K, ArrayList<V>> map;
	private int size;

	public HashMapList() {
		this.map = new HashMap<>();
		this.size = 0;
	}

	public void put(K key, V value) {
		ArrayList<V> list = this.map.get(key);
		if (list == null) {
			list = new ArrayList<>();
			this.map.put(key, list);
		}
		list.add(value);
		this.size++;
	}

	public ArrayList<V> getList(K key) {
		return this.map.get(key);
	}

	public void removeKey(K key) {
		ArrayList<V> list = this.map.remove(key);
		if (list != null) {
			this.size -= list.size();
		}
	}

	public void removeValueForKey(K key, V value) {
		ArrayList<V> list = this.map.get(key);
		if (list.remove(value)) {
			this.size--;
		}
	}

	/**
	 * Warning: Slow O(n)
	 */
	public void removeValue(V value) {
		for (ArrayList<V> key : this.map.values()) {
			if (key.remove(value)) {
				this.size--;
				return;
			}
		}
	}

	public boolean containsKey(K key) {
		return this.map.containsKey(key);
	}

	public Set<K> keys() {
		return this.map.keySet();
	}

	public void clear() {
		this.map.clear();
		this.size = 0;
	}

	public int sizeKeys() {
		return this.map.size();
	}

	/**
	 * @return recalculates the size of all values
	 */
	public int sizeAllValues() {
		return this.map.values().stream().map(list -> list.size()).reduce(Integer::sum).get();
	}

	public int getSize() {
		return this.size;
	}
}
