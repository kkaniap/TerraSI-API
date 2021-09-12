package com.terrasi.terrasiapi.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Service
public class NewsService {

    private final String resourcesPath = "src/main/resources/news/n";

    public String readNews(Long id) throws IOException{
        StringBuilder content = new StringBuilder();
        try(var fileReader = new FileReader(resourcesPath + id + ".txt");
            var reader = new BufferedReader(fileReader)){
            String nextLine = null;
            while ((nextLine = reader.readLine()) != null) {
                content.append(nextLine);
            }
        }catch (FileNotFoundException e){
            throw new FileNotFoundException();
        }catch(IOException e){
            throw new IOException();
        }
        return content.toString();
    }
}
