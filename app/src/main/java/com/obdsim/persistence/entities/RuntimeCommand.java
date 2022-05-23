package com.obdsim.persistence.entities;

public class RuntimeCommand extends RPMCommand {
    Long lValue = 0L;

    public RuntimeCommand() {
        super("011F", "41 1F 10 14>", "Tiempo en marcha del motor", true);
    }

    public RuntimeCommand(String cmd, String response, String desc, Boolean isState) {
        super(cmd, response, desc, isState);
    }

    public String parseResponse() {
        return this.value + " s (" + formatSeconds() + ")";
    }

    public String setValue() {
        String res = getResponse().replaceAll("\\s", "");
        if (res.length() < 8) {
            this.value = "-1";
            return this.value;
        }
        Long val = Long.valueOf(Long.parseLong(res.substring(4, 8), 16));
        this.lValue = val;
        this.value = val.toString();
        return this.value;
    }

    public String validateZeros(String stValue) {
        Integer len = Integer.valueOf(stValue.length());
        if (len.intValue() == 3) {
            return "0" + stValue;
        }
        if (len.intValue() == 2) {
            return "00" + stValue;
        }
        if (len.intValue() == 1) {
            return "000" + stValue;
        }
        return stValue;
    }

    public Integer transformValue() {
        return Integer.valueOf(this.value);
    }

    public String formatSeconds() {
        this.lValue = Long.valueOf(Long.parseLong(this.value));
        Long h = Long.valueOf(this.lValue.longValue() / 3600);
        Long m = Long.valueOf((this.lValue.longValue() % 3600) / 60);
        Long s = Long.valueOf(this.lValue.longValue() % 60);
        String hour = addCero(h.toString());
        String minutes = addCero(m.toString());
        return hour + ":" + minutes + ":" + addCero(s.toString());
    }

    public String addCero(String st) {
        return st.length() == 1 ? "0" + st : st;
    }
}
