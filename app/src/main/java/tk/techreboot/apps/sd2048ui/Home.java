package tk.techreboot.apps.sd2048ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    private RatingBar rb_level;
    private TextView tv_level;
    private int row=4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       rb_level = (RatingBar)findViewById(R.id.ratingBar);
        tv_level = (TextView) findViewById(R.id.textViewLevel);

        rb_level.setRating(1);
        tv_level.setText("< 4 X 4 >");

        rb_level.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                row = (int) rating+3;
                tv_level.setText("< "+row+" X "+row+" >");
            }
        });

    }


    public void onStartClick(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("level",row);
        startActivity(intent);
        //finish();
    }
}
