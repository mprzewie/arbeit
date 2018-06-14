package pl.edu.agh.arbeit.gui.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.time.Duration;
import java.util.List;

public class AppConfig implements ConfigProvider {

    private Info info;
    private ObjectMapper mapper = new ObjectMapper();

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
    public List<ApplicationInfo> getAppsToTrack() {
        return this.info.getAppsToTrack();
    }

    @Override
    public Duration getSystemPingTime() {
        return Duration.ofSeconds(info.getSystemPingTimeInSeconds());
    }


    @Override
    public void addAppToTrack(ApplicationInfo app) {
        File file = new File("config.json");
        try {
            info.addAppToTrack(app);
            mapper.writeValue(file, info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        File file = new File("config.json");
        try {
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

    @Override
    public Info getInfo() {
        return this.info;
    }
}
