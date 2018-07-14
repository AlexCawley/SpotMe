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

    /*--------------------------------------------------------------------------------------------*
     *  onCreate                                                                                  *
     *--------------------------------------------------------------------------------------------*/
    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
    }

    /*--------------------------------------------------------------------------------------------*
     *  Getters and setters                                                                       *
     *--------------------------------------------------------------------------------------------*/
    public static synchronized AppController getInstance()
    {
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

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
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
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
        req.setTag(TAG);
        getRequestQueue().add(req);
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
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }
}
