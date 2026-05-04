package com.githubx.usersms.util.errorhandling;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class StackTraceUtil {

    private StackTraceUtil() {
    }

    public static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
