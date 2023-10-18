package com.epam.training.arturs_ziemelis.infrastructure;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TakeScreenshotHelper {

    private static final Logger logger = Logger.getLogger(TakeScreenshotHelper.class.getName());

    public static void takeScreenshot(String testName) {
        File dir = new File(System.getProperty("user.dir") + "/screenshots/");
        if (!dir.exists()) {
            boolean wasDirectoryCreated = dir.mkdir();
            if (!wasDirectoryCreated) {
                logger.log(Level.SEVERE, "Failed to create directory for screenshots.");
                return;
            }
        }
        File scrFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + ".png";
        try {
            FileUtils.copyFile(scrFile, new File(path));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while taking screenshot: " + e.getMessage(), e);
        }
    }
}
