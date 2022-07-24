package java.task3;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class JSONsort {
    public static void main(String[] args) {
        //String jsWay = "src\\main\\task3\\values.json";
        String jsWay = args[0];

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(jsWay));
            JSONArray jsonArray = ((JSONArray) jsonObject.get("values"));
            System.out.println(jsonArray);

            ArrayList<JSONObject> array = new ArrayList<>();
            for (Object o : jsonArray)
                array.add((JSONObject) o);

            array.sort(Comparator.comparingInt(o -> Integer.parseInt(o.get("id").toString())));
            System.out.println(array);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void print(JSONArray jarr) {
        int i = -1;
        while (++i < jarr.size()) {
            if (((JSONObject) jarr.get(i)).get("values") != null)
                print((JSONArray) ((JSONObject) jarr.get(i)).get("values"));
            System.out.print(((JSONObject) jarr.get(i)).get("id") + "  ");
            System.out.println(((JSONObject) jarr.get(i)).get("value"));
        }
    }
}
