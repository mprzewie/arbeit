package pl.edu.agh.arbeit.gui.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.List;

public class AppConfig implements ConfigProvider{

    private Info info;
    private ObjectMapper mapper = new ObjectMapper();

    public AppConfig() {
        InputStream is;
        TypeReference<Info> mapType = new TypeReference<Info>() {};
        try {
            is = new FileInputStream(new File("config.json"));
            info = mapper.readValue(is, mapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AppInfo> getAppsToTrack() {
        return this.info.getAppsToTrack();
    }

    @Override
    public Long getSystemPingTime() {
        return info.getSystemPingTime();
    }


    @Override
    public void addAppToTrack(AppInfo appInfo) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("config.json");
        try {
            info.addAppToTrack(appInfo);
            mapper.writeValue(file,info);
            mapper.writeValue(System.out,info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppToTrack(String programName) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        File file = new File("config.json");
        try {
            info.removeAppToTrack(programName);
            mapper.writeValue(file,info);
            mapper.writeValue(System.out,info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSystemPingTime(Long pingTime) {
        this.info.setSystemPingTime(pingTime);
    }
}
