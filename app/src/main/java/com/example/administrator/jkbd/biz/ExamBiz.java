package com.example.administrator.jkbd.biz;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.bean.Question;
import com.example.administrator.jkbd.dao.ExamDao;
import com.example.administrator.jkbd.dao.IExamDao;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public class ExamBiz implements IExamBiz{
     IExamDao dao;
    int examIndex=0;
    List<Question> examList=null;

    public ExamBiz() {
        this.dao = new ExamDao();
    }

    @Override
    public void beginExam() {
        examIndex=0;
        dao.loadExamInfo();
        dao.loadQuestionLists();
    }

    @Override
    public Question getExam() {
        examList=ExamApplication.getInstance().getExamList();
        if(examList!=null) {
            return examList.get(examIndex);
        }else {
            return  null;
        }
    }

    @Override
    public Question nextQuestion() {
        if(examList!=null&&examIndex<examList.size()-1){
            examIndex++;
            return examList.get(examIndex);
        }else {
            return  null;
        }
    }

    @Override
    public Question preQuestion() {
        if(examList!=null&&examIndex>0){
            examIndex--;
            return examList.get(examIndex);
        }else {
            return  null;
        }
    }

    @Override
    public int commitExam() {
        int result=0;
        for(Question que:examList){
            String userAnswer=que.getUserAnswer();
            if(userAnswer!=null&&!userAnswer.equals("")){
                if(que.getAnswer().equals(userAnswer)){
                    result++;
                }
            }
        }
        return  result;
    }

    @Override
    public String getExamIndex() {
        return (examIndex+1)+".";
    }
}
