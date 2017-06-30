package com.example.administrator.jkbd;

import android.app.Application;
import android.util.Log;

import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.bean.Result;
import com.example.administrator.jkbd.util.OkHttpUtils;
import com.example.administrator.jkbd.util.ResultUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExamApplication extends Application {
    ExamInfo examinfo;
    List<Question> ExamList;
    private  static ExamApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        initData();
    }

    public static ExamApplication getInstance(){
        return  instance;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils<ExamInfo> utils=new OkHttpUtils<>(instance);
                String uri="http://101.251.196.90:8080/JztkServer/examInfo";
                utils.url(uri).targetClass(ExamInfo.class)
                        .execute(new OkHttpUtils.OnCompleteListener<ExamInfo>() {
                            @Override
                            public void onSuccess(ExamInfo result) {
                                Log.e("main","result="+result);
                                examinfo=result;
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("main","error="+error);
                            }
                        });
                OkHttpUtils<String> utils1=new OkHttpUtils<>(instance);
                String url2="http://101.251.196.90:8080/JztkServer/getQuestions?testType=rand";
                utils.url(url2).targetClass(String.class)
                        .execute(new OkHttpUtils.OnCompleteListener<String>() {
                            @Override
                            public void onSuccess(String jsonStr) {
                                Result result= ResultUtils.getListResultFromJson(jsonStr);
                                if(result!=null&&result.getError_code()==0){
                                    List<Question> list=result.getResult();
                                    if ((list != null && list.size() > 0)) {
                                        examinfo=list;
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("main","error="+error);
                            }
                        });
            }
        }).start();

    }

    public  ExamInfo getExamInfo(){
        return examinfo;
    }
    public void setExamInfo(ExamInfo examInfo){
        examinfo=examInfo;
    }

}
