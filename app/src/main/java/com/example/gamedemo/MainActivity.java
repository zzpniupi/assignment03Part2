package com.example.gamedemo;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView[] pics = new ImageView[9];
    private TextView tv_score;
    private Boolean[] canhit={false,false,false,false,false,false,false,false,false};
    int score=0;
    MyAsyncTask myAsyncTask;
    private Button btn;
    private int btn_status=0;
    private int TIME = 1000;  //每隔2s执行一次.
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn_start);
        btn.setOnClickListener(this);
        init();
    }

    void init(){
        pics[0]=(ImageView) findViewById(R.id.hole01);
        pics[1]=(ImageView) findViewById(R.id.hole02);
        pics[2]=(ImageView) findViewById(R.id.hole03);
        pics[3]=(ImageView) findViewById(R.id.hole04);
        pics[4]=(ImageView) findViewById(R.id.hole05);
        pics[5]=(ImageView) findViewById(R.id.hole06);
        pics[6]=(ImageView) findViewById(R.id.hole07);
        pics[7]=(ImageView) findViewById(R.id.hole08);
        pics[8]=(ImageView) findViewById(R.id.hole09);
        tv_score = (TextView) findViewById(R.id.score);
        btn=(Button)findViewById(R.id.btn_start);

        for(int i=0;i<pics.length;i++){
            final int n=i;
            pics[n].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v==pics[n]){
                        if(canhit[n]){
                            canhit[n]=false;
                            score+=10;
                            tv_score.setText(""+score);
                            pics[n].setImageDrawable(getResources().getDrawable(R.drawable.hit));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Log.v("newt","newthread");
        if (v.getId() == R.id.btn_start){
            if(btn_status==0){
                btn_status=1;
                btn.setText("Stop");
                score=0;
                tv_score.setText(""+score);
                handler.postDelayed(runnable, TIME);
            }
            else if(btn_status==1){
                btn_status=0;
                btn.setText("Start");
                handler.removeCallbacks(runnable);
                for(int i=0;i<9;i++){
                    canhit[i]=false;
                    pics[i].setImageDrawable(getResources().getDrawable(R.drawable.emptyhole));
                }
                //myAsyncTask.cancel(true);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, TIME);
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    class MyAsyncTask extends AsyncTask<String,Integer,String>{

        protected void onPreExecute(){

        }

        //((ImageView)findViewById(R.id.hole01)).setImageDrawable(getResources().getDrawable(R.drawable.show6));
        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<9;i++){
                canhit[i]=false;
                pics[i].setImageDrawable(getResources().getDrawable(R.drawable.emptyhole));
            }
            int[] position = getRandom();
            for(int i=0;i<position.length;i++){
                canhit[position[i]]=true;
                pics[position[i]].setImageDrawable(getResources().getDrawable(R.drawable.show6));
            }
            return null;
        }
    }

    public int[] getRandom() {
        int startArray[] = {0,1,2,3,4,5,6,7,8};//seed array
        int N = 3;//随机数个数
        int[] resArray = new int[N];
        Random r = new Random();
        for(int i = 0; i < N; i++)
        {
            int seed = r.nextInt(startArray.length-i);//从剩下的随机数里生成
            resArray[i] = startArray[seed];//赋值给结果数组
            startArray[seed] = startArray[startArray.length - i-1];//把随机数产生过的位置替换为未被选中的值。
        }
        return resArray;
    }
}
