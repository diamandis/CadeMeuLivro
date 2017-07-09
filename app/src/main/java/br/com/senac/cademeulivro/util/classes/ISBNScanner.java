package br.com.senac.cademeulivro.util.classes;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.senac.cademeulivro.R;
import br.com.senac.cademeulivro.model.Obra;

/**
 * Created by joaos on 22/05/2017.
 * REFATORADO by mariana 08/07/2017
 */

public class ISBNScanner {
    private Activity activityForToast;
    private Obra obra;
    private String ISBN;

    public ISBNScanner(Activity activityForToast) {
        this.activityForToast = activityForToast;
    }

    public byte[] getUrlBytes(String caminho) throws IOException {
        URL url = new URL(caminho);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage()+": em " +caminho);
            }

            int bytesLidos = 0;
            byte[] buffer = new byte[1024];
            while((bytesLidos = in.read(buffer)) < 0) {
                out.write(buffer,0, bytesLidos);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String caminho) throws IOException {
        return new String(getUrlBytes(caminho));
    }

    public List<Obra> fetch(String param) {
        this.ISBN = param;
        List<Obra> items = new ArrayList<>();
        try {
            String url = Uri.parse("https://www.googleapis.com/books/v1/volumes")
                    .buildUpon()
                    .appendQueryParameter("q","isbn:"+ISBN)
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject json = new JSONObject(jsonString);
            parseItems(items,json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
        return items;
    }

    private void parseItems(List<Obra> items, JSONObject json) throws IOException, JSONException{
        if(json.getInt("totalItems")==0) {
            Toast.makeText(activityForToast, R.string.codigo_invalido, Toast.LENGTH_SHORT).show();
            return;
        }
        JSONArray bookArray = json.getJSONArray("items");
        StringBuilder authorBuild = new StringBuilder("");

        //preenchendo a lista e verificando a existência dos campos
        for (int j=0; j<bookArray.length();j++) {
            obra = new Obra();

            JSONObject bookObject = bookArray.getJSONObject(j);
            JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");

            //pegando capa do livro
            if(!volumeObject.isNull("imageLinks")){
                JSONObject imageInfo = volumeObject.getJSONObject("imageLinks");
                obra.setCapaUrl(imageInfo.getString("smallThumbnail"));
            }

            if(!volumeObject.isNull("authors")){
                JSONArray authorArray = volumeObject.getJSONArray("authors");
                if(authorArray !=null) {
                    for (int a = 0; a < authorArray.length(); a++) {
                        if (a > 0) authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                }
                obra.setAutor(authorBuild.toString());
            }

            obra.setTitulo((!volumeObject.isNull("title")) ? volumeObject.getString("title") : null);
            obra.setEditora((!volumeObject.isNull("publisher") ? volumeObject.getString("publisher") : null));
            obra.setDescricao(!volumeObject.isNull("description") ? volumeObject.getString("description") : null);
            obra.setIsbn(ISBN);

            //capturando a data e pegando apenas o ano
            if(!volumeObject.isNull("publishedDate")){
                String[] parts = volumeObject.getString("publishedDate").split("-");
                obra.setAnoPublicacao(Integer.parseInt(parts[0]));
            }

            items.add(obra);
        }

    }
}
