package iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Dynamic array implementation of the ArrayList
 */
public class ArrayList<E> implements List<E>, Cloneable{

    public static final int CAPACITY = 10;
    private E[] data; //generic array that we will use for storage
    private int size;

    //default constructor
    public ArrayList()
    {
        this(CAPACITY);
    }

    //constructor with parameters
    public ArrayList(int capacity)
    {
        //we first create a new array of objects with the specified capacity
        //then we cast it to the type being stored in our FixedArrayList
        data = (E[]) new Object[capacity];
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public E get(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size-1);
        return data[i];
    }

    @Override
    //why do we return the old element? No idea. Just to remain consistent with Java's interface.
    public E set(int i, E e)
    {
        checkIndex(i, size-1);
        E temp = data[i];
        data[i] = e;
        return temp;
    }

    @Override
    public void add(E e) throws IndexOutOfBoundsException
    {
        //instead of throwing exception, we resize the array
        //doubling previous size
        if(size==data.length)
            resize(data.length * 2);
        data[size] = e;
        size++;
    }


    @Override
    public void add(int i, E e) throws IndexOutOfBoundsException {
        checkIndex(i, size);

        //instead of throwing exception, we resize the array now
        if(size==data.length)
            resize(data.length * 2);
        for(int k=size-1; k >= i; k--) //manual array copying for demonstration
            data[k+1] = data[k];
        data[i] = e;
        size++;
    }

    @Override
    public E remove(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size-1);
        E temp = data[i];

        //shift all elements after current index one to the left
        for(int k=i; k < size-1; k++)
            data[k] = data[k+1];
        data[size-1] = null; //delete what used to be last element and decrement size
        size--;
        return temp;
    }

    /**
     * Checks whether the index is valid
     * @param index to be checked
     * @param n top valid index
     */
    private static void checkIndex(int index, int n)
    {
        if(index<0 || index > n)
            throw new IndexOutOfBoundsException("Illegal index " + index);
    }

    private void resize(int capacity) throws IndexOutOfBoundsException
    {
        E[] temp = (E[]) new Object[capacity];
        for(int k=0; k<size; k++)
            temp[k] = data[k];
        data = temp; //start using new array
        //old array will be picked up by garbage collection
    }

    /**
     * Creates a shallow copying of the ArrayList instance
     */
    @Override
    public ArrayList<E> clone()
    {
        try {
            ArrayList<E> other = (ArrayList) super.clone();
            other.data =  data.clone(); //all we have to do
            return other;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ArrayIterator implements Iterator<E>
    {
        private int index = 0; //index of the NEXT element to remove (NOT current element)
        private boolean removable = false;

        @Override
        public boolean hasNext() {
            return index < size; //"size" is equivalent to "ArrayList.this.size"
        }

        @Override
        public E next() throws NoSuchElementException {
            if(index==size) throw new NoSuchElementException("No next element");
            removable = true;
            return data[index++];
        }

        @Override
        public void remove() throws IllegalStateException {
            if(!removable) throw new IllegalStateException("Nothing to remove");
            ArrayList.this.remove(index-1);
            index--;
            removable = false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator();
    }
}
