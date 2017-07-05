package com.example.administrator.jkbd.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.biz.ExamBiz;
import com.example.administrator.jkbd.biz.IExamBiz;
import com.example.administrator.jkbd.view.QuestionAdapter;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {
    CheckBox[] cbs = new CheckBox[4];
    TextView[] tvs=new TextView[4];
    IExamBiz biz;
    QuestionAdapter adapter;
    boolean loadisExamInfo = false;
    boolean loadisQuestion = false;
    boolean loadisExamInfoReceive = false;
    boolean loadisQuestionReceive = false;
    LoadExamBroadCast loadexambrodcast;
    LoadQuestionBroadCast loadquestionbrodcast;

    @BindView(R.id.load_dialog) ProgressBar dialog;
    @BindView(R.id.tv_load) TextView tvload;
    @BindView(R.id.layout_loading) LinearLayout layoutloading;
    @BindView(R.id.eaxminfo) TextView tvexif;
    @BindView(R.id.tv_time) TextView tvtime;
    @BindView(R.id.tv_examnum) TextView tvnum;
    @BindView(R.id.tv_exam_title) TextView tvextitle;
    @BindView(R.id.im_exam_image) ImageView imview;
    @BindView(R.id.tv_op1) TextView tvop1;
    @BindView(R.id.tv_op2) TextView tvop2;
    @BindView(R.id.tv_op3) TextView tvop3;
    @BindView(R.id.tv_op4) TextView tvop4;
    @BindView(R.id.layout03) LinearLayout layout03;
    @BindView(R.id.layout04) LinearLayout layout04;
    @BindView(R.id.cb01) CheckBox cb01;
    @BindView(R.id.cb02) CheckBox cb02;
    @BindView(R.id.cb03) CheckBox cb03;
    @BindView(R.id.cb04) CheckBox cb04;
    @BindView(R.id.gallery) Gallery gallery;
    @BindView(R.id.btn_next) Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examstart);
        ButterKnife.bind(this);
        loadexambrodcast = new LoadExamBroadCast();
        loadquestionbrodcast = new LoadQuestionBroadCast();
        biz = new ExamBiz();
        setListener();
        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadexambrodcast != null) {
            unregisterReceiver(loadexambrodcast);
        }
        if (loadquestionbrodcast != null) {
            unregisterReceiver(loadquestionbrodcast);
        }
    }

    private void setListener() {
        registerReceiver(loadexambrodcast, new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(loadquestionbrodcast, new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
    }

    public void loadData() {
        layoutloading.setEnabled(false);
        dialog.setVisibility(View.VISIBLE);
        tvload.setText("数据下载中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                biz.beginExam();
            }
        }).start();
    }

    private void initData() {
        if (loadisExamInfoReceive && loadisQuestionReceive) {
            if (loadisExamInfo && loadisQuestion) {
                layoutloading.setVisibility(View.GONE);
                ExamInfo examInfo = ExamApplication.getInstance().getExamInfo();
                if (examInfo != null) {
                    showData(examInfo);
                    initTimer(examInfo);
                    initGallery();
                }
                showExam(biz.getExam());
            } else {
                layoutloading.setEnabled(true);
                dialog.setVisibility(View.GONE);
                tvload.setText("下载失败，点击重新下载！");
            }
        }
    }

    private void initGallery() {
        adapter = new QuestionAdapter(this);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveUserAnswer();
                showExam(biz.getExam(position));
            }
        });
    }

    private void initTimer(ExamInfo examInfo) {
        long sunTime = examInfo.getLimitTime() * 60 * 1000;
        final long overTime = sunTime + System.currentTimeMillis();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long t = overTime - System.currentTimeMillis();
                final long min = t / 1000 / 60;
                final long sec = t / 1000 % 60;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (min < 10) {
                            tvtime.setText("剩余时间:0" + min + "分" + sec + "秒");
                        }
                        if (sec < 10) {
                            tvtime.setText("剩余时间:0" + min + "分" + "0" + sec + "秒");
                        }
                    }
                });
            }
        }, 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commit(null);
                    }
                });
            }
        }, sunTime);
    }

    private void showExam(Question que) {
        if (que != null) {
            tvnum.setText(biz.getExamIndex());
            tvextitle.setText(que.getQuestion());
            tvop1.setText(que.getItem1());
            tvop2.setText(que.getItem2());
            tvop3.setText(que.getItem3());
            tvop4.setText(que.getItem4());
            layout03.setVisibility(que.getItem3().equals("") ? View.GONE : View.VISIBLE);
            cb03.setVisibility(que.getItem3().equals("") ? View.GONE : View.VISIBLE);
            layout04.setVisibility(que.getItem4().equals("") ? View.GONE : View.VISIBLE);
            cb04.setVisibility(que.getItem4().equals("") ? View.GONE : View.VISIBLE);
            if (que.getUrl() != null && !que.getUrl().equals("")) {
                imview.setVisibility(View.VISIBLE);
                Picasso.with(ExamActivity.this).load(que.getUrl()).into(imview);
            } else {
                imview.setVisibility(View.GONE);
            }
            resetOptions();
            String userAnswer = que.getUserAnswer();
            if (userAnswer != null && !userAnswer.equals("")) {
                int usercb = Integer.parseInt(userAnswer) - 1;
                cbs[usercb].setChecked(true);
                setOptions(true);
                setAnswerTextColor(userAnswer,que.getAnswer());
            } else {
                setOptions(false);
                setOptionsColor();
            }
        }
    }

    private void setOptionsColor() {
        for(TextView tvOp:tvs){
            tvOp.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setAnswerTextColor(String userAnswer, String answer) {
        int ra=Integer.parseInt(answer)-1;
        for (int i=0;i<tvs.length;i++){
            if(i==ra){
                tvs[i].setTextColor(getResources().getColor(R.color.green));
            }else {
                if (!userAnswer.equals(answer)) {
                    int ua=Integer.parseInt(userAnswer)-1;
                    if(i==ua){
                        tvs[i].setTextColor(getResources().getColor(R.color.red));
                    }else {
                        tvs[i].setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }
        }
    }


    private void saveUserAnswer() {
        for (int i = 0; i < cbs.length; i++) {
            if (cbs[i].isChecked()) {
                biz.getExam().setUserAnswer(String.valueOf(i + 1));
                adapter.notifyDataSetChanged();
                return;
            }
        }
        biz.getExam().setUserAnswer("");
        adapter.notifyDataSetChanged();
    }

    private void resetOptions() {
        for (CheckBox cb : cbs) {
            cb.setChecked(false);
        }
    }

    private void showData(ExamInfo examInfo) {
        tvexif.setText(examInfo.toString());
    }

    private void initView() {
        cbs[0] = cb01;
        cbs[1] = cb02;
        cbs[2] = cb03;
        cbs[3] = cb04;
        tvs[0] =tvop1;
        tvs[1] =tvop2;
        tvs[2] =tvop3;
        tvs[3] =tvop4;
        cb01.setOnCheckedChangeListener(Listener);
        cb02.setOnCheckedChangeListener(Listener);
        cb03.setOnCheckedChangeListener(Listener);
        cb04.setOnCheckedChangeListener(Listener);
    }

    @OnClick(R.id.layout_loading) void onLoadClick(){
        loadData();
    }

    CompoundButton.OnCheckedChangeListener Listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                int userAnswer = 0;
                switch (buttonView.getId()) {
                    case R.id.cb01:
                        userAnswer = 1;
                        break;
                    case R.id.cb02:
                        userAnswer = 2;
                        break;
                    case R.id.cb03:
                        userAnswer = 3;
                        break;
                    case R.id.cb04:
                        userAnswer = 4;
                        break;
                }
                if (userAnswer > 0) {
                    for (CheckBox cb : cbs) {
                        cb.setChecked((false));
                    }
                    cbs[userAnswer - 1].setChecked(true);
                }
            }
        }
    };


    public void perExam(View view) {
        saveUserAnswer();
        showExam(biz.preQuestion());
    }

    public void nextExam(View view) {
        saveUserAnswer();
        showExam(biz.nextQuestion());
    }

    public void commit(View view) {
        saveUserAnswer();
        int result = biz.commitExam();
        View inflate = View.inflate(this, R.layout.activity_result, null);
        TextView tvresult = (TextView) inflate.findViewById(R.id.tv_result);
        tvresult.setText("你的分数为:\n" + result + "分！");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.exam_commit32x32).setTitle("交卷").setView(inflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }

    public void setOptions(boolean options) {
        for (CheckBox cb : cbs) {
            cb.setEnabled(!options);
        }
    }

    class LoadExamBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            Log.e("LoadExamBroadcast", "LoadExamBroadcast,isSuccess=" + isSuccess);
            if (isSuccess) {
                loadisExamInfo = true;
            }
            loadisExamInfoReceive = true;
            initData();
        }
    }

    class LoadQuestionBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            Log.e("LoadQuestionBroadcast", "LoadQuestionBroadcast,isSuccess=" + isSuccess);
            if (isSuccess) {
                loadisQuestion = true;
            }
            loadisQuestionReceive = true;
            initData();
        }
    }
}