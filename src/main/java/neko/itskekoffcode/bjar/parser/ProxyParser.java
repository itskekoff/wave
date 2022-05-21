package neko.itskekoffcode.bjar.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import neko.itskekoffcode.bjar.Main;
import neko.itskekoffcode.bjar.utils.ThreadUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProxyParser {
    private List<Proxy> proxies = new CopyOnWriteArrayList();
    private int number = -1;

    public static int random(int size) {
        return size;
    }

    public void init() {
        try {
            File proxyFile = new File("./proxys.txt");
            if (proxyFile.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(proxyFile));

                    try {
                        while(reader.ready()) {
                            try {
                                String line = reader.readLine();
                                this.proxies.add(new Proxy(Type.SOCKS, new InetSocketAddress(line.split(":")[0], Integer.parseInt(line.split(":")[1]))));
                            } catch (Exception var11) {
                            }
                        }
                    } catch (Throwable var16) {
                        try {
                            reader.close();
                        } catch (Throwable var10) {
                            var16.addSuppressed(var10);
                        }

                        throw var16;
                    }

                    reader.close();
                } catch (Exception var17) {
                }

                ThreadUtils.sleep(3000L);
                return;
            }

            Document proxyList;
            try {
                proxyList = Jsoup.connect("https://api.proxyscrape.com/?request=displayproxies&proxytype=socks4").get();
                this.proxies.addAll((Collection)Arrays.stream(proxyList.text().split(" ")).distinct().map((proxyx) -> {
                    return new Proxy(Type.SOCKS, new InetSocketAddress(proxyx.split(":")[0], Integer.parseInt(proxyx.split(":")[1])));
                }).collect(Collectors.toList()));
            } catch (Throwable var15) {
            }

            try {
                proxyList = Jsoup.connect("https://www.proxy-list.download/api/v1/get?type=socks4").get();
                this.proxies.addAll((Collection)Arrays.stream(proxyList.text().split(" ")).distinct().map((proxyx) -> {
                    return new Proxy(Type.SOCKS, new InetSocketAddress(proxyx.split(":")[0], Integer.parseInt(proxyx.split(":")[1])));
                }).collect(Collectors.toList()));
            } catch (Throwable var14) {
            }

            try {
                proxyList = Jsoup.connect("https://openproxylist.xyz/socks4.txt").get();
                this.proxies.addAll((Collection)Arrays.stream(proxyList.text().split(" ")).distinct().map((proxyx) -> {
                    return new Proxy(Type.SOCKS, new InetSocketAddress(proxyx.split(":")[0], Integer.parseInt(proxyx.split(":")[1])));
                }).collect(Collectors.toList()));
            } catch (Throwable var13) {
            }

            try {
                for(int k = 64; k < 1600; k += 64) {
                    Document proxyList3 = Jsoup.connect("https://hidemy.name/ru/proxy-list/?type=4&start=" + k + "#list").get();

                    for(int i = 1; i < proxyList3.getElementsByTag("tr").size(); ++i) {
                        try {
                            Elements elements = ((Element)proxyList3.getElementsByTag("tr").get(i)).getElementsByTag("td");
                            String host = ((Element)elements.get(0)).text();
                            int port = Integer.parseInt(((Element)elements.get(1)).text());
                            this.proxies.add(new Proxy(Type.SOCKS, new InetSocketAddress(host, port)));
                        } catch (Throwable var12) {
                        }
                    }
                }
            } catch (Throwable var19) {
            }

            this.proxies = new CopyOnWriteArrayList(new HashSet(this.proxies));
            Collections.shuffle(this.proxies, Main.getInstance().getRandom());
            proxyFile.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(proxyFile));

            try {
                Iterator var21 = this.proxies.iterator();

                while(var21.hasNext()) {
                    Proxy proxy = (Proxy)var21.next();
                    String[] var10001 = proxy.toString().split("/");
                    writer.write(var10001[1] + "\n");
                }
            } catch (Throwable var18) {
                try {
                    writer.close();
                } catch (Throwable var9) {
                    var18.addSuppressed(var9);
                }

                throw var18;
            }

            writer.close();
        } catch (Exception var20) {
        }

        ThreadUtils.sleep(3000L);
    }

    public Proxy nextProxy() {
        ++this.number;
        if (this.number >= this.proxies.size()) {
            this.number = 0;
        }

        return (Proxy)this.proxies.get(this.number);
    }
}
