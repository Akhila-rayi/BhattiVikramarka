package com.versatilemobitech.bhattivikramarka.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.activities.HomeActivity;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.utils.FileUtils;
import com.versatilemobitech.bhattivikramarka.utils.PopUtils;
import com.versatilemobitech.bhattivikramarka.utils.ScalingUtilities;
import com.versatilemobitech.bhattivikramarka.utils.StaticUtils;
import com.versatilemobitech.bhattivikramarka.webUtils.ServerResponse;
import com.versatilemobitech.bhattivikramarka.webUtils.VolleyMultipartRequest;
import com.versatilemobitech.bhattivikramarka.webUtils.WebServices;
import com.versatilemobitech.bhattivikramarka.webUtils.WsUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Excentd11 on 10/17/2017.
 */

public class ComplaintBoxFragment extends BaseFragment implements View.OnClickListener, IParseListener {
    private View view;

    private RelativeLayout mRlUploadImg, mRlUploadPdf;
    TextView mTxtSubmit, mTxtUpload;
    private ImageView mImgView;
    EditText mEdtFullname, mEdtMobileno, mEdtDistrict, mEdtConstituencies, mEdtMandal, mEdtVillage, mEdtDescription;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private String[] PDF_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_CODE_CAMERA = 111;
    private static final int PHONE_GALLERY_CLICK = 1001;
    private static final int PHONE_CAMERA_CLICK = 1002;
    private String selectedImagePath;
    private Bitmap bitmap;
    private Uri uri;
    private String imageString = "";
    private boolean onTimeClicked = false;
    String[] district = {"Adilabad","Bhadradri Kothagudem","Hyderabad","Jagtial","Jangaon","Jayashankar Bhupalpally","Jogulamba Gadwal","Kamareddy","Karimnagar","Khammam","Komaram Bheem Asifabad","Mahabubabad","Mahbubnagar","Mancherial","Medak","Medchal-Malkajgiri","Nalgonda","Nagarkurnool","Nirmal","Nizamabad","peddapalli","Ranga Reddy","Rajanna Sircilla","Sangareddy","Siddipet","Suryapet","Vikarabad","Wanaparthy","Warangal (rural)","Warangal (urban)","Yadadri Bhuvanagiri"};
    public static ComplaintBoxFragment newInstance() {
        ComplaintBoxFragment fragment = new ComplaintBoxFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complaint_box, container, false);

        ((HomeActivity) getActivity()).mTxtTitle.setText(R.string.complaintbox);
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
    }

    private void setReferences() {
        mTxtUpload = (TextView) view.findViewById(R.id.txtUploadImg);
        mTxtSubmit = (TextView) view.findViewById(R.id.txtSubmit);
        mEdtFullname = (EditText) view.findViewById(R.id.edtFullname);
        mEdtMobileno = (EditText) view.findViewById(R.id.edtMobileno);
        mEdtDistrict = (EditText) view.findViewById(R.id.edtDistrict);
        mEdtConstituencies = (EditText) view.findViewById(R.id.edtConstituencies);
        mEdtMandal = (EditText) view.findViewById(R.id.edtMandal);
        mEdtVillage = (EditText) view.findViewById(R.id.edtVillage);
        mEdtDescription = (EditText) view.findViewById(R.id.edtDescription);
        mImgView = (ImageView) view.findViewById(R.id.imgView);
        mImgView.setVisibility(View.GONE);
        mRlUploadImg = (RelativeLayout) view.findViewById(R.id.rlUploadImage);
        mRlUploadPdf = (RelativeLayout) view.findViewById(R.id.rlUploadPdf);
    }

    private void setClickListeners() {
        mRlUploadImg.setOnClickListener(this);
        mRlUploadPdf.setOnClickListener(this);
        mTxtSubmit.setOnClickListener(this);
        mEdtDistrict.setOnClickListener(this);
    }

    private void requestForComplaint() throws IOException {
        showLoadingDialog("Loading", false);
        HashMap<String, String> params = new HashMap<>();
        params.put("full_name", mEdtFullname.getText().toString());
        params.put("mobile_number", mEdtMobileno.getText().toString());
        params.put("district", mEdtDistrict.getText().toString());
        params.put("constituency", mEdtConstituencies.getText().toString());
        params.put("mandal", mEdtMandal.getText().toString());
        params.put("village", mEdtVillage.getText().toString());
        params.put("description", mEdtDescription.getText().toString());

        params.put("", "file.pdf");

        Map<String, VolleyMultipartRequest.DataPart> files = new HashMap<>();
        if (bitmap != null) {
            files.put("user_image", new VolleyMultipartRequest.DataPart(bitmap.toString() + ".jpg", bitmapToByte(bitmap), "image/jpeg"));
        }
        if (uri != null) {
            files.put("user_pdf", new VolleyMultipartRequest.DataPart("filename" + ".pdf", getFileFromUri(getActivity(), uri), "application/pdf"));
        }

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.multipartRequest(getActivity(), WebServices.URL_COMPLAINT_BOX, params, files, this, WsUtils.WS_CODE_COMPLAINT);
    }

    private byte[] getFileFromUri(Context context, Uri uri) throws IOException {
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        try {
            return getBytes(iStream);
        } finally {
            // close the stream
            try {
                iStream.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {

        byte[] bytesResult = null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        try {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            bytesResult = byteBuffer.toByteArray();
        } finally {
            // close the stream
            try {
                byteBuffer.close();
            } catch (IOException ignored) { /* do nothing */ }
        }
        return bytesResult;
    }

    private byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    @Override
    public void ErrorResponse(VolleyError error, int requestCode) {
        onTimeClicked = false;
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
        onTimeClicked = false;
        switch (requestCode) {
            case WsUtils.WS_CODE_COMPLAINT:
                responseForComplaint(response);
                break;
            default:
                break;
        }
    }

    private void responseForComplaint(String response) {
        if (response != null) {
           /* try {
                JSONObject mJsonObject = new JSONObject(response);
                String message = mJsonObject.getString("message");
                Log.e(ComplaintBoxFragment.class.getSimpleName(), "onResponse::::::::::::: " + response.toString());
                if (mJsonObject.getString("status").equals("200")) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    Intent submit = new Intent(getActivity(), HomeActivity.class);
                    startActivity(submit);
                } else {
                    PopUtils.alertDialog(getActivity(), message, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                       }*/

            if (response.equalsIgnoreCase("200")) {
                Toast.makeText(getActivity(), "Complaint posted successfully", Toast.LENGTH_SHORT).show();
                ((HomeActivity) getActivity()).onBackPressed();
            } else {
                Toast.makeText(getActivity(), "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlUploadImage:
                if (!PopUtils.hasPermissions(getActivity(), PERMISSIONS)) {
                    requestPermissions(PERMISSIONS, 1);
                } else {
                    PopUtils.cameraDialog(getContext(), CAMERACLICK, GALLERYCLICK);
                }
                break;
            case R.id.rlUploadPdf:
                if (!PopUtils.hasPermissions(getActivity(), PDF_PERMISSIONS)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(PDF_PERMISSIONS, 2);
                    }
                } else {
                    fetchPdfFile();
                }
                break;
            case R.id.txtSubmit:
                if (!onTimeClicked) {
                    if (checkValidation() != null && !checkValidation().equalsIgnoreCase("")) {
                        PopUtils.alertDialog(getActivity(), checkValidation(), null);
                    } else {
                        onTimeClicked = true;
                        if (PopUtils.checkInternetConnection(getActivity())) {
                            try {
                                requestForComplaint();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            PopUtils.alertDialog(getActivity(), getString(R.string.pls_check_internet), null);
                        }
                    }
                }
                break;
            case R.id.edtDistrict:
                Select_District();
                break;
            default:
                break;
        }

    }
    private void Select_District() {

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.district_dialog, null);
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        //alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationexit;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, district);
        TextView tv_dialogtitle = (TextView) view.findViewById(R.id.txtDistrict);
        tv_dialogtitle.setText("Select District");
        ListView lv_selectuser = (ListView) view.findViewById(R.id.lv_districtselect);
        lv_selectuser.setAdapter(adapter);
        lv_selectuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_gender = parent.getItemAtPosition(position).toString();
                mEdtDistrict.setText(str_gender);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }

    private void fetchPdfFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 100);
    }

    View.OnClickListener CAMERACLICK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PHONE_CAMERA_CLICK);
        }
    };


    View.OnClickListener GALLERYCLICK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHONE_GALLERY_CLICK);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case PHONE_GALLERY_CLICK:
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    selectedImagePath = StaticUtils.getPath(getContext(), selectedImageUri);
                    bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    convertBitmapToFile(bitmap);
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getActivity().getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        String imagePath = FileUtils.getPath(getContext(), selectedImageUri);
                        bitmap = StaticUtils.getResizeImage(getContext(),
                                StaticUtils.PROFILE_IMAGE_SIZE,
                                StaticUtils.PROFILE_IMAGE_SIZE,
                                ScalingUtilities.ScalingLogic.CROP,
                                true,
                                imagePath,
                                selectedImageUri);
                        convertBitmapToFile(bitmap);
                        parcelFileDescriptor.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHONE_CAMERA_CLICK:
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    convertBitmapToFile(bitmap);
                }
                break;
            case 100:
                if (data != null) {
                    uri = data.getData();
                }
                break;
            default:
                break;
        }
    }

    private void convertBitmapToFile(Bitmap bitmap) {
        if (bitmap != null) {
            mImgView.setVisibility(View.VISIBLE);
            mImgView.setImageBitmap(bitmap);
            imageString = Base64.encodeToString(StaticUtils.getBytesFromBitmap(bitmap), Base64.NO_WRAP);
        } else {
            mImgView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PopUtils.cameraDialog(getContext(), CAMERACLICK, GALLERYCLICK);
                } else {
                    Toast.makeText(getActivity(), "Permission was denied. You can't access to camera or gallery", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchPdfFile();
                } else {
                    Toast.makeText(getActivity(), "Permission was denied. You can't access to pdf files.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private String checkValidation() {
        String message = "";
        if (TextUtils.isEmpty(mEdtFullname.getText().toString().trim())) {
            message = getString(R.string.pls_name);
            mEdtFullname.requestFocus();
        } else if (TextUtils.isEmpty(mEdtMobileno.getText().toString().trim())) {
            message = getString(R.string.pls_mobileno);
            mEdtMobileno.requestFocus();
        } else if (mEdtMobileno.getText().toString().length() < 10 || mEdtMobileno.getText().toString().length() >= 13) {
            message = getString(R.string.mobile_length);
            mEdtMobileno.requestFocus();
        } else if (TextUtils.isEmpty(mEdtDistrict.getText().toString().trim())) {
            message = getString(R.string.pls_district);
            mEdtDistrict.requestFocus();
        } else if (TextUtils.isEmpty(mEdtConstituencies.getText().toString().trim())) {
            message = getString(R.string.pls_constituences);
            mEdtConstituencies.requestFocus();
        } else if (TextUtils.isEmpty(mEdtMandal.getText().toString().trim())) {
            message = getString(R.string.pls_mandal);
            mEdtMandal.requestFocus();
        } else if (TextUtils.isEmpty(mEdtVillage.getText().toString().trim())) {
            message = getString(R.string.pls_village);
            mEdtVillage.requestFocus();
        } else if (TextUtils.isEmpty(mEdtDescription.getText().toString().trim())) {
            message = getString(R.string.pls_description);
            mEdtDescription.requestFocus();
        }
        return message;
    }
}
