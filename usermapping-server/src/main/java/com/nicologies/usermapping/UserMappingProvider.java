package com.nicologies.usermapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import jetbrains.buildServer.serverSide.SBuild;
import jetbrains.buildServer.serverSide.ServerPaths;
import jetbrains.buildServer.serverSide.parameters.AbstractBuildParametersProvider;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class UserMappingProvider extends AbstractBuildParametersProvider {
    private Logger LOG = Logger.getLogger(UserMappingProvider.class);
    private ServerPaths _serverPaths;

    public UserMappingProvider(ServerPaths serverPaths) {
        this._serverPaths = serverPaths;
    }

    @NotNull
    public Map<String, String> getParameters(@NotNull SBuild build, boolean emulationMode) {
        Map parameters = super.getParameters(build, emulationMode);
        File configDir = new File(this._serverPaths.getPluginDataDirectory(), "usermapping");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File configFile = new File(configDir, "usermapping.json");
        Type type = new TypeToken<List<User>>(){}.getType();
        FileReader fileReader;

        try {
            fileReader = new FileReader(configFile);
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        }

        JsonReader reader = new JsonReader(fileReader);
        try {
            Gson gson = new Gson();
            List<User> users = gson.fromJson(reader, type);
            users.forEach(u -> MapAUser(u, parameters));
        } catch(Exception e){
            e.printStackTrace();
            LOG.error(e.getMessage());
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }

        return parameters;
    }

    private void MapAUser(User user, Map parameters){
        Map<String, String> newNames = user.getNewNames();
        user.getOrgNames().forEach(orgName ->
            newNames.forEach((key, value) ->
                parameters.put(UserMappingConstants.PrefixOfUserMapping + orgName + "." + key, value)
        ));
    }
}

