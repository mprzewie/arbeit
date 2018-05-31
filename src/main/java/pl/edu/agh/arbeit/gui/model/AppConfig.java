package pl.edu.agh.arbeit.gui.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class AppConfig implements ConfigProvider {

    private Info info;
    private ObjectMapper mapper;

    public AppConfig() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        InputStream is;
        TypeReference<Info> mapType = new TypeReference<Info>() {
        };
        try {
            is = new FileInputStream(new File("config.json"));
            info = mapper.readValue(is, mapType);

        }catch (FileNotFoundException e){
            info=new Info();
            File file = new File("config.json");
            try {
                mapper.writeValue(file, info);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public List<AppInfo> getAppsToTrack() {
        return this.info.getAppsToTrack();
    }

    @Override
    public Duration getSystemPingTime() {
        return Duration.ofSeconds(info.getSystemPingTimeInSeconds());
    }


    @Override
    public void addAppToTrack(AppInfo appInfo) {
        File file = new File("config.json");
        try {
            info.addAppToTrack(appInfo);
            mapper.writeValue(file, info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAppToTrack(String programName) {
        File file = new File("config.json");
        try {
            info.removeAppToTrack(programName);
            mapper.writeValue(file, info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setSystemPingTime(Duration pingTime) {
        this.info.setSystemPingTimeInSeconds(pingTime.getSeconds());
    }

    @Override
    public Duration getTimeToBecomePassive() {
        return Duration.ofSeconds(info.getTimeToBecomePassiveInSeconds());
    }

    @Override
    public void setTimeToBecomePassive(Duration timeToBecomePassive) {
        this.info.setTimeToBecomePassiveInSeconds(timeToBecomePassive.getSeconds());
    }
}
