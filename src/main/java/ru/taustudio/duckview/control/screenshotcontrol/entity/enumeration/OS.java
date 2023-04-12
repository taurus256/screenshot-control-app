package ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration;

/**
 * The OS enumeration.
 */
public enum OS {
    MACOS("MAC"),
    LINUX("LIN"),
    WINDOWS("WIN"),
    IOS("IOS");

    private String shortname;

    OS(String sname){
        shortname = sname;
    }

    public String getShortname(){
        return shortname;
    };
}
