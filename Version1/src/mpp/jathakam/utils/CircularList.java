package mpp.jathakam.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircularList<E>
	extends ArrayList<E>
{
	private static final long serialVersionUID = -1754666950270771586L;

	private boolean isCircular = true; 
	private int startIndex = 0;
	
	public CircularList()
	{
		this(true);
	}

	public CircularList(boolean isCircular)
	{
		super();
		this.isCircular = isCircular;
	}
	
	public CircularList(List<E> list, boolean isCircular)
	{
		this(isCircular);
		addAll(list);
	}

	public void setStartIndex(int index)
	{
		if (index >= size())
		{
			throw new IndexOutOfBoundsException();
		}
		
		startIndex = index;
	}
	
	public void setStartIndex(E object)
	{
		if (!contains(object))
		{
			throw new IllegalArgumentException(object + " object not found in the list");
		}
		
		int index = indexOf(object);
		setStartIndex(index);
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return iterator(startIndex);
	}

	public Iterator<E> infiniteIterator()
	{
		return infiniteIterator(startIndex);
	}
	
	public Iterator<E> iterator(E object)
	{
		if (!contains(object))
		{
			throw new IllegalArgumentException(object + " object not found in the list");
		}

		int index = indexOf(object);

		return iterator(index);
	}

	public Iterator<E> iterator(final int index)
	{
		Iterator<E> iter = new Iterator<E>()
		{
			int currentItem = 0;
			int listSize = CircularList.this.size();
			int startIndex = index;
			
			@Override
			public boolean hasNext()
			{
				if (isCircular)
				{
					if  (currentItem < listSize)
						return true;
					else
						return false;
				}
				
				if (startIndex == listSize)
				{
					return false;
				}

				return true;
			}

			@Override
			public E next()
			{
				currentItem++;
				if (isCircular && startIndex == listSize)
				{
					startIndex = 0;
				}

				if (startIndex == listSize)
				{
					return null;
				}

				return get(startIndex++);
			}

			@Override
			public void remove()
			{
			}
		};
		
		return iter;
	}
	
	public Iterator<E> infiniteIterator(final int index)
	{
		Iterator<E> iter = new Iterator<E>()
		{
			int currentItem = 0;
			int listSize = CircularList.this.size();
			int startIndex = index;
			
			@Override
			public boolean hasNext()
			{
				return true;
			}

			@Override
			public E next()
			{
				currentItem++;
				if (isCircular && startIndex == listSize)
				{
					startIndex = 0;
				}

				if (startIndex == listSize)
				{
					return null;
				}

				return get(startIndex++);
			}

			@Override
			public void remove()
			{
			}
		};
		
		return iter;
	}
}
