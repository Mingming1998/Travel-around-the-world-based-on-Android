package  tour.example.tour;

import org.litepal.crud.DataSupport;

public class Person extends DataSupport{

    private String nickName;
    private String personal;


    public void setNickName(String nickName){
        this.nickName = nickName;

    }

    public void setPersonal(String personal){
        this.personal = personal;

    }

    public String getNickName(){
        return nickName;
    }

    public String getPersonal(){
        return personal;
    }

}

