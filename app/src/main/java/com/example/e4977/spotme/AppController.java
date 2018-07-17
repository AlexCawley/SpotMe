package com.example.e4977.spotme;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application
{
    /*--------------------------------------------------------------------------------------------*
     *  Constants                                                                                 *
     *--------------------------------------------------------------------------------------------*/
    public static final String TAG = AppController.class.getSimpleName();

    /*--------------------------------------------------------------------------------------------*
     *  Member variables                                                                          *
     *--------------------------------------------------------------------------------------------*/
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private static  User user;

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        super.onCreate();
        mInstance = this;

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public static synchronized AppController getInstance()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        // Log method exit
        methodLogger.end();

        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        // Log method exit
        methodLogger.end();

        return mRequestQueue;
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  addToRequestQueue                                                                         *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  adds the given request to the request queue on the main thread                            *
     *--------------------------------------------------------------------------------------------*/
    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  addToRequestQueue                                                                         *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  adds the given request to the request queue on the main thread                            *
     *--------------------------------------------------------------------------------------------*/
    public <T> void addToRequestQueue(Request<T> req)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        req.setTag(TAG);
        getRequestQueue().add(req);

        // Log method exit
        methodLogger.end();
    }

    /*--------------------------------------------------------------------------------------------*
     *                                                                                            *
     *  cancelPendingRequests                                                                     *
     *                                                                                            *
     *--------------------------------------------------------------------------------------------*
     *  cancels all request in the queue of the passed tag                                        *
     *--------------------------------------------------------------------------------------------*/
    public void cancelPendingRequests(Object tag)
    {
        // Log method entry
        MethodLogger methodLogger = new MethodLogger();

        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }

        // Log method exit
        methodLogger.end();
    }
}
