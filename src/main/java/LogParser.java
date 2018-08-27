import domain.Log_Analysis_Tbl;

/**
 * Created by jiangjinping on 2018/5/26.
 * 将错误日志按照字段进行格式化
 */
public class LogParser {


    public static Log_Analysis_Tbl parseLog(String logtxt) {

        String[] tmplog = logtxt.split("\\|");
        String time = tmplog[0].trim();
        long pid = Long.valueOf(tmplog[1].trim());
        long tid = Long.valueOf(tmplog[2].trim());
        String function = tmplog[3].trim();
        long line = Long.valueOf(tmplog[4].trim());
        String level = tmplog[5].trim();
        String information = tmplog[6].trim();
        System.out.println(time);
        System.out.println(pid);
        System.out.println(tid);
        System.out.println(function);
        System.out.println(line);
        System.out.println(level);
        System.out.println(information);
        return new Log_Analysis_Tbl(time, pid, tid, function, line, level, information);
    }
}
