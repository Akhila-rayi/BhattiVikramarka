package com.versatilemobitech.bhattivikramarka.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.adapters.VideosAdapter;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.models.Videos;
import com.versatilemobitech.bhattivikramarka.utils.PopUtils;
import com.versatilemobitech.bhattivikramarka.webUtils.ServerResponse;
import com.versatilemobitech.bhattivikramarka.webUtils.WebServices;
import com.versatilemobitech.bhattivikramarka.webUtils.WsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class VideosFragment extends BaseFragment implements IParseListener {
    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Videos> mVideosList = new ArrayList<>();
    private Bundle mBundle;
    private int pageNo = 1, pageCount = 50, totalRecords = 0;

    public static VideosFragment newInstance() {
        VideosFragment fragment = new VideosFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_videos, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.videos);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
        setPagination();
        setAdapter();

        if (PopUtils.checkInternetConnection(getContext())) {
            requestForVideos();
        } else {
            PopUtils.alertDialog(getActivity(), getString(R.string.pls_check_internet), null);
        }

    }

    private void setReferences() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void setClickListeners() {

    }

    private void setPagination() {

        /*linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);*/

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    pageNo = pageNo + 1;
                    if (mVideosList.size() < totalRecords) {
                        requestForVideos();
                    } else {
                        //                  Toast.makeText(getActivity(), "You reached bottom of page", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void requestForVideos() {
        showLoadingDialog("Loading", false);
        ServerResponse serverResponse = new ServerResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", pageNo + "");
        params.put("no_of_records", pageCount + "");
        serverResponse.serviceRequest(getActivity(), WebServices.URL_VIDEOS, params, this, WsUtils.WS_CODE_VIDEOS);

    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    @Override
    public void ErrorResponse(VolleyError error, int requestCode) {
        hideLoadingDialog(getActivity());
        PopUtils.alertDialog(getActivity(), getString(R.string.something_wrong), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void SuccessResponse(String response, int requestCode) {
        hideLoadingDialog(getActivity());
        switch (requestCode) {
            case WsUtils.WS_CODE_VIDEOS:
                responseForVideos(response);
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void responseForVideos(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                Log.e(VideosFragment.class.getSimpleName(), "onResponse::::::::::::: " + response.toString());
                if (status.equals("200")) {
                    int total_no_of_records = jsonObject.getInt("no_of_records");
                    totalRecords = total_no_of_records;
                    //mRecyclerView.setVisibility(View.VISIBLE);
                    if (pageNo == 1) {
                        mVideosList.clear();
                    } else {
                        int itemPosition = ((pageNo - 1) * pageCount + 1);
                        mRecyclerView.scrollToPosition(itemPosition);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        byte[] data = Base64.decode(jsonObjectData.optString("video_name"), Base64.DEFAULT);
                        String video_name = new String(data, StandardCharsets.UTF_8);
                        mVideosList.add(new Videos(jsonObjectData.optString("id"),
                                video_name,
                                jsonObjectData.optString("youtube_url"),
                                jsonObjectData.optString("youtube_id"),
                                jsonObjectData.optString("thumbnail_image")));
                    }
                    setAdapter();

                } else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setAdapter() {
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        VideosAdapter mVideosAdapter = new VideosAdapter(getContext(), mVideosList, new VideosFragment());
        mRecyclerView.setAdapter(mVideosAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new VideosAdapter(getContext(), new VideosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        }));
    }
}
