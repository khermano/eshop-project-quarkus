package cz.muni.fi.utils;

public class MyMessageParser {
    public int parseMessage(String message) {
        String[] result = message.split("status code ");
        return Integer.parseInt(result[1].substring(0,3));
    }
}
