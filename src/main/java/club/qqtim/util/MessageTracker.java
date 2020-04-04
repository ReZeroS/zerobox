package club.qqtim.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/8/19 8:32 PM
 * @Version 1.0
 */
public class MessageTracker {

    private static List<String> MESSAGES = new ArrayList<String>();

    public static void addMsg(String msg){
        MESSAGES.add(msg);
    }
    public static void clearMsgs(){
        MESSAGES.clear();
    }
    public static List<String> getMsgs(){
        return MESSAGES;
    }


}
