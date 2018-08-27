package domain;

import java.io.Serializable;
/**
 * Created by jiangjinping on 2018/5/18.
 * 创建日志分析的表名该表总共8个字段
 * 时间，
 * pid,
 * cid，
 * 功能,
 * 行号
 * 日志级别
 * 日志的输出信息
 */
public class Log_Analysis_Tbl implements Serializable {
    private static final long serialVersionUID = 6972814572157146958L;
    private String time;
    private long pid;
    private long tid;
    private String function;
    private long line;
    private String level;
    private String information;

    //初始化日志信息表
    public Log_Analysis_Tbl(String time, long pid, long tid,
                            String function, long line, String level, String information) {
        this.time = time;
        this.pid = pid;
        this.tid = tid;
        this.function = function;
        this.line = line;
        this.level = level;
        this.information = information;

    }

    //获取时间
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    //获取进程pid
    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    //获取进程cid
    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    //获取function
    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    //获取行号
    public long getLine() {
        return line;
    }

    public void setLine(long line) {
        this.line = line;
    }


    //获取日志级别
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    //获取日志信息
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

}
