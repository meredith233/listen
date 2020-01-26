package com.example.listen.common;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class Converter {

    public static <T> List<T> jsonStringConvertToList(String string, Class<T[]> cls) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(string, cls);
        return Arrays.asList(array);
    }
}
