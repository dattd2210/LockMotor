package com.lockmotor.base.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.lockmotor.R;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FlushedInputStream;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ImageLoaderUtils {

    //******************************************************************************
    // Universal Image Loader configurations
    //******************************************************************************

    public static void configImageLoader(Context context) {
        // get cache dir
        File cacheDir = StorageUtils.getCacheDirectory(context.getApplicationContext());
        if (cacheDir == null)
            cacheDir = Environment.getDownloadCacheDirectory();

        // init display options
        DisplayImageOptions options = getDisplayImageOptions(R.mipmap.placeholder_image);

        // init config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                .threadPoolSize(4)
                .memoryCache(new LRULimitedMemoryCache(3 * 1024 * 1024))
                .diskCache(new LimitedAgeDiscCache(cacheDir, 3600 * 24 * 7))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new AuthImageDownloader(context.getApplicationContext()))
                .defaultDisplayImageOptions(options)
                .build();

        // init image loader
        ImageLoader.getInstance().init(config);
    }

    private static DisplayImageOptions getDisplayImageOptions(int placeHolderResource) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(placeHolderResource)
                .showImageForEmptyUri(placeHolderResource)
                .showImageOnFail(placeHolderResource)
                //.displayer(new FadeInBitmapDisplayer(200))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }

    private static DisplayImageOptions getDisplayImageOptions(int placeHolderResource, int cornerRadiusPixel) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixel))
                .showImageOnLoading(placeHolderResource)
                .showImageForEmptyUri(placeHolderResource)
                .showImageOnFail(placeHolderResource)
                //.displayer(new FadeInBitmapDisplayer(200))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }

    //******************************************************************************
    // Util functions
    //******************************************************************************

    public static void displayImage(int drawableResourceId, ImageView imageView) {
        if (drawableResourceId <= 0) {
            imageView.setImageResource(0);
            return;
        }
        ImageLoader.getInstance().displayImage("drawable://" + drawableResourceId, imageView);
    }

    public static void displayImage(String imageUrl, ImageView imageView) {
        ImageLoader.getInstance().displayImage(imageUrl, imageView);
    }

    public static void displayImage(String imageUrl, ImageView imageView, int placeHolder) {
        DisplayImageOptions options = getDisplayImageOptions(placeHolder);
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    public static void displayImage(String imageUrl, ImageView imageView, int placeHolder, int cornerRadiusPixel) {
        DisplayImageOptions options = getDisplayImageOptions(placeHolder, cornerRadiusPixel);
        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }


    //******************************************************************************
    // Helper class
    //******************************************************************************

    public static class AuthImageDownloader extends BaseImageDownloader {
        public final String TAG = AuthImageDownloader.class.getName();

        public AuthImageDownloader(Context context) {
            super(context, 300, 1800);
        }

        public AuthImageDownloader(Context context, int connectTimeout, int readTimeout) {
            super(context, connectTimeout, readTimeout);
        }

        @Override
        protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

            URL url = null;
            try {
                url = new URL(imageUri);
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            HttpURLConnection http = null;

            if (Scheme.ofUri(imageUri) == Scheme.HTTPS) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                http = https;
                http.connect();
            } else {
                http = (HttpURLConnection) url.openConnection();
            }

            http.setConnectTimeout(connectTimeout);
            http.setReadTimeout(readTimeout);
            return new FlushedInputStream(new BufferedInputStream(
                    http.getInputStream()));
        }

        // always verify the host - dont check for certificate
        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        /**
         * Trust every server - dont check for any certificate
         */
        private void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] x509Certificates,
                        String s) throws java.security.cert.CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
