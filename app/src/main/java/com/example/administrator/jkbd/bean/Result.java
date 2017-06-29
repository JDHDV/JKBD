package com.example.administrator.jkbd.bean;


import java.util.List;

public class Result {

    /**
     * error_code : 0
     * reason : ok
     * result : [{"id":16,"question":"这个标志是何含义？","answer":"4","item1":"靠道路左侧停车","item2":"左侧是下坡路段","item3":"只准向左转弯","item4":"靠左侧道路行驶","explains":"靠左侧道路行驶：表示只准一切车辆靠左侧道路行驶。此标志设在车辆必须靠左侧行驶的路口以前适当位置。","url":"http://images.juheapi.com/jztk/c1c2subject1/16.jpg"}]
     */

    private int error_code;
    private String reason;
    private List<Question> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<Question> getResult() {
        return result;
    }

    public void setResult(List<Question> result) {
        this.result = result;
    }
}
