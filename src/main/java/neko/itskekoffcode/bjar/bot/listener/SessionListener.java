package neko.itskekoffcode.bjar.bot.listener;

import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import neko.itskekoffcode.bjar.Main;
import neko.itskekoffcode.bjar.bot.Bot;
import neko.itskekoffcode.bjar.parser.ProxyParser;
import neko.itskekoffcode.bjar.utils.CaptchaUtils;
import neko.itskekoffcode.bjar.utils.ThreadUtils;
import org.apache.commons.lang3.StringUtils;


public class SessionListener extends SessionAdapter {
    public final Bot client;
    public static Bot clientt;
    private ProxyParser proxyParser = new ProxyParser();
    public static FileWriter logsw;
    public static int number = 1000;
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    String strDate;

    public SessionListener(Bot client) throws IOException {
        this.strDate = this.dateFormat.format(this.date);
        this.client = client;
        this.proxyParser = new ProxyParser();
        (new Thread(() -> {
            ThreadUtils.sleep(10000L);
            double posX = -0.2D;

            for(double posZ = -0.2D; client.isOnline(); posZ = -posZ) {
                int i;
                for(i = 0; i < Main.getInstance().getRandom().nextInt(200); ++i) {
                    client.getSession().send(new ClientPlayerPositionPacket(true, client.getPosX() + posX, client.getPosY(), client.getPosZ()));
                    client.setPosX(client.getPosX() + posX);
                    ThreadUtils.sleep(50L);
                }

                for(i = 0; i < Main.getInstance().getRandom().nextInt(200); ++i) {
                    client.getSession().send(new ClientPlayerPositionPacket(true, client.getPosX(), client.getPosY(), client.getPosZ() + posZ));
                    client.setPosZ(client.getPosZ() + posZ);
                    ThreadUtils.sleep(50L);
                }

                posX = -posX;
            }

        })).start();
    }

    public void packetReceived(PacketReceivedEvent receiveEvent) {
        if (receiveEvent.getPacket() instanceof ServerJoinGamePacket) {
            this.client.register();
            this.client.getSession().send(new ClientChatPacket(Main.message));
        } else if (receiveEvent.getPacket() instanceof ServerChatPacket) {
            ServerChatPacket packet = (ServerChatPacket)receiveEvent.getPacket();
            String message = packet.getMessage().getFullText();
            String ip;
            String var9;
            if (message.contains("Ожидайте завершения проверки...")) {
                try {
                    var9 = this.client.getSession().getHost();
                    ip = var9 + ":" + this.client.getSession().getPort();
                    System.out.println("Удаление сервера " + ip + ". У него ботфильтр.");
                    logsw.write("\n" + this.strDate + " Удалён сервер " + ip + " || По причине: БотФильтер\n");
                    logsw.flush();
                    Main.getInstance().getServerParser().getServers().remove(ip);
                    this.client.getSession().disconnect("bf");
                    return;
                } catch (Exception var7) {
                    var7.printStackTrace();
                }
            }

            if (message.contains("c2f3g1h9")) {
                try {
                    var9 = this.client.getSession().getHost();
                    ip = var9 + ":" + this.client.getSession().getPort();
                    System.out.println("Удаление сервера " + ip + "\n");
                    logsw.write("\n" + this.strDate + " Удалён сервер " + ip + " || По причине: удалён\n");
                    logsw.flush();
                    Main.getInstance().getServerParser().getServers().remove(ip);
                    this.client.getSession().disconnect("bf");
                    return;
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            if (!StringUtils.containsIgnoreCase(message, "chat.type")) {
                try {
                    PrintStream var10000 = System.out;
                    String var10001 = this.client.getSession().getHost();
                    var10000.print("SRV: " + var10001 + ":" + this.client.getSession().getPort() + " || Никнейм >> " + this.client.getGameProfile().getName() + " || Сообщение >> " + message + "\n");
                    TimeUnit.SECONDS.sleep(Long.parseLong("1"));
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }

            CaptchaUtils.solveCaptcha(message, this.client);
        } else if (receiveEvent.getPacket() instanceof ServerPlayerPositionRotationPacket) {
            ServerPlayerPositionRotationPacket packet = (ServerPlayerPositionRotationPacket)receiveEvent.getPacket();
            this.client.setPosX(packet.getX());
            this.client.setPosY(packet.getY());
            this.client.setPosZ(packet.getZ());
            this.client.getSession().send(new ClientTeleportConfirmPacket(packet.getTeleportId()));
        } else if (receiveEvent.getPacket() instanceof ServerPlayerHealthPacket && ((ServerPlayerHealthPacket)receiveEvent.getPacket()).getHealth() < 1.0F) {
            this.client.getSession().send(new ClientRequestPacket(ClientRequest.RESPAWN));
        }

    }

    static {
        try {
            logsw = new FileWriter("latest.txt", true);
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}

