package com.example.administrator.jkbd.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.jkbd.ExamApplication;
import com.example.administrator.jkbd.R;
import com.example.administrator.jkbd.bean.Question;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class QuestionAdapter extends BaseAdapter{
    Context context;
    List<Question> examList;

    public QuestionAdapter(Context context) {
        this.context = context;
        examList= ExamApplication.getInstance().getExamList();
    }

    @Override
    public int getCount() {
        return examList==null?0:examList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view= View.inflate(context, R.layout.item_que,null);
        TextView tvnum= (TextView) view.findViewById(R.id.tv_no);
        ImageView que= (ImageView) view.findViewById(R.id.img_img);
        String userAnswer=examList.get(position).getUserAnswer();
        String answer=examList.get(position).getAnswer();
        if (userAnswer!=null&&!userAnswer.equals("")){

            que.setImageResource(userAnswer.equals(answer)?R.mipmap.answer24x24:R.mipmap.error);
        }else {
            que.setImageResource(R.mipmap.ques24x24);
        }
        tvnum.setText("第"+(position+1)+"题");
        return view;
    }
}
