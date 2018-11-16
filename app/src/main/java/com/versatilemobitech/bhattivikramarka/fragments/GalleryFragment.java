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
import com.versatilemobitech.bhattivikramarka.adapters.GalleryAdapter;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.models.Gallery;
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

public class GalleryFragment extends BaseFragment implements IParseListener {
    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Gallery> mGalleryList = new ArrayList<>();
    private Bundle mBundle;
    private boolean isInitialized = false;
    private int pageNo = 1, pageCount = 1, totalRecords = 0;

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.gallery);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.INVISIBLE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();

        setPagination();
//        getBundleData();
        setAdapter();
        if (!isInitialized) {
            if (PopUtils.checkInternetConnection(getContext())) {
                requestForGallery();
            } else {
                PopUtils.alertDialog(getActivity(), getString(R.string.pls_check_internet), null);
            }
            isInitialized = true;
        } else {
            setAdapter();
        }

    }

    private void setReferences() {
        mRecyclerView = view.findViewById(R.id.recyclerView);
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
                    if (mGalleryList.size() < totalRecords) {
                        requestForGallery();
                    }
//                    else {
                        //                  Toast.makeText(getActivity(), "You reached bottom of page", Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
    }

    private void requestForGallery() {
        showLoadingDialog("Loading", false);
        ServerResponse serverResponse = new ServerResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("page", pageNo + "");
        params.put("no_of_records", pageCount + "");
        serverResponse.serviceRequest(getActivity(), WebServices.URL_FOLDER, params, this, WsUtils.WS_CODE_GALLERY);
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
            case WsUtils.WS_CODE_GALLERY:
                responseForGallery(response);
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void responseForGallery(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.get("message").toString();
                Log.e(GalleryFragment.class.getSimpleName(), "onResponse::::::::::::: " + response);


                if (status.equals("200")) {
                   /* int total_no_of_records = jsonObject.getInt("no_of_records");
                    totalRecords = total_no_of_records;
                   // mRecyclerView.setVisibility(View.VISIBLE);
                    if (pageNo == 1) {
                        mGalleryList.clear();
                    } else {
                        int itemPosition = ((pageNo - 1) * pageCount + 1);
                        mRecyclerView.scrollToPosition(itemPosition);
                    }*/
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        byte[] data = Base64.decode(jsonObjectData.optString("folder_name"), Base64.DEFAULT);
                        String folder_name = new String(data, StandardCharsets.UTF_8);
                        mGalleryList.add(new Gallery(jsonObjectData.optString("id"),
                                jsonObjectData.optString("image"),
                                folder_name, jsonObjectData.optString("folder_id")));
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
        GalleryAdapter mGalleryAdapter = new GalleryAdapter(getContext(), mGalleryList, new GalleryFragment());
        mRecyclerView.setAdapter(mGalleryAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new GalleryAdapter(getContext(), new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
              /*  mBundle = new Bundle();
                navigateFragment(GalleryInnerFragment.newInstance(), "IMAGESFRAGMENT", mBundle, getActivity());*/
            }
        }));

    }
}
