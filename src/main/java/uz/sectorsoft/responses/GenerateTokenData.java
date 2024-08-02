package uz.sectorsoft.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenerateTokenData {

    @JsonProperty("pfxFilePath")
    private String pfxFilePath;

    @JsonProperty("password")
    private String password;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("data")
    private String data;

    @JsonProperty("attached")
    private boolean attached;

    // Standard getters and setters

    public String getPfxFilePath() {
        return pfxFilePath;
    }

    public void setPfxFilePath(String pfxFilePath) {
        this.pfxFilePath = pfxFilePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean getAttached() {
        return attached;
    }

    public void setAttached(boolean attached) {
        this.attached = attached;
    }
}


