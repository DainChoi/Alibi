package com.dproject.alibi;

public class MyWork {
    private String title;     // 매장 이름
    private String workid;     // 매장 ID
    private String address;    // 매장 주소

    public MyWork(String title, String workid, String address) {
        this.title = title;
        this.workid = workid;
        this.address = address;
    }

    public MyWork() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
