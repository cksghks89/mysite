package com.poscodx.mysite.vo;

public class PageResult {
	private int pageNo;
	private int count;
	private String query;

	private int tabSize;
	private int listSize;

	private int beginPage;
	private int endPage;
	private boolean prev;
	private boolean next;
	private boolean prevTab;
	private boolean nextTab;

	private int startNo;

	public PageResult(int pageNo, int count, String query) {
		this(pageNo, count, 5, 5, query);
	}

	public PageResult(int pageNo, int count, int listSize, int tabSize, String query) {
		this.pageNo = pageNo;
		this.count = count;
		this.listSize = listSize;
		this.tabSize = tabSize;
		this.query = query;

		int currTab = (pageNo - 1) / tabSize + 1;
		int lastPage = (count - 1) / listSize + 1;
		this.beginPage = (currTab - 1) * tabSize + 1;
		this.endPage = (currTab * tabSize > lastPage) ? lastPage : currTab * tabSize;

		this.prev = beginPage != 1;
		this.next = endPage != lastPage;

		this.prevTab = pageNo != 1;
		this.nextTab = pageNo != lastPage;

		this.startNo = count - (listSize * (pageNo - 1));
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getTabSize() {
		return tabSize;
	}

	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public int getBeginPage() {
		return beginPage;
	}

	public void setBeginPage(int beginPage) {
		this.beginPage = beginPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public boolean isPrevTab() {
		return prevTab;
	}

	public void setPrevTab(boolean prevTab) {
		this.prevTab = prevTab;
	}

	public boolean isNextTab() {
		return nextTab;
	}

	public void setNextTab(boolean nextTab) {
		this.nextTab = nextTab;
	}

	public int getStartNo() {
		return startNo;
	}

	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}

}
