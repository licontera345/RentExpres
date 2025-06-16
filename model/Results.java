package com.pinguela.rentexpres.model;

import java.util.List;

public class Results<T> {
	private List<T> results;
	private int pageNumber;
	private int pageSize;
	private int totalRecords;

	public Results() {
	}

	public Results(List<T> results, int pageNumber, int pageSize, int totalRecords) {
		this.results = results;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalRecords = totalRecords;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	
	public int getTotalPages() {
		return pageSize == 0 ? 0 : (totalRecords + pageSize - 2) / pageSize;
	}
}
