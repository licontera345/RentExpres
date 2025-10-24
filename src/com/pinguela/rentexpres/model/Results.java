package com.pinguela.rentexpres.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class Results<T> implements Serializable {

        private static final long serialVersionUID = 1L;

        private List<T> items;
        private int page;
        private int pageSize;
        private int total;
        private int totalPages;
        private boolean hasPrev;
        private boolean hasNext;
        private int fromRow;
        private int toRow;

        public Results() {
                super();
        }

        public List<T> getItems() {
                return items;
        }

        public void setItems(List<T> items) {
                this.items = items;
        }

        public List<T> getResults() {
                return items;
        }

        public void setResults(List<T> results) {
                this.items = results;
        }

        public int getPage() {
                return page;
        }

        public void setPage(int page) {
                this.page = page;
        }

        public Integer getPageNumber() {
                return Integer.valueOf(page);
        }

        public void setPageNumber(Integer pageNumber) {
                this.page = pageNumber == null ? 0 : pageNumber.intValue();
        }

        public int getPageSize() {
                return pageSize;
        }

        public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
        }

        public Integer getPageSizeObject() {
                return Integer.valueOf(pageSize);
        }

        public void setPageSize(Integer pageSize) {
                this.pageSize = pageSize == null ? 0 : pageSize.intValue();
        }

        public int getTotal() {
                return total;
        }

        public void setTotal(int total) {
                this.total = total;
        }

        public Integer getTotalRecords() {
                return Integer.valueOf(total);
        }

        public void setTotalRecords(Integer totalRecords) {
                this.total = totalRecords == null ? 0 : totalRecords.intValue();
        }

        public int getTotalPages() {
                return totalPages;
        }

        public void setTotalPages(int totalPages) {
                this.totalPages = totalPages;
        }

        public boolean isHasPrev() {
                return hasPrev;
        }

        public void setHasPrev(boolean hasPrev) {
                this.hasPrev = hasPrev;
        }

        public boolean isHasNext() {
                return hasNext;
        }

        public void setHasNext(boolean hasNext) {
                this.hasNext = hasNext;
        }

        public int getFromRow() {
                return fromRow;
        }

        public void setFromRow(int fromRow) {
                this.fromRow = fromRow;
        }

        public int getToRow() {
                return toRow;
        }

        public void setToRow(int toRow) {
                this.toRow = toRow;
        }

        public void normalize() {
                if (items == null) {
                        items = Collections.<T>emptyList();
                }
                if (page < 1) {
                        page = 1;
                }
                if (pageSize != 10 && pageSize != 20 && pageSize != 25 && pageSize != 50 && pageSize != 100) {
                        pageSize = 20;
                }
                if (total < 0) {
                        total = 0;
                }
                totalPages = (total == 0) ? 1 : ((total + pageSize - 1) / pageSize);
                if (page > totalPages) {
                        page = totalPages;
                }
                hasPrev = page > 1;
                hasNext = page < totalPages;
                if (total == 0) {
                        fromRow = 0;
                        toRow = 0;
                } else {
                        fromRow = (page - 1) * pageSize + 1;
                        toRow = Math.min(page * pageSize, total);
                }
        }
}
