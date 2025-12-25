/**
 * A simple counter class.
 */
public class Counter {

    private int value;

    /**
     * Increase the counter by a given number.
     *
     * @param number the number to add
     */
    public void increase(int number) {
        this.value += number;
    }

    /**
     * Decrease the counter by a given number.
     *
     * @param number the number to subtract
     */
    public void decrease(int number) {
        this.value -= number;
    }

    /**
     * Get the current count.
     *
     * @return the current value
     */
    public int getValue() {
        return this.value;
    }
}
