package main.task3;

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
        String jsWay = "src\\main\\task3\\values.json";

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



       /* System.out.println(array.get(3).get("id").toString());
        System.out.println(array.get(4).get("id").toString());
        System.out.println(array.get(3).get("id").toString().compareTo(array.get(4).get("id").toString()));//.getClass().getName());

        /*Collections.sort(array, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return (o1.get("value").toString().compareTo(o2.get("value").toString()));
            }
        });*/

     /*   array.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return ((o1.get("id").toString()).compareTo(o2.get("id").toString()));
            }
        });*/
        /*array.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                return Integer.compare(Integer.parseInt(o1.get("id").toString())
                        ,Integer.parseInt(o2.get("id").toString()));
            }
        });*/


    }

    public static JSONArray values(String name) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(name));
            return ((JSONArray) jsonObject.get("values"));
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
//    public static   sort

  /*  public static JSONArray values2(String way) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(way)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

            String name = (String) jsonObject.get("name");
            System.out.println(name);

            long age = (Long) jsonObject.get("age");
            System.out.println(age);

            // loop array
            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator<String> iterator = msg.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/
}
