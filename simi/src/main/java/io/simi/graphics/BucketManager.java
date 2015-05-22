package io.simi.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BucketManager {

    private static final String[] THUMBNAILS_PROJECTION = { Thumbnails.IMAGE_ID, Thumbnails.DATA };
    private static final String[] BUCKETS_PROJECTION = { Media._ID, Media.BUCKET_ID, Media.DATA, Media.BUCKET_DISPLAY_NAME };
    private static final String[] ALBUMS_PROJECTION = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
    private static final String[] IMAGES_PROJECTION = { Media._ID, Media.DATA };

    private ContentResolver mContentResolver;
    private boolean hasCreatedBuckets = false;
    private Map<String, String> thumbnails = new HashMap<String, String>();
    private Map<String, Bucket> buckets = new HashMap<String, Bucket>();
    private Vector<Bucket> tempBuckets = new Vector<Bucket>();
    private Vector<Map<String, String>> albums = new Vector<Map<String, String>>();

    private static BucketManager instance;

    private BucketManager(){

    }

    private BucketManager(Context context){
        mContentResolver = context.getContentResolver();
        instance = new BucketManager();
    }

    public static BucketManager getInstance(Context context){
        if (instance == null) {
            instance = new BucketManager(context);
        }
        return instance;
    }

    private void createThumbnails(){
        Cursor cursor = mContentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, THUMBNAILS_PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            int id;
            String data;
            int idIndex  = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataIndex = cursor.getColumnIndex(Thumbnails.DATA);
            thumbnails.clear();
            do{
                id = cursor.getInt(idIndex );
                data = cursor.getString(dataIndex);
                thumbnails.put(String.valueOf(id), data);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void createAlbums(){
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, ALBUMS_PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Albums._ID);
            int albumIndex  = cursor.getColumnIndex(Albums.ALBUM);
            int albumArtIndex  = cursor.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyIndex  = cursor.getColumnIndex(Albums.ALBUM_KEY);
            int artistIndex  = cursor.getColumnIndex(Albums.ARTIST);
            int numberOfSongsIndex  = cursor.getColumnIndex(Albums.NUMBER_OF_SONGS);
            do {
                int id = cursor.getInt(idIndex);
                int numberOfSongs = cursor.getInt(numberOfSongsIndex);
                String album = cursor.getString(albumIndex);
                String artist = cursor.getString(artistIndex);
                String albumKey = cursor.getString(albumKeyIndex);
                String albumArt = cursor.getString(albumArtIndex);
                Map<String, String> albumItem = new HashMap<String, String>();
                albumItem.put("_id", String.valueOf(id));
                albumItem.put("album", album);
                albumItem.put("albumArt", albumArt);
                albumItem.put("albumKey", albumKey);
                albumItem.put("artist", artist);
                albumItem.put("numOfSongs", String.valueOf(numberOfSongs));
                albums.add(albumItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void createBuckets(){
        createThumbnails();
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, BUCKETS_PROJECTION, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(Media._ID);
            int dataIndex = cursor.getColumnIndex(Media.DATA);
            int bucketDisplayNameIndex = cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cursor.getColumnIndex(Media.BUCKET_ID);
            buckets.clear();
            tempBuckets.clear();
            do {
                String id = cursor.getString(idIndex);
                String data = cursor.getString(dataIndex);
                String bucketDisplayName = cursor.getString(bucketDisplayNameIndex);
                String bucketId = cursor.getString(bucketIdIndex);
                Bucket bucket = buckets.get(bucketId);
                if (bucket == null) {
                    bucket = new Bucket();
                    bucket.name = bucketDisplayName;
                    buckets.put(bucketId, bucket);
                    tempBuckets.add(bucket);
                }
                BucketPhoto photo = new BucketPhoto();
                photo.id = id;
                photo.path = data;
                photo.thumbnail = thumbnails.get(id);
                bucket.add(photo);
            } while (cursor.moveToNext());
            hasCreatedBuckets = true;
        }
        cursor.close();
    }

    public Vector<Bucket> getTempBuckets(boolean refresh){
        if (refresh || (!refresh && !hasCreatedBuckets)) {
            createBuckets();
        }
        return tempBuckets;
    }

    public Vector<Map<String, String>> getAlbums(boolean refresh){
        if (refresh) {
            createAlbums();
        }
        return albums;
    }

    public String getOriginalPhotoPath(String id) {
        Cursor cursor = mContentResolver.query(Media.EXTERNAL_CONTENT_URI, IMAGES_PROJECTION, Media._ID + "=" + id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(Media.DATA));
        }
        return null;
    }

    public static class Bucket{
        public String name;
        public int count = 0;
        public Vector<BucketPhoto> photos = new Vector<BucketPhoto>();
        public void add(BucketPhoto photo){
            photos.add(photo);
            count++;
        }
    }

    public static class BucketPhoto implements Serializable{
        public String id;
        public String path;
        public String thumbnail;
        public boolean isSelected = false;
    }
}