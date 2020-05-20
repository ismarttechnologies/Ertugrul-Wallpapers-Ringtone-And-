package com.wallpapers.ertugrul.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
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

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.universalvideoview.UniversalVideoView;
import com.varunest.sparkbutton.SparkButton;
import com.wallpapers.ertugrul.R;
import com.wallpapers.ertugrul.model.Ringtone;
import com.wallpapers.ertugrul.model.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import at.grabner.circleprogress.CircleProgressView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.content.ContentValues.TAG;

public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.DriverViewHolder> {

    private ArrayList<Ringtone> list_data;
    private Context context;
    RingtoneAdapter.OnItemListner OnItemListner;

    String totalTime;
    int last_position=-1;
    private Handler handler;
    Runnable updateTimeTask;
    boolean isPlay;

    public interface OnItemListner {
        void setRingtone(Ringtone ringtone);
        void likeRingtone(Ringtone ringtone);
    }

    public RingtoneAdapter(Context context, ArrayList<Ringtone> dataList, RingtoneAdapter.OnItemListner OnItemCheckListner) {
        this.list_data = dataList;
        this.context = context;
        this.OnItemListner = OnItemCheckListner;

        handler = new Handler();


    }

    @Override
    public RingtoneAdapter.DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ringtone, parent, false);
        return new RingtoneAdapter.DriverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RingtoneAdapter.DriverViewHolder holder, final int position) {


        holder.play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPlay) {
                    if (last_position==position){
                        holder.gifDrawable.start();

                        isPlay = true;
                        playAudio(holder, list_data.get(position), position);
                    }
                    else {

                        holder.mVideoView.setAutoRotation(false);
                        holder.mVideoView.setFitXY(true);

                        Uri uri = Uri.parse(list_data.get(position).getUrl());
                        holder.mVideoView.setVideoURI(uri);


//        holder.mVideoView.setFullscreen(false, OrientationHelper.VERTICAL);

                        isPlay = true;
                        playAudio(holder, list_data.get(position), position);
                        last_position = position;
                    }
                } else {
                    holder.gifDrawable.stop();
                    isPlay = false;
                    holder.play_pause.setBackgroundResource(R.drawable.play);
                    holder.mVideoView.pause();
                }
            }
        });


        holder.set_tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnItemListner.setRingtone(list_data.get(position));

            }
        });

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnItemListner.likeRingtone(list_data.get(position));

            }
        });


    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }


    public class DriverViewHolder extends RecyclerView.ViewHolder {

        public ImageView set_tone;

        View mBottomLayout;
        View mVideoLayout;
        UniversalVideoView mVideoView;
        ImageView play_pause;
        SparkButton likes;
        TextView tvTime;
        SeekBar seekBar;
        CircleProgressView progress_dialog;
        GifImageView gif_audio;
        GifDrawable gifDrawable;


        public DriverViewHolder(View view) {
            super(view);
            set_tone = view.findViewById(R.id.set_tone);
            play_pause = view.findViewById(R.id.ivPlayPause);
            likes = view.findViewById(R.id.likes);
            tvTime = view.findViewById(R.id.tvTime);
            seekBar = view.findViewById(R.id.seekBar);
            progress_dialog = view.findViewById(R.id.progress_dialog);
            mVideoView = (UniversalVideoView) view.findViewById(R.id.videoView);
            gif_audio =  view.findViewById(R.id.gif_audio);
//            mMediaController = (UniversalMediaController) view.findViewById(R.id.media_controller);

            try {

                //resource (drawable or raw)
                gifDrawable = new GifDrawable(context.getResources(), R.drawable.audio_gif);
                gif_audio.setImageDrawable(gifDrawable);
                gifDrawable.stop();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }




    public void playAudio(final RingtoneAdapter.DriverViewHolder holder, Ringtone video_status, int position){

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
                holder.gifDrawable.start();

                handler.postDelayed(updateTimeTask, 900);

            }

            @Override
            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
                Log.d(TAG, "onBufferingStart UniversalVideoView callback");
                holder.progress_dialog.spin();
                holder.progress_dialog.setVisibility(View.VISIBLE);
                holder.gifDrawable.stop();


            }

            @Override
            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
                Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
                holder.progress_dialog.stopSpinning();
                holder.progress_dialog.setVisibility(View.GONE);
                holder.gifDrawable.start();


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