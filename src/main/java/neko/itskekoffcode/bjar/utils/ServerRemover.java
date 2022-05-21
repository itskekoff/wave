package neko.itskekoffcode.bjar.utils;

import neko.itskekoffcode.bjar.parser.ProxyParser;
import neko.itskekoffcode.bjar.parser.ServerParser;

import java.util.Scanner;

public class ServerRemover {
    private static final ServerParser serverParser = new ServerParser();
    private static final ProxyParser proxyParser = new ProxyParser();

    public static void init() {
        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }
}
