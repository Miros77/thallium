package thallium.fabric;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.minecraft.client.MinecraftClient;

public class ThalliumUpdateCheck {

    private static boolean outdated;
    private static boolean checked;

    public static boolean check(ThalliumMod instance) {
        if (checked) return outdated;
        HttpURLConnection httpurlconnection = null;

        try {
            ThalliumMod.LOGGER.info("Checking for new version");
            URL url = new URL("https://addons-ecs.forgesvc.net/api/v2/addon/search?gameId=432&sectionId=6&searchFilter=thallium"); // TODO Find better Curse API URL
            httpurlconnection = (HttpURLConnection)url.openConnection();

            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();

            try {
                InputStream inputstream = httpurlconnection.getInputStream();
                String s = toString(inputstream);
                inputstream.close();
                String str = (s = s.substring(s.indexOf("393938"))).substring(s.indexOf("latestFiles")+11, s.indexOf("gameName"));
                String displayName = (displayName = str.substring(str.indexOf("displayName\":"))).substring(14, displayName.indexOf("\","));

                String cur = toString(instance.getClass().getClassLoader().getResourceAsStream("thallium_version.txt"));
                outdated = isOutdated(cur, displayName);
                checked = true;
            } finally {
                if (httpurlconnection != null) httpurlconnection.disconnect();
            }
        }  catch (Exception exception) {
            ThalliumMod.LOGGER.info(exception.getClass().getName() + ": " + exception.getMessage());
        }
        return outdated;
    }

    private static boolean isOutdated(String current, String latest) {
        if (current.equals("${version}")) return false; // Development build
        String latestVer = latest.substring(0, latest.indexOf("(")).replace("Thallium","").trim();
        String mcVersion = latest.substring(latest.indexOf(latestVer) + latestVer.length() + 6).replace(")", "");

        return !(latestVer.equalsIgnoreCase(current) && mcVersion.equalsIgnoreCase(MinecraftClient.getInstance().getGame().getVersion().getName()));
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))){
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
                stringBuilder.append(inputLine);
            return stringBuilder.toString();
        }
    }

}