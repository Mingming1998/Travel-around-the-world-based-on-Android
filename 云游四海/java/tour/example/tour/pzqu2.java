package tour.example.tour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pzqu2 {

    // 地址
    private static final String URL = "http://piao.qunar.com/ticket/list.htm?keyword=菏泽&region=&from=mps_search_suggest";
    // 获取每个页面的url
    private static final String url="/ticket/detail_.*?from=mps_search_suggest";
    //获取景点名称
   private static final String name1="title=\"水浒好汉城\">.*?</a>";
    //获取景点评分

    //获取价格

    //获取具体的地址




    public static void main(String[] args) {
        try {
            pzqu2 cm=new pzqu2();
            //获得html文本内容
            String HTML = cm.getHtml(URL);
            System.out.println("总url："+URL);
            //System.out.println(HTML);
            List<String> imgUrl = cm.getImageUrl(HTML);
            for(int i=0;i<imgUrl.size();i++){
                System.out.println("页面内url: "+imgUrl.get(i));
                String su="http://piao.qunar.com" +imgUrl.get(i);
                String Hname1 = cm.getHtml(su);
                System.out.println(su);
                String na1=getName1(Hname1,name1);
                System.out.println(na1);
            }

        }catch (Exception e){
            System.out.println("发生错误");
            e.printStackTrace();
        }

    }

    private  static String getName1(String html,String name1){



//        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(html.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
//
//        String line;
//
//        StringBuffer strbuf=new StringBuffer();
//
//        try{
//            while ( (line = br.readLine()) != null ) {
//
//
//                if(!line.trim().equals("")&&(line.indexOf("title=\"水浒好汉城\"")!=-1)){
//
//                    //if(line.contains("mp-description-name"));
////                   if((line.indexOf("head=")!=-1)){
////                        //System.out.println("title:"+line);
////                        System.out.println("indesof"+line.indexOf("head"));
////                  }
//                System.out.println("index: "+line);
//
//                }
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        Matcher matcher=Pattern.compile(name1).matcher(html);
        while(matcher.find()){
            String u=matcher.group();
            return u;
        }

        return "none!";
    }

    //获取Url地址
    private List<String> getImageUrl(String html){
        Matcher matcher=Pattern.compile(url).matcher(html);
        List<String>listimgurl=new ArrayList<String>();
        while (matcher.find()){
            String u=matcher.group();
            if(!exist(u,listimgurl)){
                listimgurl.add(u);
            }
        }
        return listimgurl;
    }

    //获取HTML内容
    private String getHtml(String url)throws Exception{
        URL url1=new URL("http://piao.qunar.com/ticket/list.htm?keyword=菏泽&region=&from=mps_search_suggest");
        URLConnection connection=url1.openConnection();
        InputStream in=connection.getInputStream();
        InputStreamReader isr=new InputStreamReader(in);
        BufferedReader br=new BufferedReader(isr);

        String line;
        StringBuffer sb=new StringBuffer();
        while((line=br.readLine())!=null){
            sb.append(line,0,line.length());
            sb.append('\n');
        }
        br.close();
        isr.close();
        in.close();
        return sb.toString();
    }



    private boolean exist(String u,List<String> list){
        for(int i=0;i<list.size();i++){
            if(u.equals(list.get(i))){
                return true;
            }
        }
        return false;
    }

}