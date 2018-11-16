package com.versatilemobitech.bhattivikramarka.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class DailyActivitiesInnerFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    ImageView mImgSend, mImgDailyActivitiesInner;
    TextView mTxtDate, mTxtContent, mTxtTitle;
    String descriptionTxt, imageUrl;

    public static DailyActivitiesInnerFragment newInstance() {
        DailyActivitiesInnerFragment fragment = new DailyActivitiesInnerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_daily_activities_inner, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.dailyactivities);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
    }

    private void setReferences() {
        mImgSend = (ImageView) view.findViewById(R.id.imgShare);
        mImgDailyActivitiesInner = (ImageView) view.findViewById(R.id.imgDailyactivitiesInner);
        mTxtDate = (TextView) view.findViewById(R.id.txtDate);
        mTxtContent = (TextView) view.findViewById(R.id.txtContent);
        mTxtTitle = (TextView) view.findViewById(R.id.txtTitle);
    }

    private void setClickListeners() {

        mImgSend.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Picasso.with(getActivity()).load(bundle.getString("image")).placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder).into(mImgDailyActivitiesInner);
            ((HomeActivity) getActivity()).mTxtTitle.setText(String.valueOf(bundle.getString("title")));
            mTxtDate.setText(String.valueOf(bundle.getString("service_done_on")));
            mTxtContent.setText(String.valueOf(bundle.getString("description")));
            descriptionTxt = bundle.getString("description");
            imageUrl = bundle.getString("image");
            mTxtTitle.setText(String.valueOf(bundle.getString("title")));
        }
    }

    @Override
    public void onClick(View view) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
       /* String shareBody = "Here is the share content body";
        String body = descriptionTxt + imageUrl;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing content");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
        shareItem(imageUrl);
    }

    private void shareItem(String latest_image) {
        Picasso.with(getContext()).load(latest_image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, descriptionTxt);
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.setType("image/*");
                startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
