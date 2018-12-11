package tour.example.tour;

public class Spots {
    String jingdian;
    String jieshao;

    public Spots(String jingdian, String jieshao) {
        this.jingdian = jingdian;
        this.jieshao = jieshao;
    }
    public Spots(){

    }

    public String getJingdian() {
        return jingdian;
    }

    public void setJingdian(String jingdian) {
        this.jingdian = jingdian;
    }

    public String getJieshao() {
        return jieshao;
    }

    public void setJieshao(String jieshao) {
        this.jieshao = jieshao;
    }
}
