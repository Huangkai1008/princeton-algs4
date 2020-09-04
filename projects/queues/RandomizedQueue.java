package queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author huangkai
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    /**
     * The starting capacity of the queue should be 10.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * The resize factor of the queue.
     */
    private static final int RESIZE_FACTOR = 2;

    /**
     * The shrink factor of the queue.
     */
    private static final int SHRINK_FACTOR = 4;

    /**
     * The capacity of queue.
     */
    private int capacity;

    /**
     * The size of queue.
     */
    private int size;

    /**
     * The array to carry items.
     */
    private Object[] items;

    /**
     * Constructor of randomized queue.
     */
    public RandomizedQueue() {
        items = new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    /**
     * Add the item to the rear of the queue.
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null.");
        }

        if (isFull()) {
            resize(capacity * RESIZE_FACTOR);
        }

        items[size] = item;
        size++;
    }

    /**
     * Remove and return a random item.
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty");
        }

        int index = StdRandom.uniformInt(0, size);
        Item item = (Item) items[index];
        items[index] = items[size - 1];
        items[size - 1] = null;
        size--;

        if (size == capacity / SHRINK_FACTOR && capacity / RESIZE_FACTOR >= DEFAULT_CAPACITY) {
            resize(capacity / RESIZE_FACTOR);
        }
        return item;
    }

    /**
     * @return a random item without removing it.
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty");
        }

        int index = StdRandom.uniformInt(0, size);
        return (Item) items[index];
    }

    /**
     * @return whether the queue is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of items on the randomized queue.
     */
    public int size() {
        return size;
    }

    /**
     * @return whether the queue is full or not.
     */
    private boolean isFull() {
        return size == capacity;
    }

    /**
     * Resize the queue.
     *
     * @param capacity of new queue.
     */
    private void resize(int capacity) {
        Object[] newItems = new Object[capacity];
        System.arraycopy(items, 0, newItems, 0, size);
        this.capacity = capacity;
        this.items = newItems;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Object[] copyItems;
        private int remain;

        public RandomizedQueueIterator() {
            copyItems = new Object[size];
            System.arraycopy(items, 0, copyItems, 0, size);
            StdRandom.shuffle(copyItems);
            remain = size;
        }

        @Override
        public Item next() {
            if (remain == 0) {
                throw new NoSuchElementException();
            }
            return (Item) copyItems[--remain];
        }

        @Override
        public boolean hasNext() {
            return remain != 0;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The randomized queue implementation doesn't support remove operation.");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        System.out.println(randomizedQueue.size());
        System.out.println(randomizedQueue.isEmpty());
        for (int i = 1; i < 11; i++) {
            randomizedQueue.enqueue(i);
        }

        for (int i = 0; i < 5; i++) {
            System.out.print(randomizedQueue.sample());
            System.out.print("\t");
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
            System.out.print(randomizedQueue.dequeue());
            System.out.print("\t");
        }
        System.out.println();

        System.out.println(randomizedQueue.size());
        System.out.println(randomizedQueue.isEmpty());

        for (Integer item : randomizedQueue) {
            System.out.print(item);
            System.out.print("\t");
        }
    }
}
