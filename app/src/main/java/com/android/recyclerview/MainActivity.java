package com.android.recyclerview;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView horizontal_recycler_view;
    private ArrayList<File> horizontalList;
    private HorizontalAdapter horizontalAdapter;
    ImageCache imgCache;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        imgCache = ImageCache.getInstance(0.125f);

        horizontalList=new ArrayList<File>();
        File[] imgFiles = null;
        requestExternalStorageWritePermission();
        String MEDIA_MOUNTED = "mounted";
        String disk_state = Environment.getExternalStorageState();
        if(disk_state.equals(MEDIA_MOUNTED)) {
            File folder_path = getOrCreateExternalStorageDirectory();
            imgFiles = folder_path.listFiles();
        }
        else{
            Log.e("FolderCreation","External Storage not available");
        }

        if(imgFiles!= null) {
            for (File imgFile : imgFiles){
                Log.d("ImageDetails" , imgFile.getAbsolutePath());
                horizontalList.add(imgFile);
            }
        }

        horizontalAdapter=new HorizontalAdapter(MainActivity.this,horizontalList);

        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
        horizontal_recycler_view.setHasFixedSize(true);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

    }


    private void requestExternalStorageWritePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            Log.e("FolderCreation","No write permission");
        }
    }

    public static File getOrCreateExternalStorageDirectory() {
        boolean result=false;
        if (isExternalStorageWritable()) {

            File newpath = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures" + File.separator + "SampleImages");
            if(! newpath.exists()) {
                result = newpath.mkdirs();
                if (result) {
                    return newpath;
                }
                else{
                    Log.e("FolderCreation","Not able to create sample images folder");
                    return Environment.getExternalStorageDirectory();
                }
            }
            else{
                Log.d("FolderCreation","Sample images folder already exists");
                return newpath;
            }
        }
        else {
            throw new RuntimeException("External storage not writable");
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public Bitmap getBitmapFromFile(File imgFile) {
        if (imgFile.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(imgFile
                    .getAbsolutePath());

            return bmp;
        }
        return null;
    }


    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder>  {

        private List<File> horizontalList;
        private RecyclerView vertical_recycler_view;
        private VerticalAdapter verticalAdapter;
        private ArrayList<ImageDescriptor> verticalList;
        private Context context;

        public HorizontalAdapter(Context context,List<File> horizontalList) {
            this.horizontalList = horizontalList;
            this.context = context;
        }

        @Override
        public HorizontalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.horizontal_item_view, parent, false);

            return new HorizontalViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HorizontalViewHolder holder, final int position) {
            File imgFile = horizontalList.get(position);
            ImageView imageView = holder.imgView;
            if (imageView!= null) {

                if (cancelPotentialDownload(imgFile, imageView)) {
                    final Bitmap bitmap = imgCache.getBitmapFromMemCache(imgFile.getName());
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.popupimg);
                    }
                    ImageLoaderTask task = new ImageLoaderTask(imageView);
                    AsyncDrawable imgDrawable = new AsyncDrawable(task);
                    imageView.setImageDrawable(imgDrawable);
                    task.execute(imgFile);
                }
            }
            //imageView.setImageBitmap(getBitmapFromFile(imgFile));
            final String filename = imgFile.getAbsolutePath();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vertical_recycler_view = (RecyclerView) findViewById(R.id.vertical_recycler_view);
                    vertical_recycler_view.setHasFixedSize(true);
                    try {
                        ExifInterface exif = new ExifInterface(filename);

                        verticalList = new ArrayList<>();

                        verticalList.add(new ImageDescriptor(filename, "IMAGE_LENGTH", exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH )));
                        verticalList.add(new ImageDescriptor(filename,"IMAGE_WIDTH",exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)));
                        verticalList.add(new ImageDescriptor(filename,"DATETIME",exif.getAttribute(ExifInterface.TAG_DATETIME)));
                        verticalList.add(new ImageDescriptor(filename,"TAG_MAKE",exif.getAttribute(ExifInterface.TAG_MAKE)));
                        verticalList.add(new ImageDescriptor(filename, "TAG_MODEL", exif.getAttribute(ExifInterface.TAG_MODEL)));
                        verticalList.add(new ImageDescriptor(filename,"TAG_ORIENTATION",exif.getAttribute(ExifInterface.TAG_ORIENTATION)));
                        verticalList.add(new ImageDescriptor(filename,"TAG_FLASH",exif.getAttribute(ExifInterface.TAG_FLASH)));

                    }
                    catch (IOException e) {
                        System.out.println("Exception!!!");
                        e.printStackTrace();
                    }

                    verticalAdapter = new VerticalAdapter(context, verticalList);
                    LinearLayoutManager verticalLayoutmanager
                            = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    vertical_recycler_view.setLayoutManager(verticalLayoutmanager);
                    vertical_recycler_view.setAdapter(verticalAdapter);
                }

            });


        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }

    }

    class ImageLoaderTask extends AsyncTask<File, Void, Bitmap> {
        private File img;
        private final WeakReference<ImageView> imageViewReference;

        public ImageLoaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(File... params) {
            Bitmap bmp = getBitmapFromFile(params[0]);
            imgCache.addBitmapToCache(params[0].getName(), bmp);
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                ImageLoaderTask imgLoaderTask = getImageLoaderTask(imageView);
                if (this == imgLoaderTask) {
                    imageView.setImageBitmap(bitmap);
                }
                else {
                    Drawable dummyImage = imageView.getContext().getResources().getDrawable(R.drawable.popupimgfile);
                    imageView.setImageDrawable(dummyImage);
                }
            }

        }
    }

    static class AsyncDrawable extends ColorDrawable {
        private final WeakReference<ImageLoaderTask> bitmapLoaderTaskReference;

        public AsyncDrawable(ImageLoaderTask bitmapLoaderTask) {
            super(Color.LTGRAY);
            bitmapLoaderTaskReference =
                    new WeakReference<ImageLoaderTask>(bitmapLoaderTask);
        }

        public ImageLoaderTask getBitmapLoaderTask() {
            return bitmapLoaderTaskReference.get();
        }
    }

    private static boolean cancelPotentialDownload(File img, ImageView imageView) {
        ImageLoaderTask bitmapLoaderTask = getImageLoaderTask(imageView);

        if (bitmapLoaderTask != null) {
            File imgSource = bitmapLoaderTask.img;
            if ((imgSource == null) || (!imgSource.equals(img))) {
                bitmapLoaderTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private static ImageLoaderTask getImageLoaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable downloadedDrawable = (AsyncDrawable)drawable;
                return downloadedDrawable.getBitmapLoaderTask();
            }
        }
        return null;
    }



}

