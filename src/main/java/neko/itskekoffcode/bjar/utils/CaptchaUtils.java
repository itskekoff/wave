package neko.itskekoffcode.bjar.utils;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Session;
import java.util.regex.Pattern;
import neko.itskekoffcode.bjar.bot.Bot;
import org.apache.commons.lang3.StringUtils;

public class CaptchaUtils {
    public static void solveCaptcha(String message, Bot client) {
        Session var10000;
        String line;
        try {
            line = FindUtils.findStringByRegex(message, Pattern.compile("\\..* /verify .*")).replace("\"", " ");
            if (StringUtils.containsIgnoreCase(message, "clickEvent")) {
                var10000 = client.getSession();
                String var10003 = line.split(" ")[0];
                var10000.send(new ClientChatPacket(var10003 + " /verify " + line.split(" ")[2]));
                client.register();
            }
        } catch (Exception var7) {
        }

        String[] var8;
        try {
            line = FindUtils.findStringByRegex(message, Pattern.compile("/captcha .*")).replace("»", " ");
            var10000 = client.getSession();
            var8 = line.split(" ");
            var10000.send(new ClientChatPacket("/captcha " + var8[1]));
            client.register();
        } catch (Exception var6) {
        }

        try {
            line = FindUtils.findStringByRegex(message, Pattern.compile("Type .*"));
            if (StringUtils.containsIgnoreCase(message, "prove")) {
                client.getSession().send(new ClientChatPacket(line.split(" ")[1]));
                client.register();
            }
        } catch (Exception var5) {
        }

        try {
            line = FindUtils.findStringByRegex(message, Pattern.compile("following code: .*"));
            client.getSession().send(new ClientChatPacket(line.split(" ")[2]));
            client.register();
        } catch (Exception var4) {
        }

        try {
            line = FindUtils.findStringByRegex(message, Pattern.compile("clique na cor .*"));
            var10000 = client.getSession();
            var8 = line.split(" ");
            var10000.send(new ClientChatPacket("/color " + var8[3]));
            client.register();
        } catch (Exception var3) {
        }

    }
}
