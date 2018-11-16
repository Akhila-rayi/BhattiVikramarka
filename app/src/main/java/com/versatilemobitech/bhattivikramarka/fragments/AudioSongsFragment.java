package com.versatilemobitech.bhattivikramarka.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
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
import com.versatilemobitech.bhattivikramarka.adapters.AudiosAdapter;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.models.Audios;
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

public class AudioSongsFragment extends BaseFragment implements IParseListener {
    private View view;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Audios> mAudiosList = new ArrayList<>();
    private boolean isInitialized = false;
    private int pageNo = 1, pageCount = 50, totalRecords = 0;

    public static AudioSongsFragment newInstance() {
        AudioSongsFragment fragment = new AudioSongsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_audio_songs, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.audiosongs);
        ((HomeActivity) getActivity()).mImgMenu.setVisibility(View.VISIBLE);
        ((HomeActivity) getActivity()).mImgBack.setVisibility(View.GONE);
        ((HomeActivity) getActivity()).mImgSettings.setVisibility(View.GONE);

        initComponents();
        return view;
    }

    private void initComponents() {
        setReferences();
        getBundleData();
        setPagination();
        setAdapter();

        if (!isInitialized) {
            if (PopUtils.checkInternetConnection(getContext())) {
                requestForAudioSongs();
            } else {
                PopUtils.alertDialog(getActivity(), getString(R.string.pls_check_internet), null);
            }
            isInitialized = true;
        } else {
            setAdapter();
        }
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
                    if (mAudiosList.size() < totalRecords) {
                        requestForAudioSongs();
                    } else {
                    }
                }
            }
        });
    }

    private void setReferences() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    private void requestForAudioSongs() {
        showLoadingDialog("Loading", false);
        ServerResponse serverResponse = new ServerResponse();
        HashMap<String, String> params = new HashMap<>();
        params.put("action", "audios");
        params.put("page", pageNo + "");
        params.put("no_of_records", pageCount + "");
        serverResponse.serviceRequest(getActivity(), WebServices.BASE_URL, params, this, WsUtils.WS_CODE_AUDIOS);
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
            case WsUtils.WS_CODE_AUDIOS:
                responseForAudioSongs(response);
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void responseForAudioSongs(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.optString("status");
                String message = jsonObject.optString("message");
                Log.e(AudioSongsFragment.class.getSimpleName(), "onResponse::::::::::::: " + response.toString());

                if (status.equals("200")) {
                    int total_no_of_records = jsonObject.getInt("no_of_records");
                    totalRecords = total_no_of_records;
                    //mRecyclerView.setVisibility(View.VISIBLE);
                    if (pageNo == 1) {
                        mAudiosList.clear();
                    } else {
                        int itemPosition = ((pageNo - 1) * pageCount + 1);
                        mRecyclerView.scrollToPosition(itemPosition);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.getJSONObject(i);
                        byte[] data = Base64.decode(jsonObjectData.optString("title"), Base64.DEFAULT);
                        String title = new String(data, StandardCharsets.UTF_8);
                        byte[] data1 = Base64.decode(jsonObjectData.optString("description"), Base64.DEFAULT);
                        String description = new String(data1, StandardCharsets.UTF_8);
                        mAudiosList.add(new Audios(jsonObjectData.optString("id"),
                                jsonObjectData.optString("image"),
                                title,
                                description, jsonObjectData.optString("audio")));
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
        mLayoutManager = new LinearLayoutManager(getActivity());
        AudiosAdapter mAudiosAdapter = new AudiosAdapter(getContext(), mAudiosList, new AudioSongsFragment());
        mRecyclerView.setAdapter(mAudiosAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnItemTouchListener(new AudiosAdapter(getContext(), new AudiosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle mBundle = new Bundle();
                mBundle.putInt("position", position);
                navigateFragment(AudioSongsInnerFragment.newInstance(
                        new ArrayList<Audios>(mAudiosList)), "AudioSongsInnerFragment",
                        mBundle, getActivity());
            }
        }));
    }
}
