package com.example.url_shortner.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UrlUtil {

    Map<Integer,Character> characterMap=new HashMap<>();

    public void createBinaryCharacterMap(){
        int keyCount =0;

        for(char c='a';c<='z';c++){
            characterMap.put(keyCount++,c);
        }

        for(char c='A';c<='Z';c++){
            characterMap.put(keyCount++,c);
        }

        for(char c='0';c<='9';c++){
            characterMap.put(keyCount++,c);
        }
    }


    public String IDToURLGenerator(long id) {
        if(characterMap.isEmpty()) createBinaryCharacterMap();
        StringBuilder shortUrl = new StringBuilder();
        while (id > 0) {
            // 1. Get the current 6-bit chunk (0-63)
            int index = (int) (id & 63);
            // 2. Map it to a character
            shortUrl.append(characterMap.get(index));
            id >>= 6;
        }
        return shortUrl.reverse().toString();
    }
}
