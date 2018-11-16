package com.versatilemobitech.bhattivikramarka.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.VolleyError;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.adapters.ImgaesAdapter;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.models.Images;
import com.versatilemobitech.bhattivikramarka.utils.PopUtils;
import com.versatilemobitech.bhattivikramarka.webUtils.ServerResponse;
import com.versatilemobitech.bhattivikramarka.webUtils.WebServices;
import com.versatilemobitech.bhattivikramarka.webUtils.WsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Excentd11 on 10/17/2017.
 */

public class GalleryInnerFragment extends BaseFragment implements IParseListener {
    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Images> mImagesList = new ArrayList<>();
    private Bundle mBundle;
    private boolean isInitialized = false;
    private String folderName, folderid;
    private boolean loading = true;
    private boolean canScroll;
    private int pageNo = 1, pageCount = 50, totalRecords = 0;

    public static GalleryInnerFragment newInstance() {
        GalleryInnerFragment fragment = new GalleryInnerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery_inner, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.gallery);
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
        setPagination();
        setAdapter();
        if (!isInitialized) {
            if (PopUtils.checkInternetConnection(getContext())) {
                requestForGalleryInner();
            } else {
                PopUtils.alertDialog(getActivity(), getString(R.string.pls_check_internet), null);
            }
            isInitialized = true;
        } else {
            setAdapter();
        }
    }

    private void setReferences() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void setClickListeners() {

    }

    private void setPagination() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    pageNo = pageNo + 1;
                    if (mImagesList.size() < totalRecords) {
                        requestForGalleryInner();
                    } else {
                    }
                }
            }
        });
    }

    private void requestForGalleryInner() {
        showLoadingDialog("Loading", false);
        ServerResponse serverResponse = new ServerResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("folder_id", folderid);
        params.put("page", pageNo + "");
        params.put("no_of_records", pageCount + "");
        serverResponse.serviceRequest(getActivity(), WebServices.URL_IMAGES, params, this, WsUtils.WS_CODE_IMAGES);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            folderName = bundle.getString("folder_name");
            folderid = bundle.getString("folder_id");
            ((HomeActivity) getActivity()).mTxtTitle.setText(String.valueOf(bundle.getString("folder_name")));
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

    @Override
    public void SuccessResponse(String response, int requestCode) {
        hideLoadingDialog(getActivity());
        switch (requestCode) {
            case WsUtils.WS_CODE_IMAGES:
                responseForGallery(response);
                break;
            default:
                break;
        }
    }

    private void responseForGallery(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.get("status").toString();
                String message = jsonObject.get("message").toString();
                Log.e(GalleryInnerFragment.class.getSimpleName(), "onResponse::::::::::::: " + response.toString());

                if (status.equals("200")) {
                    int total_no_of_records = jsonObject.getInt("no_of_records");
                    totalRecords = total_no_of_records;
                    //mRecyclerView.setVisibility(View.VISIBLE);
                    if (pageNo == 1) {
                        mImagesList.clear();
                    } else {
                        int itemPosition = ((pageNo - 1) * pageCount + 1);
                        mRecyclerView.scrollToPosition(itemPosition);
                    }
                    JSONArray jarray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject jobj = jarray.getJSONObject(i);
                        mImagesList.add(new Images(jobj.optString("id"),
                                (jobj.optString("image")),
                                (jobj.optString("folder_id"))));
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
        ImgaesAdapter mImgaesAdapter = new ImgaesAdapter(folderName, getContext(), mImagesList, new GalleryInnerFragment());
        mRecyclerView.setAdapter(mImgaesAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new ImgaesAdapter(getContext(), new ImgaesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        }));
    }
}
