package com.poscodx.mysite.vo;

public class Page {
	private int pageNo;
	private int listSize;
	private String query;
	private int type;

	public Page() {
		this(1, 5);
	}

	public Page(int pageNo) {
		this(pageNo, 5);
	}

	public Page(int pageNo, int listSize) {
		this.pageNo = pageNo;
		this.listSize = listSize;
	}

	public int getBegin() {
		return (pageNo - 1) * this.listSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
