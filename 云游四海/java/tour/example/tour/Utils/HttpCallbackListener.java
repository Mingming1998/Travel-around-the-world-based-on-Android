package tour.example.tour.Utils;

/**
 * Created by dream on 2017/5/30.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
