/* 
 * Yet to decide on the license
 */
package mpp.jathakamu.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author phani
 */

public class CircularListNotUsed<E>
        extends ArrayList<E>
{

    private static final long serialVersionUID = -1754666950270771586L;

    private boolean isCircular = true;
    private int startIndex = 0;

    public CircularListNotUsed()
    {
        this(true);
    }

    public CircularListNotUsed(boolean isCircular)
    {
        super();
        this.isCircular = isCircular;
    }

    public CircularListNotUsed(List<E> list, boolean isCircular)
    {
        this(isCircular);
        addAll(list);
    }

    public CircularListNotUsed(List<E> list, boolean isCircular, int startIndex)
    {
        this(isCircular);
        addAll(list);
        this.startIndex = startIndex;
    }

    public CircularListNotUsed(List<E> list, boolean isCircular, E object)
    {
        this(isCircular);
        addAll(list);
        if (!contains(object))
        {
            throw new IllegalArgumentException(
                    object + " object not found in the list");
        }

        int index = indexOf(object);
        if (index >= size())
        {
            throw new IndexOutOfBoundsException();
        }

        startIndex = index;
    }

    @Override
    public Iterator<E> iterator()
    {
        return iterator(startIndex);
    }

    public Iterator<E> iterator(E object)
    {
        if (!contains(object))
        {
            throw new IllegalArgumentException(
                    object + " object not found in the list");
        }

        int index = indexOf(object);

        return iterator(index);
    }

    public Iterable<E> iterable(final int index)
    {
        return new Iterable<E>()
        {
            @Override
            public Iterator<E> iterator()
            {
                return CircularListNotUsed.this.iterator(index);
            }
        };
    }

    public Iterator<E> iterator(final int index)
    {
        Iterator<E> iter = new Iterator<E>()
        {
            int currentItem = 0;
            int listSize = CircularListNotUsed.this.size();
            int curIndex = index;

            @Override
            public boolean hasNext()
            {
                if (isCircular)
                {
                    if (currentItem < listSize)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                /*
                 * If list is not circular then once it reaches end of the list
                 * return false
                 */
                if (curIndex == listSize)
                {
                    return false;
                }

                return true;
            }

            @Override
            public E next()
            {
                currentItem++;
                if (isCircular && curIndex == listSize)
                {
                    curIndex = 0;
                }

                if (curIndex == listSize)
                {
                    return null;
                }

                E obj = get(curIndex);
                curIndex++;

                return obj;
            }

            @Override
            public void remove()
            {
            }
        };

        return iter;
    }
}
