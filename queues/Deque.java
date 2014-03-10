import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size = 0;

    private class Node {

        private Item item;
        private Node next;
        private Node prev;

        private Node(Item item) {
            this.item = item;
        }

        private Node() {

        }
    }

    public Deque() {
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;

        if (first.next != null) {
            first.next.prev = first;
        }

        if (last == null) {
            last = first;
        }

        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;

        if (last.prev != null) {
            last.prev.next = last;
        }

        if (first == null) {
            first = last;
        }

        size++;
    }

    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Node result = first;

        first = first.next;

        if (first != null) {
            first.prev = null;
        } else {
            last = null;
        }

        size--;
        return result.item;
    }

    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }

        Node result = last;

        last = last.prev;

        if (last != null) {
            last.next = null;
        } else {
            first = null;
        }

        size--;
        return result.item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current;

        private DequeIterator() {
            this.current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        String nullPointerMessage = "Throws null pointer exception when adding null";
        String noSuchElementMessage = "Throws no such element exception "
                + "when retrieving from empty queue";

        Deque<Integer> deque = new Deque<Integer>();
        printQueue(deque);
        try {
            deque.addFirst(null);
        } catch (NullPointerException ex) {
            StdOut.println(nullPointerMessage);
        }

        try {
            StdOut.println("Remove first: " + deque.removeFirst());
        } catch (NoSuchElementException ex) {
            StdOut.println(noSuchElementMessage);
        }

        assertEmpty(deque);
        StdOut.println("Add 1 to queue");
        deque.addFirst(1);
        assertEmpty(deque);
        printQueue(deque);
        StdOut.println("Remove last: " + deque.removeLast());
        assertEmpty(deque);
        printQueue(deque);

        try {
            StdOut.println("Remove first: " + deque.removeFirst());
        } catch (NoSuchElementException ex) {
            StdOut.println(noSuchElementMessage);
        }

        StdOut.println("Add 2 to queue");
        deque.addLast(2);
        assertEmpty(deque);
        printQueue(deque);
        StdOut.println("Remove first: " + deque.removeFirst());
        assertEmpty(deque);
        printQueue(deque);

        try {
            StdOut.println("Remove last: " + deque.removeLast());
        } catch (NoSuchElementException ex) {
            StdOut.println(noSuchElementMessage);
        }

        deque.addFirst(1);
        printQueue(deque);
        deque.addFirst(2);
        printQueue(deque);
        deque.addFirst(3);
        printQueue(deque);
        deque.addLast(4);
        printQueue(deque);
        deque.addLast(5);
        printQueue(deque);
        deque.addLast(6);
        printQueue(deque);

        StdOut.println("Remove first: " + deque.removeFirst());
        printQueue(deque);

        StdOut.println("Remove first: " + deque.removeFirst());
        printQueue(deque);

        StdOut.println("Remove first: " + deque.removeFirst());
        printQueue(deque);

        StdOut.println("Remove first: " + deque.removeFirst());
        printQueue(deque);

        StdOut.println("Remove last: " + deque.removeLast());
        printQueue(deque);

        StdOut.println("Remove last: " + deque.removeLast());
        printQueue(deque);

        assertEmpty(deque);
    }

    private static void assertEmpty(Deque<Integer> deque) {
        String message = "";
        if (deque.isEmpty()) {
            message = "Queue is empty";
        } else {
            message = "Queue not empty";
        }
        StdOut.println(message);
    }

    private static void printQueue(Deque<Integer> deque) {
        StdOut.print("Queue: ");
        for (int element : deque) {
            StdOut.print(element + " ");
        }
        StdOut.println();
    }
}