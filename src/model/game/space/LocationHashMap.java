package model.game.space;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.function.Function;

import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointI;

public class LocationHashMap<E extends Locatable> {
	private HashMapList<Integer, E> hashMap;

	public LocationHashMap() {
		this.hashMap = new HashMapList<Integer, E>();
	}

	public void put(E entity) {
		PointI position = entity.getLocation();
		Integer hashValue = GridHashing.hashPointIToMapIndex(position);
		this.hashMap.put(hashValue, entity);
	}

	/**
	 * @param position
	 * @param range
	 * @param consumer
	 *            if the consumer returns true the no more elements will be
	 *            fetched
	 */
	public void forEachInRange(PointI position, int range, Function<E, Boolean> consumer) {
		PointI min = position.subtract(new PointI(range, range));
		PointI max = position.add(new PointI(range, range));
		forEachInRect(min, max, consumer);
	}

	public void forEachInRect(PointI min, PointI max, Function<E, Boolean> consumer) {
		PointI minGridIndex = GridHashing.hashPointToGridIndex(min);
		PointI maxGridIndex = GridHashing.hashPointToGridIndex(max);
		for (int x = minGridIndex.x; x <= maxGridIndex.x; x++) {
			for (int y = minGridIndex.y; y <= maxGridIndex.y; y++) {
				int index = GridHashing.hashGridPointIntoMapIndex(new PointI(x, y));
				ArrayList<E> list = this.hashMap.getList(index);
				if (list != null) {
					for (E element : list) {
						if (consumer.apply(element))
							return;
					}
				}
			}
		}
	}

	public ArrayList<E> forAllInRange(PointI position, int range) {
		ArrayList<E> result = new ArrayList<E>();
		forEachInRange(position, range, element -> !result.add(element));
		return result;
	}

	public void clear() {
		this.hashMap.clear();
	}

	public ArrayList<E> forAllInGrid(int x, int y) {
		int index = GridHashing.hashGridPointIntoMapIndex(new PointI(x, y));
		ArrayList<E> list = this.hashMap.getList(index);
		if (list != null) {
			return list;
		}
		return new ArrayList<>();
	}

	public void forEachInRect(Rectangle rectangle, Function<E, Boolean> consumer) {
		PointI min = new PointI(rectangle.getLocation());
		PointI max = min.add(new PointI(rectangle.width, rectangle.height));
		forEachInRect(min, max, consumer);
	}
}
