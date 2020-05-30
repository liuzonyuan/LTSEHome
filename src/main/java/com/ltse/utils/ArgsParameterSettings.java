package com.ltse.utils;

import com.beust.jcommander.Parameter;

public class ArgsParameterSettings {
    @Parameter(names = "-o", description = "Output Folder", required = false)
    private String outputFolder;

    @Parameter(names = "-m", description = "Window Mode", required = false)
    private String windowMode;

    public String getOutputFolder() {
        return outputFolder;
    }

    public String getWindowMode() {
        return windowMode == null ? "rolling" : windowMode;
    }
}
