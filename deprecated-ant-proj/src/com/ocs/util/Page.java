package com.ocs.util;

import java.util.Collections;
import java.util.List;

/**
 * @author Tiger Tang (tangtianguang@gmail.com)
 * @since 1.0
 */
public interface Page<T> {

    final int DEFAULT_PAGE_NUMBER = 1;

    final int DEFAULT_PAGESIZE = 5;    

    /**
     * 返回当前页是否是第一页。
     *
     * @return true表示当前页是否是第一页。
     */
    boolean isFirstPage();

    /**
     * 返回当前页是否已经是最后一页。
     *
     * @return true表示当前页已经是最后一页。
     */
    boolean isLastPage();

    /**
     * 返回是否有下一页
     *
     * @return true表示有下一页；否则，没有下一页。
     */
    boolean hasNextPage();

    /**
     * 获取是否有上一页
     *
     * @return true，则表示有上一页；否则没有上一页
     */
    boolean hasPreviousPage();

    /**
     * 获取最后一页的页码。
     *
     * @return 最后一页的页码
     */
    int getLastPageNumber();

    /**
     * 获取当前页的所有元素列表。
     *
     * @return 当前页的所有元素列表
     */
    List<T> getThisPageElements();

    /**
     * 获取总的元素数目。
     *
     * @return 总的元素数目
     */
    int getTotalNumberOfElements();

    /**
     * 获取当前页第一个元素的number。
     *
     * @return 当前页第一个元素的number。
     */
    int getThisPageFirstElementNumber();

    /**
     * 获取当前页最后一个元素的number。
     *
     * @return 当前页最后一个元素的number。
     */
    int getThisPageLastElementNumber();

    /**
     * 得到当前页码。
     *
     * @return 当前页码
     */
    int getPageNumber();

    /**
     * 获取下一页的页码。
     *
     * @return 下一页的页码
     */
    int getNextPageNumber();

    /**
     * 得到前一页的页码。
     *
     * @return 前一页的页码。
     */
    int getPreviousPageNumber();

    /**
     * 获得每页显示的条目数目
     *
     * @return 每页显示的条目数目
     */
    int getPageSize();

    /**
     * 返回所有元素。
     *
     * @return 所有元素的列表。
     */
    List<T> getAllElements();

    /**
     * 返回总页数。
     *
     * @return 总页数
     */
    int getTotalPages();

    @SuppressWarnings("unchecked")
	final Page EMPTY_PAGE = new Page() {

        public boolean isFirstPage() {
            return false;
        }

        public boolean isLastPage() {
            return false;
        }

        public boolean hasNextPage() {
            return false;
        }

        public boolean hasPreviousPage() {
            return false;
        }

        public int getLastPageNumber() {
            return 0;
        }

        public List getThisPageElements() {
            return Collections.EMPTY_LIST;
        }

        public int getTotalNumberOfElements() {
            return 0;
        }

        public int getThisPageFirstElementNumber() {
            return 0;
        }

        public int getThisPageLastElementNumber() {
            return 0;
        }

        public int getPageNumber() {
            return 0;
        }

        public int getNextPageNumber() {
            return 0;
        }

        public int getPreviousPageNumber() {
            return 0;
        }

        public int getStartIndex() {
            return 0;
        }

        public int getPageSize() {
            return 0;
        }

        public List getAllElements() {
            return Collections.EMPTY_LIST;
        }

        public int getTotalPages() {
            return 0;
        }

        public int getStartPage() {
            return 0;
        }

        public int getEndPage() {
            return 0;
        }
    };
}
