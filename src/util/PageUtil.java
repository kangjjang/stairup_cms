package util;


public class PageUtil
{

    public PageUtil()
    {
        rowCount = 20;
        pageNo = 1;
        totalRecord = 0;
        pageGroup = 10;
        startViewPage = 0;
        endViewPage = 0;
    }

    public PageUtil(int rowCount, int pageGroup, int pageNo, int totalRecord)
    {
        this.rowCount = 20;
        this.pageNo = 1;
        this.totalRecord = 0;
        this.pageGroup = 10;
        startViewPage = 0;
        endViewPage = 0;
        this.rowCount = rowCount;
        this.pageGroup = pageGroup;
        this.pageNo = pageNo;
        this.totalRecord = totalRecord;
        totalPage = processTotalPage(totalRecord, rowCount);
    }

    public void setRowCount(int pRowCount)
    {
        rowCount = pRowCount;
    }

    public void setPageNo(int pPageNo)
    {
        pageNo = pPageNo;
    }

    public void setTotalRecord(int pTotalRecord)
    {
        totalRecord = pTotalRecord;
    }

    public void setPageGroup(int pPageGroup)
    {
        pageGroup = pPageGroup;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public int getTotalRecord()
    {
        return totalRecord;
    }

    public int getPageGroup()
    {
        return pageGroup;
    }

    public void setTotalPage(int totalPage)
    {
        this.totalPage = totalPage;
    }

    public void setTotalPage()
    {
        totalPage = processTotalPage(totalRecord, rowCount);
    }

    public int processTotalPage(int totalRecord, int pageSize)
    {
        return (int)Math.ceil((double)totalRecord / (double)pageSize);
    }

    public int processTotalBlock(int totalPage, int currentPage)
    {
        return (totalPage - 1) / currentPage;
    }

    public void pageViewProcess()
    {
        StringBuffer view = new StringBuffer();
        int totalBlock = 0;
        if(totalPage == 0)
            totalPage = 1;
        startViewPage = ((pageNo - 1) / pageGroup) * pageGroup + 1;
        endViewPage = (((pageNo - 1) + pageGroup) / pageGroup) * pageGroup;
    }

    public int getTotalPage()
    {
        return totalPage;
    }

    public int getStartViewPage()
    {
        return startViewPage;
    }

    public int getEndViewPage()
    {
        return endViewPage;
    }

    private int rowCount;
    private int pageNo;
    private int totalRecord;
    private int pageGroup;
    private int totalPage;
    private int startViewPage;
    private int endViewPage;
}
