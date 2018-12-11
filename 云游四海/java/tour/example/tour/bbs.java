package tour.example.tour;

public class bbs {
    private String name;
    private  String time;
    private String content;
    private  String praise;
    private String comment;

    public bbs(String name, String time, String content, String praise, String comment) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.praise = praise;
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getPraise() {
        return praise;
    }

    public String getComment() {
        return comment;
    }

}
