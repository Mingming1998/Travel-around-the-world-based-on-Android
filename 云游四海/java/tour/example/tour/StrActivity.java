package tour.example.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class StrActivity extends AppCompatActivity {

    private ImageButton weather_button;
    private ImageButton city_button;
    private ImageButton photo_button;
    private ImageButton friend_button;
    private ImageButton map_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str);

        weather_button = (ImageButton) findViewById(R.id.weather_button);
        weather_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrActivity.this,MiniWeaActivity.class);
                startActivity(intent);
            }
        });

        city_button = (ImageButton) findViewById(R.id.city_button);
        city_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrActivity.this,SpotActivity.class);
                startActivity(intent);
            }
        });

        photo_button = (ImageButton) findViewById(R.id.photo_button);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });

        friend_button = (ImageButton) findViewById(R.id.friend_button);
        friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrActivity.this,RefreshActivity.class);
                startActivity(intent);
            }
        });

        map_button = (ImageButton) findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StrActivity.this,cloudActivity.class);
                startActivity(intent);
            }
        });
    }
}
