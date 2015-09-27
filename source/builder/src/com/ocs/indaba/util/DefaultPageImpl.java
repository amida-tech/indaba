/**
 *  Copyright 2010 OpenConcept Systems,Inc. All rights reserved.
 */

package com.ocs.indaba.util;

import java.io.Serializable;
import java.util.List;

/**
 * @author TianGuang
 */
public class DefaultPageImpl<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = 3455774971597942213L;
	
	protected int pageNumber;
    protected int pageSize;
    protected int totalElementCount;

    protected List<T> elements;

    public DefaultPageImpl() {
    }

    public DefaultPageImpl(List<T> elements, int totalElementCount) {
        this(DEFAULT_PAGE_NUMBER, DEFAULT_PAGESIZE, totalElementCount, elements);
    }

    public DefaultPageImpl(int pageNumber, int pageSize, int totalElementCount, List<T> elements) {
        if (pageNumber < 0)
        	pageNumber = DEFAULT_PAGE_NUMBER;

        if (pageSize < 1)
            pageSize = Page.DEFAULT_PAGESIZE;

        this.pageSize = pageSize;
        this.totalElementCount = totalElementCount;
        this.elements = elements;

        this.pageNumber = pageNumber;
        if (pageNumber == 0) pageNumber = 1;
    }

    public List<T> getAllElements() {
        return this.elements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalNumberOfElements() {
        return totalElementCount;
    }

    /**
     * {@inheritDoc}
     */
    public int getTotalPages() {
        int totalPage = this.totalElementCount / pageSize;
        return this.totalElementCount % pageSize == 0 ? totalPage
                : totalPage + 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getPreviousPageNumber() {
        int previous = pageNumber - 1;
        return previous > 0 ? (previous) : 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getNextPageNumber() {
        int next = pageNumber + 1;
        int lastPageNumber = getLastPageNumber();
        return next <= lastPageNumber ? next : lastPageNumber;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isFirstPage() {
        return getPageNumber() == 1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastPage() {
        return getPageNumber() >= getLastPageNumber();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNextPage() {
        return getLastPageNumber() > getPageNumber();
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasPreviousPage() {
        return getPageNumber() > 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getLastPageNumber() {
        return totalElementCount % this.pageSize == 0 ? totalElementCount / this.pageSize : totalElementCount / this.pageSize + 1;
    }

    public List<T> getThisPageElements() {
        return this.elements;
    }

    /**
     * {@inheritDoc}
     */
    public int getThisPageFirstElementNumber() {
        return (getPageNumber() - 1) * getPageSize() + 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getThisPageLastElementNumber() {
        int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
        return getTotalNumberOfElements() < fullPage ? getTotalNumberOfElements()
                : fullPage;
    }

    /**
     * {@inheritDoc}
     */
    public int getPageNumber() {
        return pageNumber;
    }
}

