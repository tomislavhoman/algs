import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tomodoma on 2/15/14.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int first = 0;
    private int last = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    // is the queue empty?
    public boolean isEmpty() {
        return first == last;
    }

    // return the number of items on the queue
    public int size() {
        return last - first;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        //Resize if necessary
        if (last - first >= items.length) {
            resize(items.length * 2);
        }
        items[last % items.length] = item;
        last++;
    }

    // delete and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int randomPosition = StdRandom.uniform(first, last);
        swap(first % items.length, randomPosition % items.length);

        Item item = items[first % items.length];
        items[first % items.length] = null; //Prevent loitering
        first++;

        //resize if necessary
        if (last - first < items.length / 4) {
            resize(items.length / 2);
        }

        return item;
    }

    // return (but do not delete) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int randomPosition = StdRandom.uniform(first, last);
        return items[randomPosition % items.length];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resize(int newSize) {
        Item[] tempArray = (Item[]) new Object[newSize];
        int i = 0;
        for (int j = first; j < last; i++, j++) {
            tempArray[i] = items[j % items.length];
        }
        items = tempArray;
        first = 0;
        last = i;
    }

    private void swap(int p, int q) {
        Item tmp = items[p];
        items[p] = items[q];
        items[q] = tmp;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] itemsCopy;
        private int index = 0;

        private RandomizedQueueIterator() {
            itemsCopy = (Item[]) new Object[last - first];
            for (int i = 0, j = first; j < last; i++, j++) {
                itemsCopy[i] = items[j % items.length];
            }
            StdRandom.shuffle(itemsCopy);
        }

        @Override
        public boolean hasNext() {
            return itemsCopy != null && index < itemsCopy.length;
        }

        @Override
        public Item next() {
            if (index >= itemsCopy.length) {
                throw new NoSuchElementException();
            }

            return itemsCopy[index++];
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

        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        printQueue(queue);
        try {
            queue.enqueue(null);
        } catch (NullPointerException ex) {
            StdOut.println(nullPointerMessage);
        }

        try {
            StdOut.println("Dequeue: " + queue.dequeue());
        } catch (NoSuchElementException ex) {
            StdOut.println(noSuchElementMessage);
        }

        assertEmpty(queue);
        StdOut.println("Add 1 to queue");
        queue.enqueue(1);
        assertEmpty(queue);
        printQueue(queue);
        StdOut.println("Dequeue: " + queue.dequeue());
        assertEmpty(queue);
        printQueue(queue);

        try {
            StdOut.println("Dequeue: " + queue.dequeue());
        } catch (NoSuchElementException ex) {
            StdOut.println(noSuchElementMessage);
        }

        queue.enqueue(1);
        printQueue(queue);
        queue.enqueue(2);
        printQueue(queue);
        queue.enqueue(3);
        printQueue(queue);
        queue.enqueue(4);
        printQueue(queue);
        queue.enqueue(5);
        printQueue(queue);
        queue.enqueue(6);
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        StdOut.println("Dequeue: " + queue.dequeue());
        printQueue(queue);

        assertEmpty(queue);
    }

    private static void assertEmpty(RandomizedQueue<Integer> queue) {
        String message = "";
        if (queue.isEmpty()) {
            message = "Queue is empty";
        } else {
            message = "Queue not empty";
        }
        StdOut.println(message);
    }

    private static void printQueue(RandomizedQueue<Integer> quque) {
        StdOut.print("Queue: ");
        for (int element : quque) {
            StdOut.print(element + " ");
        }
        StdOut.println();
    }
}
