package ch.judos.snakes.client.old.model.game.space;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import ch.judos.generic.data.HashMapList;
import ch.judos.generic.data.geometry.PointI;

public class LocationHashMap<E extends Locatable> {
	private HashMapList<PointI, E> hashMap;
	private int gridSize;

	public LocationHashMap(int gridSize) {
		this.gridSize = gridSize;
		this.hashMap = new HashMapList<PointI, E>();
	}

	public void put(E entity) {
		PointI position = entity.getLocation();
		PointI gridPoint = hashPointToGridIndex(position);
		this.hashMap.put(gridPoint, entity);
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
		int rangeSq = range * range;
		forEachInRect(min, max, element -> {
			if (element.getLocation().distanceSq(position) <= rangeSq)
				return consumer.apply(element);
			return false;
		});
	}

	public Stream<E> streamForRect(PointI min, PointI max) {
		PointI minGridIndex = hashPointToGridIndex(min);
		PointI maxGridIndex = hashPointToGridIndex(max);
		int width = maxGridIndex.x - minGridIndex.x + 1;
		int height = maxGridIndex.y - minGridIndex.y + 1;
		return StreamSupport.stream(new RectangleSpliterator(minGridIndex, width, height),
			true);
	}

	public void forEachInRect(PointI min, PointI max, Function<E, Boolean> consumer) {
		PointI minGridIndex = hashPointToGridIndex(min);
		PointI maxGridIndex = hashPointToGridIndex(max);
		for (int x = minGridIndex.x; x <= maxGridIndex.x; x++) {
			for (int y = minGridIndex.y; y <= maxGridIndex.y; y++) {
				PointI gridIndex = new PointI(x, y);
				ArrayList<E> list = this.hashMap.getList(gridIndex);
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
		ArrayList<E> list = this.hashMap.getList(new PointI(x, y));
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

	public void removeAll(List<E> entries) {
		for (E entry : entries) {
			PointI position = entry.getLocation();
			PointI gridIndex = hashPointToGridIndex(position);
			this.hashMap.removeValueForKey(gridIndex, entry);
		}
	}

	public int getSize() {
		return this.hashMap.getSize();
	}

	public PointI hashPointToGridIndex(PointI point) {
		return new PointI(Math.floorDiv(point.x, this.gridSize), Math.floorDiv(point.y,
			this.gridSize));
	}

	private class RectangleSpliterator implements Spliterator<E> {
		private int width;
		private int height;
		private PointI minGridIndex;
		private int maxIndex;
		private int currentIndex;

		public RectangleSpliterator(PointI minGridIndex, int width, int height) {
			this.minGridIndex = minGridIndex;
			this.width = width;
			this.height = height;
			this.currentIndex = 0;
			this.maxIndex = width * height - 1;
		}

		@Override
		public boolean tryAdvance(Consumer<? super E> action) {
			int ax = this.currentIndex % this.width;
			int ay = this.currentIndex / this.width;
			PointI gridIndex = this.minGridIndex.add(new PointI(ax, ay));
			hashMap.getList(gridIndex).forEach(action);
			this.currentIndex++;
			return this.currentIndex <= this.maxIndex;
		}

		@Override
		public Spliterator<E> trySplit() {
			if (this.currentIndex == this.maxIndex)
				return null;
			int middleUp = (this.maxIndex - this.currentIndex + 1) / 2 + this.currentIndex;
			RectangleSpliterator prefixIterator = new RectangleSpliterator(this.minGridIndex,
				this.width, this.height);
			prefixIterator.maxIndex = middleUp - 1;
			prefixIterator.currentIndex = this.currentIndex;
			this.currentIndex = middleUp;
			return prefixIterator;
		}

		@Override
		public long estimateSize() {
			return this.maxIndex - this.currentIndex + 1;
		}

		@Override
		public int characteristics() {
			return CONCURRENT | DISTINCT | NONNULL | ORDERED;
		}

	}
}
