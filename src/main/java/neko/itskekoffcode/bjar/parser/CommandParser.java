package neko.itskekoffcode.bjar.parser;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import neko.itskekoffcode.bjar.bot.Bot;
import neko.itskekoffcode.bjar.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;

public class CommandParser {
    public static void parseCommand(String str, Bot client) {
        str = str.trim();
        if (str.contains("await")) {
            if (str.contains("ms")) {
                ThreadUtils.sleep(Long.parseLong(str.substring(str.indexOf(" "), str.indexOf("ms")).trim()));
            } else if (str.contains("s")) {
                ThreadUtils.sleep(TimeUnit.SECONDS.toMillis(Long.parseLong(str.substring(str.indexOf(" "), str.indexOf("s")).trim())));
            } else if (str.contains("h")) {
                ThreadUtils.sleep(TimeUnit.HOURS.toMillis(Long.parseLong(str.substring(str.indexOf(" "), str.indexOf("h")).trim())));
            }
        } else {
            client.getSession().send(new ClientChatPacket(str));
        }

    }
}
