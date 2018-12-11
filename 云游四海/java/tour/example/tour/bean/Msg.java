package tour.example.tour.bean;

/**
 * Created by dream on 2017/6/1.
 */

public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String content;

    public int type;

    public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }
}
