package tour.example.tour;

/**
 * Created by hh on 2017/3/24.
 */
import com.google.gson.Gson;

public class GsonService {

    public static <T> T parseJson(String jsonString, Class<T> clazz) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, clazz);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("解析json失败");
        }
        return t;

    }
}
