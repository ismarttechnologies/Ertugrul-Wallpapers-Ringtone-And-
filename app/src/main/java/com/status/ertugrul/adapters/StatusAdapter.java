package com.status.ertugrul.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.universalvideoview.UniversalVideoView;
import com.varunest.sparkbutton.SparkButton;
import com.status.ertugrul.R;
import com.status.ertugrul.model.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import at.grabner.circleprogress.CircleProgressView;

import static android.content.ContentValues.TAG;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.DriverViewHolder> {

    private ArrayList<Status> list_data;
    private Context context;
    StatusAdapter.OnItemListner OnItemListner;

    String totalTime;
    int last_position=-1;
    private Handler handler;
    Runnable updateTimeTask;
    boolean isPlay;


    public interface OnItemListner {
        void onShareImage(Bitmap bitmap);
        void onShareVideo(Status status);
    }

    public StatusAdapter(Context context, ArrayList<Status> dataList, StatusAdapter.OnItemListner OnItemCheckListner) {
        this.list_data = dataList;
        this.context = context;
        this.OnItemListner = OnItemCheckListner;

        handler = new Handler();


    }

    @Override
    public StatusAdapter.DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_status, parent, false);
        return new StatusAdapter.DriverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StatusAdapter.DriverViewHolder holder, final int position) {

        if (list_data.get(position).getType()!=null && !list_data.get(position).getType().isEmpty() && list_data.get(position).getType().equalsIgnoreCase("Banner")){
            holder.card_view_outer.setVisibility(View.GONE);
            holder.adView.setVisibility(View.VISIBLE);

            AdRequest adRequest = new AdRequest.Builder().build();
            holder.adView.loadAd(adRequest);
        }
        else{

            String imageAddress = list_data.get(position).getThumbnail();
//        imageAddress = WebConstants.USER_PROFILE_URL + imageAddress;


            int a = holder.thumbnail.getMeasuredWidth();
            int b = holder.thumbnail.getMeasuredHeight();
            String url = list_data.get(position).getUrl();

//        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(list_data.get(position).getUrl(), MediaStore.Video.Thumbnails.MICRO_KIND);
//        holder.thumbnail.setImageBitmap(bMap);



//        Glide.with(context)
//                .load(list_data.get(position).getUrl())
//                .thumbnail(Glide.with(context).load(list_data.get(position).getUrl()))
//                .into(holder.thumbnail);


            try {
                retriveVideoFrameFromVideo(list_data.get(position).getUrl(), holder.thumbnail);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

//            Picasso.get()
//                    .load(imageAddress)
//                    .priority(Picasso.Priority.HIGH)
//                    .into(new Target() {
//                        @Override
//                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                            list_data.get(position).setBitmap(bitmap);
//                            holder.thumbnail.setImageBitmap(bitmap);
//                        }
//
//                        @Override
//                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                            e.printStackTrace();
//
//                        }
//
//                        @Override
//                        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//
//                        }
//                    });


//            holder.like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                OnItemListner.onShareImage(list_data.get(position).getBitmap());
//
//                    Animation pulse = AnimationUtils.loadAnimation(context, R.anim.like_animation);
//                    holder.like.startAnimation(pulse);
//
//                }
//            });


            holder.play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.thumbnail.setVisibility(View.GONE);
                    if (!isPlay) {

                        if (last_position==position){

                            isPlay = true;
                            showVideo(holder, list_data.get(position), position);
                        }
                        else {
//        holder.mVideoView.setMediaController(holder.mMediaController);

                            holder.mVideoView.setAutoRotation(false);
                            holder.mVideoView.setFitXY(true);

//        holder.mVideoView.setFullscreen(false, OrientationHelper.VERTICAL);
                            Uri uri = Uri.parse(list_data.get(position).getUrl());
                            holder.mVideoView.setVideoURI(uri);

                            isPlay = true;
                            showVideo(holder, list_data.get(position), position);
                            last_position = position;

                        }

                    } else {
                        isPlay = false;
                        holder.play_pause.setBackgroundResource(R.drawable.play);
                        holder.mVideoView.pause();
                    }
                }
            });




            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                sendIntent.setType("text/plain");
//                sendIntent.setPackage("com.whatsapp");
//                context.startActivity(sendIntent);



//                Uri uri = Uri.parse(list_data.get(position).getThumbnail());
//                Intent videoshare = new Intent(Intent.ACTION_SEND);
//                videoshare.setType("*/*");
//                videoshare.setPackage("com.whatsapp");
//                videoshare.putExtra(Intent.EXTRA_STREAM, uri);
//
//                context.startActivity(videoshare);

                    OnItemListner.onShareVideo(list_data.get(position));

                }
            });
        }


    }


    Bitmap bitmap = null;

    public void retriveVideoFrameFromVideo(final String videoPath, final ImageView image) //throws Throwable
    {


        Thread thread = new Thread() {
            @Override
            public void run() {
//                try { Thread.sleep(2000); }
//                catch (InterruptedException e) {}

                MediaMetadataRetriever mediaMetadataRetriever = null;
                try
                {
                    mediaMetadataRetriever = new MediaMetadataRetriever();
                    if (Build.VERSION.SDK_INT >= 14)
                        mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
                    else
                        mediaMetadataRetriever.setDataSource(videoPath);
                    //   mediaMetadataRetriever.setDataSource(videoPath);
                    bitmap = mediaMetadataRetriever.getFrameAtTime();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                } finally {
                    if (mediaMetadataRetriever != null) {
                        mediaMetadataRetriever.release();
                    }
                }


                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //change View Data
                        image.setImageBitmap(bitmap);

                    }
                });

            }
        };
        thread.start();


    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class DriverViewHolder extends RecyclerView.ViewHolder {
        public ImageView  share, thumbnail;
        public SparkButton like;

        View mBottomLayout;
        View mVideoLayout;
        UniversalVideoView mVideoView;
        //        UniversalMediaController mMediaController;
        ImageView play_pause;
        TextView tvTime;
        SeekBar seekBar;
        CircleProgressView progress_dialog;
        private AdView adView;
        CardView card_view_outer;


        public DriverViewHolder(View view) {
            super(view);
             like= view.findViewById(R.id.like);
            share = view.findViewById(R.id.share);
//            download = view.findViewById(R.id.download);
            thumbnail = view.findViewById(R.id.thumbnail);
            play_pause = view.findViewById(R.id.ivPlayPause);
            tvTime = view.findViewById(R.id.tvTime);
            seekBar = view.findViewById(R.id.seekBar);
            progress_dialog = view.findViewById(R.id.progress_dialog);
            card_view_outer = view.findViewById(R.id.card_view_outer);
            adView = view.findViewById(R.id.adView);
            mVideoView = (UniversalVideoView) view.findViewById(R.id.videoView);
//            mMediaController = (UniversalMediaController) view.findViewById(R.id.media_controller);

        }
    }




    public void showVideo(final DriverViewHolder holder, Status video_status, int position){

        holder.play_pause.setBackgroundResource(R.drawable.pause);
        holder.mVideoView.start();

        if (position!=last_position) {
            holder.progress_dialog.setVisibility(View.VISIBLE);
            holder.progress_dialog.spin();
        }


        holder.progress_dialog.setBarWidth(8);
        holder.progress_dialog.setRimWidth(8);

        holder.progress_dialog.setBarColor(context.getResources().getColor(R.color.colorPrimary));
        holder.progress_dialog.setRimColor(context.getResources().getColor(R.color.white));


        holder.mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
            @Override
            public void onScaleChange(boolean isFullscreen) {
//                this.isFullscreen = isFullscreen;
                if (isFullscreen) {
                    ViewGroup.LayoutParams layoutParams = holder.mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    holder.mVideoLayout.setLayoutParams(layoutParams);
                    //GONE the unconcerned views to leave room for video and controller
                    holder.mBottomLayout.setVisibility(View.GONE);
                } else {
                    ViewGroup.LayoutParams layoutParams = holder.mVideoLayout.getLayoutParams();
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = 200; //this.cachedHeight;
                    holder.mVideoLayout.setLayoutParams(layoutParams);
                    holder.mBottomLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPause(MediaPlayer mediaPlayer) { // Video pause
                Log.d(TAG, "onPause UniversalVideoView callback");
                holder.progress_dialog.stopSpinning();
                holder.progress_dialog.setVisibility(View.GONE);
            }

            @Override
            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
                Log.d(TAG, "onStart UniversalVideoView callback");
                holder.progress_dialog.stopSpinning();
                holder.progress_dialog.setVisibility(View.GONE);

                handler.postDelayed(updateTimeTask, 900);
            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart UniversalVideoView callback");
                holder.progress_dialog.spin();
                holder.progress_dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
                holder.progress_dialog.stopSpinning();
                holder.progress_dialog.setVisibility(View.GONE);
            }
        });


        updateTimeTask = new Runnable() {
            @Override
            public void run() {
                long totalDuration = holder.mVideoView.getDuration();
                long currentDuration = holder.mVideoView.getCurrentPosition();

                totalTime = getDateFromTimestamp(String.valueOf(totalDuration/1000), "mm:ss");
                String updateTime = getDateFromTimestamp(String.valueOf(currentDuration/1000), "mm:ss");
                holder.tvTime.setVisibility(View.VISIBLE);
                holder.tvTime.setText(updateTime + " / " + totalTime);
                // update progress bar
                int progress = (int) (getProgressPercentage(currentDuration, totalDuration));
                holder.seekBar.setProgress(progress);

                handler.postDelayed(this, 900);

            }
        };


        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.e(TAG, "progress: " + progress);

                //videoView.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateTimeTask);

            }
        });


//        holder.seekBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // PRESSED
//                        Log.d("^^^^^^^^^^^^^^^", " PRESSED");
//
//                        return true; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        // RELEASED
//
//                        holder.mVideoView.seekTo(holder.seekBar.getProgress()*1000);
//                        Log.d("^^^^^^^^^^^^^^^", " RELEASED ->"+holder.seekBar.getProgress()*1000);
//
//                        return true; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });



        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacks(updateTimeTask);
                isPlay = false;
                holder.play_pause.setBackgroundResource(R.drawable.play);

                holder.tvTime.setText(totalTime + " / " + totalTime);
                // update progress bar
                holder.seekBar.setProgress(100);

            }
        });
    }


    public static String getDateFromTimestamp(String dateInTimeStamp, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.format(new Date(Long.valueOf(dateInTimeStamp) * 1000L));
    }




    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;


        // return percentage
        return percentage.intValue();
    }




}