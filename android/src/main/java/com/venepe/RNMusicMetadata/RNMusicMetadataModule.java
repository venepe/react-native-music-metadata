package com.venepe.RNMusicMetadata;

import android.net.Uri;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import wseemann.media.FFmpegMediaMetadataRetriever;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

/**
 * Created by venepe on 5/06/17.
 */
public class RNMusicMetadataModule extends ReactContextBaseJavaModule {

    @Override
    public String getName() {
        return "RNMusicMetadata";
    }

    public RNMusicMetadataModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    private WritableMap getData(String path) {
        Uri uri = Uri.parse(path);
        FFmpegMediaMetadataRetriever meta = new FFmpegMediaMetadataRetriever();
        meta.setDataSource(getReactApplicationContext(), uri);
        String title = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        String albumName = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
       // String albumArtist = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
        String genre = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE);
        String date = meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE);
        Double duration = Double.valueOf(meta.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000.0;

        // Creates the returnable objects for Javascript
        WritableMap songMap = Arguments.createMap();
        songMap.putString("title", title);
        songMap.putString("artist", artist);
        songMap.putString("album", albumName);
       // songMap.putString("albumArtist", albumArtist);
        songMap.putDouble("duration", duration);
        songMap.putString("date", date);
        songMap.putString("genre", genre);
        songMap.putString("url", path);
        songMap.putString("id", title);
        byte[] imagebytes =  meta.getEmbeddedPicture();
        if (imagebytes != null) {
            Bitmap songImage = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
            try {
              String pathToImg = Environment.getExternalStorageDirectory() + "/" + title + ".jpg";
              String encoded = saveIndGetPath(pathToImg, songImage);
              songMap.putString("artwork", "file://" + encoded);
            } catch (Exception e) {
              //TODO: handle exception
            }
          }
        return songMap;
    }

    @ReactMethod
    public void getMetadata(ReadableArray uris, Promise promise) {
        WritableArray songArray = Arguments.createArray();
        for (int i = 0; i < uris.size(); i++) {
            String uri = uris.getString(i);
            WritableMap songMap = this.getData(uri);
            songArray.pushMap(songMap);
        }
        promise.resolve(songArray);
    }
    public String saveIndGetPath(String pathToImg, Bitmap songImage) throws IOException {
        try{
    
            if (songImage != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                songImage.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
    
                if(byteArray != null) {
                  savorage(pathToImg, imageByte);
    
                    return pathToImg;
                }
            }
    
        } catch (IOException e){
            Log.e("Error savingImageAfter",e.getMessage());
        }
    
        return "";
    }
    
    
    public void savorage(String pathToImg, byte[] imageBytes) throws IOException {
        FileOutputStream fos = null;
        try {
            File filePath = new File(pathToImg);
            fos = new FileOutputStream(filePath, true);
            fos.write(imageBytes);
        } catch (IOException e){
            Log.e("Error saving image => ",e.getMessage());
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }
    }

}

