import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MyList <E>{
    private static final int DEFAULT_CAPACITY = 10;

    private static final Object[] EMPTY_ELEMENTDATA = {};

    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private Object[] elementData;

    private int size;

    public MyList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;//The size of DEFAULTCAPACITY_EMPTY_ELEMENTDATA is also 0, and the default array size initialization operation will be performed when add is called for the first time
    }

    public MyList(int initialCapacity) {
        if (initialCapacity> 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else  {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    public MyList(Collection<? extends E> c) {
        elementData = c.toArray();//Convert Collection to array elements and store them in the array
        if ((size = elementData.length) != 0) {//If the array length is not 0, expand the array size to the size of the array object converted by collection
            // c.toArray might (incorrectly) not return Object[] (see 6260652)
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else  {//array length is 0, then initialize an empty shared array instance
            // replace with empty array.
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {//If this is the first time add
            return Math.max(DEFAULT_CAPACITY, minCapacity);//Return the largest number in the default capacity size and the minimum capacity size passed
        }
        return minCapacity;//return the minimum capacity value passed
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity <0) // If minCapacity is less than 0, throw an OutOfMemoryError exception
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;//If the current minimum capacity is greater than (Integer.MAX_VALUE-8), then expand to Integer.MAX_VALUE, otherwise expand to (Integer.MAX_VALUE-8)
    }

    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;//Current array capacity
        int newCapacity = oldCapacity + (oldCapacity >> 1);//new capacity = current array capacity size * 1.5
        if (newCapacity-minCapacity <0)//The new capacity is less than the minimum capacity, set the new capacity as the minimum capacity
            newCapacity = minCapacity;
        if (newCapacity-MAX_ARRAY_SIZE> 0)//When the new capacity size exceeds the maximum array size, set the new capacity to Integer.MAX_VALUE
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);//Expand elementData to the size of newCapacity
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public int indexOf(Object o) {
        if (o == null) {//Object o is null, then return the index value of the first null object in the array
            for (int i = 0; i <size; i++)//forward traversal
                if (elementData[i] == null)
                    return i;
        } else  {//Object o is not null, then according to the equals() comparison result, return the index value of the first occurrence in the array
            for (int i = 0; i <size; i++)//forward traversal
                if (o.equals(elementData[i]))
                    return i;
        }
        return  -1;//return -1
    }

    public int lastIndexOf(Object o) {
        if (o == null) {//Object o is null, then return the index value of the last null object in the array
            for (int i = size-1; i >= 0; i--)//Reverse traversal
                if (elementData[i] == null)
                    return i;
        } else  {//Object o is not null, then according to the equals() comparison result, return the index value of the last occurrence in the array
            for (int i = size-1; i >= 0; i--)//Reverse traversal
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    public boolean add(E e) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;//Add element e to the end of the array
        return true;
    }

    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }

    public E remove(int index) {
        rangeCheck(index);
        E oldValue = elementData(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null;
        return oldValue;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        elementData[--size] = null; // clear to let GC do its work
    }

    public void clear() {
        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elementData[i] = null;

        size = 0;
    }

    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }
}