package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author huangkai
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * Doubly linked node.
     */
    private static class Node<Item> {
        /**
         * The item of node.
         */
        private final Item item;
        /**
         * The prev node and next node.
         */
        private Node<Item> prev, next;

        /**
         * The constructor of `Node`.
         *
         * @param item of the node.
         */
        Node(Item item) {
            this.item = item;
        }
    }

    /**
     * The head and tail of the deque.
     */
    private Node<Item> head, tail;

    /**
     * The size of the deque.
     */
    private int size;

    /**
     * Constructor of deque.
     */
    public Deque() {
        head = tail = null;
        size = 0;
    }

    /**
     * Add the item to the front.
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null.");
        }

        Node<Item> node = new Node<>(item);
        if (isEmpty()) {
            head = tail = node;
        } else {
            head.prev = node;
            node.next = head;
            head = node;
        }
        size++;
    }

    /**
     * Add the item to the back
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can't be null.");
        }

        Node<Item> node = new Node<>(item);
        if (isEmpty()) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    /**
     * remove and return the item from the front
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty.");
        }

        Item item = head.item;
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return item;
    }

    /**
     * remove and return the item from the back
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty.");
        }

        Item item = tail.item;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return item;
    }

    /**
     * @return whether the deque is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return the number of items on the deque.
     */
    public int size() {
        return size;
    }


    /**
     * @return an iterator over items in order from front to back.
     */
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<Item> cur = head;
        while (cur != null) {
            sb.append(cur.item);
            sb.append("\t");
            cur = cur.next;
        }
        return sb.toString();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> cur = head;

        @Override
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = cur.item;
            cur = cur.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The deque implementation doesn't support remove operation.");
        }
    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        System.out.println(deque.isEmpty());
        for (int i = 1; i < 6; i++) {
            deque.addFirst(i);
        }

        for (int i = 6; i < 11; i++) {
            deque.addLast(i);
        }

        System.out.println(deque);

        System.out.println(deque.isEmpty());
        System.out.println(deque.size);

        for (Integer item: deque) {
            System.out.println(item);
        }

        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
        System.out.println(deque.removeFirst());
        System.out.println(deque.removeLast());
    }
}
