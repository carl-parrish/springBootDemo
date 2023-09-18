package com.sourcecodesamurai;
    
import java.util.Date;
    
public class PageView {
    private String userName;
    private String broswer;
    private String page;
    private Date viewDate;
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getBroswer() {
        return broswer;
    }
    
    public void setBroswer(String broswer) {
        this.broswer = broswer;
    }
    
    public String getPage() {
        return page;
    }
    
    public void setPage(String page) {
        this.page = page;
    }
    
    public Date getViewDate() {
        return viewDate;
    }
    
    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }
    
    @Override
    public String toString() {
        return "PageView{" +
                "userName='" + userName + '\'' +
                ", broswer='" + broswer + '\'' +
                ", page='" + page + '\'' +
                ", viewDate=" + viewDate +
                '}';
    }
    
}
