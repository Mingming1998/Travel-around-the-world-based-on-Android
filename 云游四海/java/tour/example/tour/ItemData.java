package tour.example.tour;

import java.sql.Timestamp;

/**
 * Created by Jing on 15/5/27.
 */
public class ItemData {

    int icon;//头像图标
    int num;//论坛id
    String id;//用户
    Timestamp time;//时间
    String content;//内容
    int praise;//点赞数
    int talk;//评论数


    public ItemData(int icon, int num, String id, Timestamp time, String content, int praise, int talk) {

        this.icon = icon;
        this.num = num;
        this.id = id;
        this.time = time;
        this.content = content;
        this.praise = praise;
        this.talk = talk;
    }




    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getTalk() {
        return talk;
    }

    public void setTalk(int talk) {
        this.talk = talk;
    }
}