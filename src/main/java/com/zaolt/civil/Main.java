package com.zaolt.civil;

import net.sf.geographiclib.Geodesic;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<User> resultList = new ArrayList<>();
        int distanceInMile = 50;
        City city = new City();
        city.setName("London");
        city.setLatitude(51 + (30 / 60.0) + (26 / 60.0 / 60.0));
        city.setLongitude(0 - (7 / 60.0) - (39 / 60.0 / 60.0));

        String url = "https://bpdts-test-app.herokuapp.com/city/"+city.getName()+"/users";
        resultList.addAll(parseUserObject(getData(url)));
        url= "https://bpdts-test-app.herokuapp.com/users";
        resultList.addAll(
                getUsersInsideDistance(
                        parseUserObject(
                                getData(url)),city, distanceInMile));

        resultList.forEach(user -> System.out.println(user));
    }

    public static List<Object> getData(String url) {
        return  WebClient.builder().
                build().
                get().
                uri(url).
                retrieve().bodyToMono(List.class).
                block();
    }

    public static List<User> getUsersInsideDistance(List<User> userList, City city, int distanceInMile) {
       return userList.stream().filter(user ->
                ((Geodesic.WGS84.Inverse(
                city.getLatitude(), city.getLongitude(), user.getLatitude(), user.getLongitude()).s12)/1609.34)
                        < distanceInMile).collect(Collectors.toList());
    }

    public static List<User> parseUserObject(List<Object> list) {
        List<User> userList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray((list));
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User user = new User();
            user.setId(jsonObject.getInt("id"));
            user.setFirst_name(jsonObject.getString("first_name"));
            user.setLast_name(jsonObject.getString("last_name"));
            user.setEmail(jsonObject.getString("email"));
            user.setIp_address(jsonObject.getString("ip_address"));
            user.setLatitude(jsonObject.getDouble("latitude"));
            user.setLongitude(jsonObject.getDouble("longitude"));
            userList.add(user);
        }
        return userList;
    }

}
