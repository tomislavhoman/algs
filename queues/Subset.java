/**
 * Created by tomodoma on 2/15/14.
 */
public class Subset {

    public static void main(String[] args) {

        if (args.length < 1) {
            throw new IllegalArgumentException();
        }

        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        String[] items = StdIn.readAllStrings();
        for (String item : items) {
            queue.enqueue(item);
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(queue.dequeue());
        }
    }
}
