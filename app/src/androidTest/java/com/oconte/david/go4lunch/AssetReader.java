package com.oconte.david.go4lunch;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AssetReader {

    private static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder total = new StringBuilder();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    public static String getAsset(Context context, String path) {
        try {
            InputStream inputStream = context.getAssets().open("network_files/" + path);
            return inputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
