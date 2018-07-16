package com.example.e4977.spotme;

import android.util.Log;

public class MethodLogger
{
    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private String tag;
    private String methodName;
    private long startTime;

    /*--------------------------------------------------------------------------------------------*
     *  Constructor                                                                               *
     *--------------------------------------------------------------------------------------------*/
    public MethodLogger()
    {
        /*----------------------------------------------------------------------------------------*
         *  If the app is in debug mode                                                           *
         *----------------------------------------------------------------------------------------*/
        if (AppConfig.DEUBUG_MODE)
        {
            // Gets the current stack trace
            StackTraceElement callersStackTraceElement = Thread.currentThread().getStackTrace()[3];

            // Gets the class name and sets it as the tag
            tag = callersStackTraceElement.getClassName();

            // Gets the method name that calls the constructor
            methodName = callersStackTraceElement.getMethodName() + "()";

            // Stores the time that the method is called
            startTime = System.currentTimeMillis();

            // Logs the entry of the method
            Log.d(tag, "Method Entry: " + this.methodName);
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  e                                                                                         *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Log an error                                                                              *
     *--------------------------------------------------------------------------------------------*/
    public void e(String message)
    {
        Log.e(tag, methodName + " " + message);
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  i                                                                                         *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Log a message                                                                             *
     *--------------------------------------------------------------------------------------------*/
    public void i(String message)
    {
        Log.i(tag, methodName + " " + message);
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  d                                                                                         *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Log a debug message                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public void d(String message)
    {
        /*----------------------------------------------------------------------------------------*
         *  If the app is in debug mode                                                           *
         *----------------------------------------------------------------------------------------*/
        if (BuildConfig.DEBUG)
        {
            Log.d(tag, methodName + " " + message);
        }
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  end                                                                                       *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  Log a method exit                                                                         *
     *--------------------------------------------------------------------------------------------*/
    public void end()
    {
        /*----------------------------------------------------------------------------------------*
         *  If the app is in debug mode                                                           *
         *----------------------------------------------------------------------------------------*/
        if (BuildConfig.DEBUG)
        {
            // Get the total time the method took to execute
            long elapsedMillis = System.currentTimeMillis() - startTime;

            // Log the method exit with the time that it took
            Log.d(tag, String.format("%s finished in %.3f seconds.", methodName, 0.001f * elapsedMillis));
        }
    }

}
