package main.task4;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class EditFile {

    public static void reFile(String login, String password, String way) {
        File f = new File(way);
        if (f.delete()) {
            System.out.println("del");
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        putJSON(login, password, way);
    }

    public static JSONObject readJSON(String jsWay) {
        try (FileReader f = new FileReader(jsWay)) {
            return (JSONObject) new JSONParser().parse(f);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void putJSON(String login, String password, String way) {
        JSONObject object = new JSONObject();
        object.put("login", login);
        object.put("password", password);
        try (FileWriter file = new FileWriter(way)) {
            file.write(object.toJSONString());
            file.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
