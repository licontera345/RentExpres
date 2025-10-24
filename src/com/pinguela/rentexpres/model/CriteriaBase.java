package com.pinguela.rentexpres.model;

public class CriteriaBase extends ValueObject {

        private static final long serialVersionUID = 1L;

        private Integer page;
        private Integer pageSize;
        private String orderBy;
        private String orderDir;

        public CriteriaBase() {
                super();
        }

        public Integer getPage() {
                return page;
        }

        public void setPage(Integer page) {
                this.page = page;
        }

        public Integer getPageNumber() {
                return page;
        }

        public void setPageNumber(Integer pageNumber) {
                this.page = pageNumber;
        }

        public Integer getPageSize() {
                return pageSize;
        }

        public void setPageSize(Integer pageSize) {
                this.pageSize = pageSize;
        }

        public String getOrderBy() {
                return orderBy;
        }

        public void setOrderBy(String orderBy) {
                this.orderBy = orderBy;
        }

        public String getOrderDir() {
                return orderDir;
        }

        public void setOrderDir(String orderDir) {
                this.orderDir = orderDir;
        }

        public int getSafePage() {
                return (page == null || page.intValue() < 1) ? 1 : page.intValue();
        }

        public int getSafePageSize() {
                int ps = (pageSize == null) ? 20 : pageSize.intValue();
                switch (ps) {
                case 10:
                case 20:
                case 25:
                case 50:
                case 100:
                        return ps;
                default:
                        return 20;
                }
        }

        public int getOffset() {
                return (getSafePage() - 1) * getSafePageSize();
        }

        public String getSafeOrderDir() {
                return "DESC".equalsIgnoreCase(orderDir) ? "DESC" : "ASC";
        }

        protected String trimToNull(String value) {
                if (value == null) {
                        return null;
                }
                String cleaned = value.trim();
                return cleaned.isEmpty() ? null : cleaned;
        }

        public void normalize() {
                // subclasses may override to adjust ranges or trim values
        }
}
