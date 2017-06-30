package com.example.administrator.jkbd;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.example.administrator.jkbd.bean.ExamInfo;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.bean.Result;
import com.example.administrator.jkbd.biz.ExamBiz;
import com.example.administrator.jkbd.biz.IExamBiz;
import com.example.administrator.jkbd.util.OkHttpUtils;
import com.example.administrator.jkbd.util.ResultUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExamApplication extends Application {
    public  static String LOAD_EXAM_INFO="load_exam_info";
    public  static String LOAD_EXAM_QUESTION="load_exam_question";
    public  static String LOAD_DATA_SUCCESS="load_data_success";
    ExamInfo examinfo;
    List<Question> ExamList;
    private  static ExamApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static ExamApplication getInstance(){
        return  instance;
    }

    public  ExamInfo getExamInfo(){
        return examinfo;
    }
    public void setExamInfo(ExamInfo examInfo){
        examinfo=examInfo;
    }

    public List<Question> getExamList() {
        return ExamList;
    }

    public void setExamList(List<Question> examList) {
        ExamList = examList;
    }
}
