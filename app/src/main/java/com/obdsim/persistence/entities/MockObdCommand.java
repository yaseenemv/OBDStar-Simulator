package com.obdsim.persistence.entities;

import android.text.TextUtils;

public class MockObdCommand {
    protected String description;
    protected String value;
    private Integer _id;
    private String cmd;
    private String response;
    private Boolean stateFlag = false;

    public MockObdCommand() {
    }

    public MockObdCommand(String cmd2, String response2) {
        this.cmd = cmd2;
        this.response = response2;
    }

    public MockObdCommand(Integer _id2, String cmd2, String response2, String desc) {
        this._id = _id2;
        this.cmd = cmd2;
        this.response = response2;
        this.description = desc;
        this.stateFlag = false;
    }

    public MockObdCommand(String cmd2, String response2, String desc, Boolean bool) {
        this.cmd = cmd2;
        this.response = response2;
        this.stateFlag = bool;
        this.description = desc;
    }

    public MockObdCommand(Integer _id2, String cmd2, String response2, Boolean stateFlag2, String description2) {
        this._id = _id2;
        this.cmd = cmd2;
        this.response = response2;
        this.stateFlag = stateFlag2;
        this.description = description2;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response2) {
        this.response = response2;
    }

    public String parseResponse() {
        return null;
    }

    public String setValue() {
        return this.value;
    }

    public String generateResponse() {
        String responseHeader = prepareCommandResponse();
        return responseHeader + putSpaces(validateZeros(Integer.toHexString(transformValue().intValue()))).toUpperCase() + ">";
    }

    public String validateZeros(String st) {
        return st;
    }

    public Boolean validateInput(String val) {
        if (TextUtils.isEmpty(val)) {
            return false;
        }
        return Boolean.valueOf(TextUtils.isDigitsOnly(val));
    }

    /* access modifiers changed from: protected */
    public Integer transformValue() {
        return null;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd2) {
        this.cmd = cmd2;
    }

    public Integer get_id() {
        return this._id;
    }

    public void set_id(Integer _id2) {
        this._id = _id2;
    }

    public Boolean getStateFlag() {
        return this.stateFlag;
    }

    public void setStateFlag(Boolean stateFlag2) {
        this.stateFlag = stateFlag2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String putSpaces(String st) {
        return st.replaceAll("..(?=..)", "$0 ");
    }

    public String prepareCommandResponse() {
        return "4" + putSpaces(this.cmd).substring(1) + " ";
    }
}
