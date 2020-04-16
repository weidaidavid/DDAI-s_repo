/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // FIXME: Implement this method and return correct value
        int i = 0;
        DNode curr = _front;
        while (curr != null) {

            curr = curr._next;
            i+=1;

        }


        return i;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        // FIXME: Implement this method and return correct value
        if (i == 0) {
            return _front._val;

        }
        else if (i>0) {
            DNode curr = _front;
            for (int j = 0; j< i;j++){
                curr = curr._next;
            }
            return curr._val;
        }
        else {
            DNode curr = _back;
            for (int j = -1; j> i;j--){
                curr  = curr._prev;
            }
            return curr._val;
        }

    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // FIXME: Implement this method
        if (_front == null){
            _front = new DNode(d);
            _back = _front;

        }
        else {_front._prev = new DNode(d);
            _front._prev._next = _front;
            _front = _front._prev;

        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // FIXME: Implement this method
        if (_back == null){
            _back = new DNode(d);
            _front = _back;
        }
        else    {
            _back._next = new DNode(d);
            _back._next._prev = _back;
            _back = _back._next;
        }

    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        // FIXME: Implement this method
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // FIXME: Implement this method and return correct value
        int front_val = _front._val;
        _front = _front._next;
        return front_val;

    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // FIXME: Implement this method and return correct value
        int back_val = _back._val;
        if (_back._prev != null){
            _back._prev._next = null;
            _back = _back._prev;
        }
        else {
            _front = _back = null;
        }
        return back_val;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // FIXME: Implement this method and return correct value
        int  size = this.size();
        DNode current = _front;
        if (index == 0) {
            return deleteFront();

        }
        else if(index== this.size()-1){
            return deleteBack();

        }
        else {
            for (int i = 0; i < index && current._next!= null; i++){
                current = current._next;

            }
            current._prev._next = current._next;
            current._next._prev = current._prev;
        }
        return current._val;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // FIXME: Implement this method to return correct value
        return null;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
