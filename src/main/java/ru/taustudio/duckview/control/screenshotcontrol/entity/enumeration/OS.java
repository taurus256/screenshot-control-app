package ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration;

/**
 * The OS enumeration.
 */
public enum OS {
    MACOS("mac"),
    LINUX("lin"),
    WINDOWS("win"),
    IOS("ios");

    private String shortname;

    OS(String sname){
        shortname = sname;
    }

    public String getShortname(){
        return shortname;
    };
}
