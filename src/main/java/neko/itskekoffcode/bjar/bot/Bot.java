package neko.itskekoffcode.bjar.bot;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import java.io.IOException;
import java.net.Proxy;
import java.util.Random;

import neko.itskekoffcode.bjar.bot.listener.SessionListener;
import neko.itskekoffcode.bjar.utils.ThreadUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class Bot {
    private static final Random random = new Random();
    private final MinecraftProtocol account;
    private final Proxy proxy;
    private Session session;
    private double posX;
    private double posY;
    private double posZ;

    public Bot(MinecraftProtocol account, Proxy proxy) {
        this.proxy = proxy;
        this.account = account;
    }

    public void connect(String ip, int port2) throws IOException {
        Client client = new Client(ip, port2, this.account, new TcpSessionFactory(this.proxy));
        client.getSession().addListener(new SessionListener(this));
        client.getSession().setConnectTimeout(6000);
        client.getSession().connect();
        this.session = client.getSession();
    }

    public void register() {
        String password = RandomStringUtils.random(14, true, true);
        ThreadUtils.sleep(500L);
        this.session.send(new ClientChatPacket(String.format("/register %s %s", password, password)));
        this.session.send(new ClientChatPacket(String.format("/login %s %s", password, password)));
    }

    public boolean isOnline() {
        return this.session != null && this.session.isConnected();
    }

    public Session getSession() {
        return this.session;
    }

    public GameProfile getGameProfile() {
        return this.account.getProfile();
    }

    public double getPosX() {
        return this.posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }
}
