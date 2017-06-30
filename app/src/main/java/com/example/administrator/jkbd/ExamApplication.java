package com.example.administrator.jkbd;

import android.app.Application;
import android.util.Log;

import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.util.OkHttpUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExamApplication extends Application {
    ExamInfo examinfo;
    List<ExamInfo> ExamList;
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
        OkHttpUtils<ExamInfo> utils=new OkHttpUtils<>(instance);
        String uri="http://101.251.196.90:8080/JztkServer/examInfo";
        utils.url(uri).targetClass(ExamInfo.class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamInfo>() {
                    @Override
                    public void onSuccess(ExamInfo result) {
                        Log.e("main","result="+result);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main","error="+error);
                    }
                });
    }

    public  ExamInfo getExamInfo(){
        return examinfo;
    }
    public void setExamInfo(ExamInfo examInfo){
        examinfo=examInfo;
    }
}
