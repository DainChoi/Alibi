package com.dproject.alibi;

public class ItemData {
    int imgID;
    String title;
    String content;

    // 생성자
    public ItemData(int imgID, String title, String content) {
        this.imgID = imgID;
        this.title = title;
        this.content = content;
    }

    // Getter와 Setter 연산자
    public int getImgID() { return imgID; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public void setImgID(int imgID) { this.imgID = imgID; }

    public void setTitle(String title) { this.title = title; }

    public void setContent(String content) { this.content = content; }
}
