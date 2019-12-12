package tk.techreboot.apps.sd2048ui;

import android.content.DialogInterface;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import static android.view.GestureDetector.*;
import static java.lang.StrictMath.abs;

public class MainActivity extends AppCompatActivity implements OnGestureListener {
    private int[][] board,previousBoard;
    private int level;
    private int inputDirection;
    private TextView tv_score;
    private Random random;
    private int score;
    private GridLayout gridLayout;
    private int maxDigit=2;
    private int height;
    private AlertDialog.Builder alert;
    private ImageView info_IV;
    private Button btn_undo;
    private int keepGoing;

    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    GestureDetectorCompat gestureDetectorCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_score = (TextView) findViewById(R.id.score);
        btn_undo = (Button)findViewById(R.id.undo);

        info_IV  = (ImageView) findViewById(R.id.ImageViewInfo);
        
        level = getIntent().getIntExtra("level",4); // data from the home activity

        board = new int[level][level];
        previousBoard = new int[level][level];
        random = new Random();
        score=0;
        keepGoing=0;

        height = getWindowManager().getDefaultDisplay().getWidth();

        alert = new AlertDialog.Builder(this);



        gridLayout = (GridLayout) findViewById(R.id.boardGridView);
        gridLayout.setRowCount(level);
        gridLayout.setColumnCount(level);


        addDigit();
        addDigit();
        displayBoard();


        gestureDetectorCompat = new GestureDetectorCompat(this,this);

        onClickInfo();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onClickInfo(){
        info_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setTitle("Game Rule of 2048")
                        .setMessage("You need to make 2048 number. The number will twice after collision of same number. Swipe LEFT RIGHT UP DOWN to play this Game. Remember game will over if you fill all boxes.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();
                            }
                        })
                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });
    }

    public void displayBoard(){

        gridLayout.removeAllViews();
        int i,j,c=0;
        for(i=0;i<level;i++){
            for (j=0;j<level;j++){
                if(board[i][j]>maxDigit){
                    maxDigit=board[i][j];
                }
                else if(board[i][j]>0){
                    c++;
                }
                gridLayout.addView(new DigitToTile(this,board[i][j],level,height));
            }
        }
        tv_score.setText("Score : "+score);
        if(maxDigit>=2048 && keepGoing==0){
            onWin();
        }
    }

    public Boolean addDigit(){
        int f=0,row,column;
        while (f<level){
            row = random.nextInt(level);
            column = Arrays.binarySearch(board[row],0);
            if(column>=0){
                board[row][column]=2;
                return true;
            }
            f++;
        }
        if(f==level){
            for(row=0;row<level;row++){
                for(column=0;column<level;column++){
                    if(board[row][column]==0){
                        board[row][column]=2;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void playGame(){
        btn_undo.setEnabled(true);
        int i,j,k,c;
        for(i=0;i<level;i++){
            for(j=0;j<level;j++){
                previousBoard[i][j]=board[i][j];
            }
        }

        switch (inputDirection){
            case 8: // up logic
                for(i=0;i<level;i++){
                    for(j=0;j<level-1;j++){
                        c=0;
                        while(board[j][i]==0 & c<level){
                            c++;
                            for(k=j;k<(level-1);k++){
                                board[k][i] = board[k+1][i];
                            }
                            board[k][i]=0;
                        }
                        if(board[j][i]==board[j+1][i]){
                            board[j][i] = 2*board[j][i];
                            score += board[j][i];
                            board[j+1][i]=0;
                        }
                    }
                }
                break;
            case 2: //down logic
                for(i=0;i<level;i++){
                    for(j=level-1;j>0;j--){
                        c=0;
                        while(board[j][i]==0 && c<level){
                            c++;
                            for(k=j;k>0;k--){
                                board[k][i] = board[k-1][i];
                            }
                            board[k][i]=0;
                        }
                        if(board[j][i]==board[j-1][i]){
                            board[j][i] = 2*board[j][i];
                            score += board[j][i];
                            board[j-1][i]=0;
                        }
                    }
                }
                break;
            case 6://right logic
                for(i=0;i<level;i++){
                    for(j=level-1;j>0;j--){
                        c=0;
                        while(board[i][j]==0 && c<level){
                            c++;
                            for(k=j;k>0;k--){
                                board[i][k] = board[i][k-1];
                            }
                            board[i][k]=0;
                        }
                        if(board[i][j]==board[i][j-1]){
                            board[i][j] = 2*board[i][j];
                            score += board[i][j];
                            board[i][j-1]=0;
                        }
                    }
                }
                break;
            case 4:// left logic
                for(i=0;i<level;i++){
                    for(j=0;j<level-1;j++){
                        c=0;
                        while(board[i][j]==0 && c<level){
                            c++;
                            for(k=j;k<(level-1);k++){
                                board[i][k] = board[i][k+1];
                            }
                            board[i][k]=0;
                        }
                        if(board[i][j]==board[i][j+1]){
                            board[i][j] = 2*board[i][j];
                            score += board[i][j];
                            board[i][j+1]=0;
                        }
                    }
                }
                break;
        }
        //Log.v("Shanu : ",""+addDigit());
        if(addDigit()==false){
            onGameOver();
        }
        displayBoard();
    }

    public void onClickRestart(View view){
        alert.setTitle("Restart");
        alert.setMessage("Would you want to RESTART?");
        alert.setCancelable(false);
        alert.setNegativeButton(" No ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        return;
                    }
                });
        alert.setPositiveButton(" Yes ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn_undo.setEnabled(false);
                board = new int[level][level];
                addDigit();
                addDigit();
                score=0;
                displayBoard();
            }
        });
        alert.show();
    }

    public void onClickUndo(View view){
        int i,j;
        for(i=0;i<level;i++){
            for(j=0;j<level;j++){
                board[i][j]=previousBoard[i][j];
            }
        }
        displayBoard();
        btn_undo.setEnabled(false);
    }

    public void onWin(){
        alert.setCancelable(false)
                .setTitle("  Winner")
                .setMessage("You have win the game. Do you want to restart or keep going")
                .setPositiveButton("Keep going", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keepGoing=1;
                    }
                })
                .setNegativeButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_undo.setEnabled(false);
                        board = new int[level][level];
                       // board[random.nextInt(level)][random.nextInt(level)]=2;
                        addDigit();
                        addDigit();
                        score=0;
                        displayBoard();
                        dialog.cancel();
                    }
                })
                .show();


    }

    public void onGameOver(){
        alert.setCancelable(false)
                .setTitle("  Game Over")
                .setMessage("The game is over now. Do you wan to Restart or Undo?")
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btn_undo.setEnabled(false);
                        board = new int[level][level];
                        addDigit();
                        addDigit();
                        score=0;
                        displayBoard();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Undo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i,j;
                        for(i=0;i<level;i++){
                            for(j=0;j<level;j++){
                                board[i][j]=previousBoard[i][j];
                            }
                        }
                        displayBoard();
                        btn_undo.setEnabled(false);
                    }
                })
                .show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int difX = (int) (e1.getX()-e2.getX());
        int difY = (int) (e1.getY()-e2.getY());

        if(abs(difX)>SWIPE_MIN_DISTANCE  && abs(velocityX) >SWIPE_THRESHOLD_VELOCITY){
            if(difX>0){
                //left
                inputDirection = 4;
            }
            else{
                //right
                inputDirection = 6;
            }
            playGame();
            return true;
        }
        else if(abs(difY)>SWIPE_MIN_DISTANCE && abs(velocityY) >SWIPE_THRESHOLD_VELOCITY){
            if(difX>0){
                //down
                inputDirection = 2;
            }
            else {
                //up
                inputDirection = 8;
            }
            playGame();
            return true;
        }
        return false;
    }

}

