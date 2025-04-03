package ec.edu.uees.akinator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author judit
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.*;

public class WikipediaImageFetcher {

    public String getWikipediaImageUrl(String title) {
        try {
            title = formatTitle(title);

            // 1️. Buscar el nombre real del artículo en Wikipedia
            String urlStr = "https://es.wikipedia.org/w/api.php?action=query&titles=" + title
                    + "&redirects=1&format=json";

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parsear JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject pages = jsonResponse.getJSONObject("query").getJSONObject("pages");

            String firstPageId = pages.keys().next();
            JSONObject page = pages.getJSONObject(firstPageId);

            if (page.has("title")) {
                title = formatTitle(page.getString("title"));  // 🔹 Usar título corregido
                System.out.println("Título corregido: " + title);
            }

            //2. Buscar la imagen del título corregido
            urlStr = "https://es.wikipedia.org/w/api.php?action=query&titles=" + title
                    + "&prop=pageimages&format=json&pithumbsize=500";

            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parsear JSON para obtener la imagen
            jsonResponse = new JSONObject(response.toString());
            pages = jsonResponse.getJSONObject("query").getJSONObject("pages");
            firstPageId = pages.keys().next();
            page = pages.getJSONObject(firstPageId);

            if (page.has("thumbnail")) {
                String imageUrl = page.getJSONObject("thumbnail").getString("source");
                System.out.println("Imagen encontrada: " + imageUrl);
                return imageUrl;
            } else {
                System.out.println("No se encontró imagen para: " + title);
                return "No se encontró una imagen.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener la imagen.";
        }
    }

    public String formatTitle(String title) {

        title = title.trim();

        title = title.replace(" ", "_");

        return URLEncoder.encode(title, StandardCharsets.UTF_8);
    }

}
