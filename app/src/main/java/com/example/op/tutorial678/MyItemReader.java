/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.op.tutorial678;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.op.tutorial678.Model.Person;
import com.google.android.gms.maps.model.LatLng;


public class MyItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";
    private ArrayList<Integer> listAvatars = new ArrayList<>();
    private  Random random = new Random();

    public List<Person> read(InputStream inputStream) throws JSONException {
        List<Person> items = new ArrayList<Person>();
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);

        listAvatars.add(R.drawable.avatar);
        listAvatars.add(R.drawable.avatar2);
        listAvatars.add(R.drawable.avatar3);
        listAvatars.add(R.drawable.bighero6);
        listAvatars.add(R.drawable.ff8);
        listAvatars.add(R.drawable.avatar_film);

        for (int i = 0; i < array.length(); i++) {
            String name = null;
            String email = null;
            String phoneNumber = null;

            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            //pictureResource =
            if (!object.isNull("name")) {
                name = object.getString("name");
            }
            if (!object.isNull("email")) {
                email = object.getString("email");
            }

            if (!object.isNull("phoneNumber")) {
                phoneNumber = object.getString("phoneNumber");
            }

            int idx = random.nextInt(listAvatars.size());
            items.add(new Person(new LatLng(lat, lng), name, email, phoneNumber, listAvatars.get(idx)));
        }
        return items;
    }
}
