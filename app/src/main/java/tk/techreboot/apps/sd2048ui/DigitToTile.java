package tk.techreboot.apps.sd2048ui;

import android.content.Context;
import android.os.Build;

public class DigitToTile extends android.support.v7.widget.AppCompatTextView {


    public DigitToTile(Context context,int digit,int level,int height) {
        super(context);

        if(digit==0){
            setText(" ");
        }
        else{
            setText(""+digit);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            setTextAlignment(TEXT_ALIGNMENT_CENTER);
        }

        setHeight(height/level);
        setWidth(height/level);
        setTextSize((height/level)/5);

        setPadding(2,5,2,2);

        if(digit==0){
            setBackgroundResource(R.drawable.tile0);
        }
        else {
            int[] tile = {
                    R.drawable.tile0,
                    R.drawable.tile2,
                    R.drawable.tile4,
                    R.drawable.tile8,
                    R.drawable.tile16,
                    R.drawable.tile32,
                    R.drawable.tile64,
                    R.drawable.tile128,
                    R.drawable.tile256,
                    R.drawable.tile512,
                    R.drawable.tile1024,
                    R.drawable.tile2048
            };
            int index = (int) (Math.log(digit) / Math.log(2));
            setBackgroundResource(tile[index]);
        }

    }
}
