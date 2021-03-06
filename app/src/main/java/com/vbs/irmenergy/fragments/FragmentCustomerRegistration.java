package com.vbs.irmenergy.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.activity.MainActivity;
import com.vbs.irmenergy.activity.PaymentDetailActivity;
import com.vbs.irmenergy.activity.ScanActivity;
import com.vbs.irmenergy.utilities.APIProgressDialog;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;
import com.vbs.irmenergy.utilities.volley.VolleyAPIClass;
import com.vbs.irmenergy.utilities.volley.VolleyResponseInterface;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentCustomerRegistration extends Fragment implements OnClickListener,
        VolleyResponseInterface, AdapterView.OnItemSelectedListener {

    private View view;
    private LinearLayout ll_reg1, ll_reg2, ll_reg3, ll_corporate, ll_scan, ll_data,
            ll_barcode, ll_attachment1, ll_attachment2, ll_browse1, ll_browse2,
            ll_attachment12, ll_attachment22;
    private ImageView img_reg1, img_reg2, img_reg3, img_remove1, img_remove2,
            img_remove12, img_remove22;
    private TextView tv_attachment_name1, tv_attachment_name2, tv_attachment_name12,
            tv_attachment_name22;
    private Button btn_save;
    private VolleyAPIClass volleyAPIClass;
    private APIProgressDialog mProgressDialog;
    private String[] type_id, type_name, category_id, category_name, corporate_id,
            corporate_name, property_id, property_name, ownership_id, ownership_name,
            contractor_id, contractor_name, billing_id, billing_name, state_id, state_name,
            city_id, city_name, area_id, area_name, doc_id, doc_name;
    private String stringTypeId = "0", stringCategoryId = "0",
            stringCorporateId = "0", stringPropertyId = "0", stringOwnerId = "0",
            stringContractorId = "-2", stringBillingTo = "-1", stringState = "0",
            stringCity = "0", stringArea = "0", stringDoc1 = "0", stringDoc2 = "0",
            stringDocName1 = "", stringDocName2 = "";
    private Spinner sp_customer_type, sp_customer_category, sp_corporate_name,
            sp_owner_type, sp_property_type, sp_contractor, sp_billing_to,
            sp_doc1, sp_doc2, sp_state, sp_city, sp_area;
    private EditText et_application_no, et_date, et_firstname, et_middlename, et_lastname,
            et_dob, et_block_no, et_address1, et_address2, et_pincode, et_contact_no,
            et_mobile_no, et_email, et_lpg_no, et_lpg_distributor, et_lpg_omc, et_property_name,
            et_owner_name, et_owner_contact_no, et_remarks;
    private MaskedEditText et_aadhar_no;
    private RadioGroup rg_payment;
    private String paymentType = "online";
    private Calendar myCalendar;
    private String filePath, fileName, ftpDirectory;
    private Uri mCapturedImageURI;
    private File[] fileImage;
    private int imageType = 0;
    private String[] ftpFileName;
    private Button btn_verify_app_no;
    private String application_date, customer_type, customer_category, corporation_name,
            billing_to, first_name, middle_name, last_name, dob, adhar_no,
            block_no, address, society, landmark, state, city, area, pincode,
            customer_mobile, customer_email, lpg_cust_no, lpg_distributor,
            lpg_omc, property_type, property_name1, ownership_type,
            ownership_name1, owner_mobile, dma_contractor, remarks,
            center_code, doc1, doc2, customer_phone, plan_id;
    private int indexCity = 0, indexArea = 0, indexState = 0, indexPropType = 0,
            indexOwnerType = 0, indexDMA = 0;
    private boolean flag = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_registration, container, false);

        init();

        rg_payment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_online:
                        paymentType = "online";
                        break;
                    case R.id.rb_cheque:
                        paymentType = "cheque";
                        break;
                }
            }
        });

        return view;
    }

    public void init() {
        Utility.setTitle(getActivity(), "Registration");
        mProgressDialog = new APIProgressDialog(getActivity(), R.style.DialogStyle);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);

        fileImage = new File[4];
        fileImage[0] = null;
        fileImage[1] = null;
        fileImage[2] = null;
        fileImage[3] = null;

        ftpFileName = new String[4];
        ftpFileName[0] = "";
        ftpFileName[1] = "";
        ftpFileName[2] = "";
        ftpFileName[3] = "";

        volleyAPIClass = new VolleyAPIClass();
        myCalendar = Calendar.getInstance();

        ll_attachment1 = view.findViewById(R.id.ll_doc_attach1);
        ll_attachment2 = view.findViewById(R.id.ll_doc_attach2);
        tv_attachment_name1 = view.findViewById(R.id.tv_doc1_attachment);
        tv_attachment_name2 = view.findViewById(R.id.tv_doc2_attachment);
        img_remove1 = view.findViewById(R.id.img_doc1_remove);
        img_remove1.setOnClickListener(this);
        img_remove2 = view.findViewById(R.id.img_doc2_remove);
        img_remove2.setOnClickListener(this);

        ll_attachment12 = view.findViewById(R.id.ll_doc_attach12);
        ll_attachment22 = view.findViewById(R.id.ll_doc_attach22);
        tv_attachment_name12 = view.findViewById(R.id.tv_doc12_attachment);
        tv_attachment_name22 = view.findViewById(R.id.tv_doc22_attachment);
        img_remove12 = view.findViewById(R.id.img_doc12_remove);
        img_remove12.setOnClickListener(this);
        img_remove22 = view.findViewById(R.id.img_doc22_remove);
        img_remove22.setOnClickListener(this);

        btn_verify_app_no = view.findViewById(R.id.btn_verify_app_no);
        btn_verify_app_no.setOnClickListener(this);

        ll_data = view.findViewById(R.id.ll_data);
        ll_barcode = view.findViewById(R.id.ll_barcode);
        ll_scan = view.findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        ll_browse1 = view.findViewById(R.id.ll_browse1);
        ll_browse1.setOnClickListener(this);
        ll_browse2 = view.findViewById(R.id.ll_browse2);
        ll_browse2.setOnClickListener(this);
        ll_corporate = view.findViewById(R.id.ll_corporate);
        ll_reg1 = view.findViewById(R.id.ll_reg1);
        ll_reg2 = view.findViewById(R.id.ll_reg2);
        ll_reg3 = view.findViewById(R.id.ll_reg3);
        img_reg1 = view.findViewById(R.id.img_reg1);
        img_reg1.setOnClickListener(this);
        img_reg2 = view.findViewById(R.id.img_reg2);
        img_reg2.setOnClickListener(this);
        img_reg3 = view.findViewById(R.id.img_reg3);
        img_reg3.setOnClickListener(this);
        btn_save = view.findViewById(R.id.btn_cust_payment);
        btn_save.setOnClickListener(this);

        sp_customer_type = (Spinner) view.findViewById(R.id.sp_cust_type);
        sp_customer_type.setOnItemSelectedListener(this);
        sp_customer_category = (Spinner) view.findViewById(R.id.sp_cust_category);
        sp_customer_category.setOnItemSelectedListener(this);
        sp_corporate_name = (Spinner) view.findViewById(R.id.sp_corporate_name);
        sp_corporate_name.setOnItemSelectedListener(this);
        sp_property_type = (Spinner) view.findViewById(R.id.sp_property_type);
        sp_property_type.setOnItemSelectedListener(this);
        sp_owner_type = (Spinner) view.findViewById(R.id.sp_owner_type);
        sp_owner_type.setOnItemSelectedListener(this);
        sp_contractor = (Spinner) view.findViewById(R.id.sp_contractor);
        sp_contractor.setOnItemSelectedListener(this);
        sp_billing_to = (Spinner) view.findViewById(R.id.sp_billing_to);
        sp_billing_to.setOnItemSelectedListener(this);
        sp_doc1 = (Spinner) view.findViewById(R.id.sp_doc1);
        sp_doc1.setOnItemSelectedListener(this);
        sp_doc2 = (Spinner) view.findViewById(R.id.sp_doc2);
        sp_doc2.setOnItemSelectedListener(this);
        sp_state = (Spinner) view.findViewById(R.id.sp_reg_state);
        sp_state.setOnItemSelectedListener(this);
        sp_city = (Spinner) view.findViewById(R.id.sp_reg_city);
        sp_city.setOnItemSelectedListener(this);
        sp_area = (Spinner) view.findViewById(R.id.sp_reg_area);
        sp_area.setOnItemSelectedListener(this);

        et_application_no = (EditText) view.findViewById(R.id.et_reg_application_no);
        et_application_no.setTag(Utility.getAppPrefString(getActivity(), "center_prefix"));
        et_date = (EditText) view.findViewById(R.id.et_reg_application_date);
        et_date.setOnClickListener(this);
        et_firstname = (EditText) view.findViewById(R.id.et_reg_first_name);
        et_middlename = (EditText) view.findViewById(R.id.et_reg_middle_name);
        et_lastname = (EditText) view.findViewById(R.id.et_reg_last_name);
        et_dob = (EditText) view.findViewById(R.id.et_reg_dob);
        et_dob.setOnClickListener(this);
        et_aadhar_no = (MaskedEditText) view.findViewById(R.id.et_reg_aadhar_no);
        et_block_no = (EditText) view.findViewById(R.id.et_reg_block_no);
        et_address1 = (EditText) view.findViewById(R.id.et_reg_address1);
        et_address2 = (EditText) view.findViewById(R.id.et_reg_address2);
        et_pincode = (EditText) view.findViewById(R.id.et_reg_pincode);
        et_contact_no = (EditText) view.findViewById(R.id.et_reg_contact_no);
        et_mobile_no = (EditText) view.findViewById(R.id.et_reg_mobile_no);
        et_email = (EditText) view.findViewById(R.id.et_reg_email_id);
        et_lpg_no = (EditText) view.findViewById(R.id.et_reg_lpg_no);
        et_lpg_distributor = (EditText) view.findViewById(R.id.et_reg_distributor);
        et_lpg_omc = (EditText) view.findViewById(R.id.et_reg_lpg_omc);
        et_property_name = (EditText) view.findViewById(R.id.et_reg_property_name);
        et_owner_name = (EditText) view.findViewById(R.id.et_reg_owner_name);
        et_owner_contact_no = (EditText) view.findViewById(R.id.et_reg_owner_contact_no);
        et_remarks = (EditText) view.findViewById(R.id.et_reg_remarks);
        rg_payment = (RadioGroup) view.findViewById(R.id.rg_payment);

        getCustomerType();
        getDocumentList();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_app_no:
                if (!et_application_no.getText().toString().trim()
                        .equalsIgnoreCase("")) {
                    verifyAppNo();
                } else {
                    Utility.toast("Please enter application no.", getActivity());
                }
                break;
            case R.id.ll_scan:
                startActivity(new Intent(getActivity(), ScanActivity.class));
                break;
            case R.id.img_doc1_remove:
                tv_attachment_name1.setText("");
                ll_attachment1.setVisibility(View.GONE);
                fileImage[0] = null;
                ftpFileName[0] = "";
                break;
            case R.id.img_doc2_remove:
                tv_attachment_name2.setText("");
                ll_attachment2.setVisibility(View.GONE);
                fileImage[1] = null;
                ftpFileName[1] = "";
                break;
            case R.id.img_doc12_remove:
                tv_attachment_name12.setText("");
                ll_attachment12.setVisibility(View.GONE);
                fileImage[2] = null;
                ftpFileName[2] = "";
                break;
            case R.id.img_doc22_remove:
                tv_attachment_name22.setText("");
                ll_attachment22.setVisibility(View.GONE);
                fileImage[3] = null;
                ftpFileName[3] = "";
                break;
            case R.id.ll_browse1:
                View sheetView = getLayoutInflater().
                        inflate(R.layout.dialog_picker, null);

                BottomDialog bottomDialog = new BottomDialog.Builder(getActivity())
                        .setTitle("Upload Document")
                        .setContent("")
                        .setCustomView(sheetView)
                        .autoDismiss(true)
                        .show();

                LinearLayout camera = (LinearLayout) sheetView.findViewById(R.id.ll_cam);
                LinearLayout gallery = (LinearLayout) sheetView.findViewById(R.id.ll_gallery);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog.dismiss();
                        imageType = 1;
                        filePath = Utility.getTimeStamp() + ".jpg";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, filePath);
                        mCapturedImageURI = getActivity().getContentResolver()
                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                        startActivityForResult(intent, 101);
                    }
                });

                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog.dismiss();
                        imageType = 1;
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 102);
                    }
                });
                break;
            case R.id.ll_browse2:
                View sheetView1 = getLayoutInflater().
                        inflate(R.layout.dialog_picker, null);

                BottomDialog bottomDialog1 = new BottomDialog.Builder(getActivity())
                        .setTitle("Upload Document")
                        .setContent("")
                        .setCustomView(sheetView1)
                        .autoDismiss(true)
                        .show();

                LinearLayout camera1 = (LinearLayout) sheetView1.findViewById(R.id.ll_cam);
                LinearLayout gallery1 = (LinearLayout) sheetView1.findViewById(R.id.ll_gallery);

                camera1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog1.dismiss();
                        imageType = 2;
                        filePath = Utility.getTimeStamp() + ".jpg";
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, filePath);
                        mCapturedImageURI = getActivity().getContentResolver()
                                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                        startActivityForResult(intent, 101);
                    }
                });

                gallery1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomDialog1.dismiss();
                        imageType = 2;
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 102);
                    }
                });
                break;
            case R.id.et_reg_application_date:
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_date.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
                break;
            case R.id.et_reg_dob:
                DatePickerDialog dialog1 = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd-MM-yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                et_dob.setText(sdf.format(myCalendar.getTime()));
                            }
                        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog1.show();
                break;
            case R.id.btn_cust_payment:
                if (!flag) {
                    if (et_application_no.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter application no.", getActivity());
                    else if (et_date.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please select application date", getActivity());
                    else if (TextUtils.isEmpty(stringCategoryId))
                        Utility.toast("Please select customer type", getActivity());
                    else if (TextUtils.isEmpty(stringBillingTo))
                        Utility.toast("Please select billing to", getActivity());
                    else if (et_firstname.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter first name", getActivity());
                    else if (et_lastname.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter last name", getActivity());
                    else if (TextUtils.isEmpty(stringDoc1))
                        Utility.toast("Please select document1", getActivity());
                    else if (TextUtils.isEmpty(stringDoc2))
                        Utility.toast("Please select document2", getActivity());
                    else if (et_address1.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter society name", getActivity());
                    else if (TextUtils.isEmpty(stringState))
                        Utility.toast("Please select state", getActivity());
                    else if (TextUtils.isEmpty(stringCity))
                        Utility.toast("Please select city", getActivity());
                    else if (TextUtils.isEmpty(stringArea))
                        Utility.toast("Please select area", getActivity());
                    else if (et_pincode.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter area pincode", getActivity());
                    else if (et_mobile_no.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter mobile no.", getActivity());
                    else if (TextUtils.isEmpty(stringPropertyId))
                        Utility.toast("Please select property type", getActivity());
                    else if (TextUtils.isEmpty(stringOwnerId))
                        Utility.toast("Please select ownership type", getActivity());
                    else if (TextUtils.isEmpty(stringContractorId))
                        Utility.toast("Please select DMA contractor", getActivity());
                    else {
                        if (ftpFileName[0].equalsIgnoreCase("") &&
                                ftpFileName[1].equalsIgnoreCase("") &&
                                ftpFileName[2].equalsIgnoreCase("") &&
                                ftpFileName[3].equalsIgnoreCase("")) {
                            saveRegistrationDetails();
                        } else {
                            ftpDirectory = et_application_no.getTag().toString() + ""
                                    + et_application_no.getText().toString().trim();
                            new uploadFileFTP().execute();
                        }
                    }
                } else {
                    if (et_application_no.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter application no.", getActivity());
                    else if (et_date.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please select application date", getActivity());
                    else if (TextUtils.isEmpty(stringCategoryId))
                        Utility.toast("Please select customer type", getActivity());
                    else if (TextUtils.isEmpty(stringBillingTo))
                        Utility.toast("Please select billing to", getActivity());
                    else if (et_firstname.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter first name", getActivity());
                    else if (et_lastname.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter last name", getActivity());
                    else if (TextUtils.isEmpty(stringDoc1))
                        Utility.toast("Please select document1", getActivity());
                    else if (TextUtils.isEmpty(stringDoc2))
                        Utility.toast("Please select document2", getActivity());
                    else if (TextUtils.isEmpty(stringDoc2))
                        Utility.toast("Please select document2", getActivity());
                    else if (et_address1.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter society name", getActivity());
                    else if (TextUtils.isEmpty(stringState))
                        Utility.toast("Please select state", getActivity());
                    else if (TextUtils.isEmpty(stringCity))
                        Utility.toast("Please select city", getActivity());
                    else if (TextUtils.isEmpty(stringArea))
                        Utility.toast("Please select area", getActivity());
                    else if (et_pincode.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter area pincode", getActivity());
                    else if (et_mobile_no.getText().toString().equalsIgnoreCase(""))
                        Utility.toast("Please enter mobile no.", getActivity());
                    else if (TextUtils.isEmpty(stringPropertyId))
                        Utility.toast("Please select property type", getActivity());
                    else if (TextUtils.isEmpty(stringOwnerId))
                        Utility.toast("Please select ownership type", getActivity());
                    else if (TextUtils.isEmpty(stringContractorId))
                        Utility.toast("Please select DMA contractor", getActivity());
                    else {
                        if (paymentType.equalsIgnoreCase("cheque")) {
                            Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                            intent.putExtra("app_no", et_application_no.getTag().toString() + ""
                                    + et_application_no.getText().toString().trim());
                            intent.putExtra("cust_name", et_firstname.getText().toString().trim() + " " +
                                    et_lastname.getText().toString().trim());
                            startActivity(intent);
                        } else {

                            Dialog dialog2 = new Dialog(getActivity());
                            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog2.setContentView(R.layout.dialog_sucess);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);

                            Button btn_dashboard = (Button) dialog2.findViewById(R.id.btn_dashboard);
                            TextView tv_success_msg = (TextView) dialog2.findViewById(R.id.tv_success_msg);
                            tv_success_msg.setText("Customer Registration Updated Successfully");

                            btn_dashboard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                    getActivity().finish();
                                    Intent i = new Intent(getActivity(), MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            });

                            Window window1 = dialog2.getWindow();
                            WindowManager.LayoutParams wlp1 = window1.getAttributes();
                            wlp1.width = ActionBar.LayoutParams.MATCH_PARENT;
                            wlp1.height = ActionBar.LayoutParams.MATCH_PARENT;
                            window1.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            window1.setAttributes(wlp1);
                            dialog2.show();
                        }
                    }
                }
                break;
            case R.id.img_reg1:
                ll_reg1.setVisibility(View.VISIBLE);
                ll_reg2.setVisibility(View.GONE);
                ll_reg3.setVisibility(View.GONE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(90);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(0);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(0);
                break;
            case R.id.img_reg2:
                ll_reg1.setVisibility(View.GONE);
                ll_reg2.setVisibility(View.VISIBLE);
                ll_reg3.setVisibility(View.GONE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(0);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(90);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(0);
                getState();
                break;
            case R.id.img_reg3:
                ll_reg1.setVisibility(View.GONE);
                ll_reg2.setVisibility(View.GONE);
                ll_reg3.setVisibility(View.VISIBLE);
                img_reg1.setImageResource(R.drawable.right);
                img_reg1.setRotation(0);
                img_reg2.setImageResource(R.drawable.right);
                img_reg2.setRotation(0);
                img_reg3.setImageResource(R.drawable.right);
                img_reg3.setRotation(90);
                getPropertyType();
                getOwnershipType();
                getContractorName();
                break;
            default:
                return;
        }
    }

    private void getCustomerType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CUSTOMER_TYPE,
                    Constant.URL_GET_CUSTOMER_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCustomerCategory() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("type_id", stringTypeId);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CUSTOMER_CATEGORY,
                    Constant.URL_GET_CUSTOMER_CATEGORY, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCorporateName() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("type_id", stringTypeId);
            params.put("category_id", stringCategoryId);
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CORPORATE_NAME,
                    Constant.URL_GET_CORPORATE_NAME, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getBillingInfo() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("category_id", stringCategoryId);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_BILLING_INFO,
                    Constant.URL_GET_BILLING_INFO, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getPropertyType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_PROPERTY_TYPE,
                    Constant.URL_GET_PROPERTY_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getOwnershipType() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_OWNER_TYPE,
                    Constant.URL_GET_OWNER_TYPE);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getContractorName() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("WO_type", "");
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CONTRACTOR,
                    Constant.URL_GET_CONTRACTOR, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getState() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_STATE,
                    Constant.URL_GET_STATE, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getCity() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("state_id", stringState);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_CITY,
                    Constant.URL_GET_CITY, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getArea() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("state_id", stringState);
            params.put("city_id", stringCity);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_AREA,
                    Constant.URL_GET_AREA, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void getDocumentList() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            volleyAPIClass.volleyGetJsonAPI(getActivity(), FragmentCustomerRegistration.this,
                    Constant.GET_DOCUMENT_LIST,
                    Constant.URL_GET_DOCUMENT_LIST);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    private void verifyAppNo() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            Map<String, Object> params = new HashMap<>();
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("application_no", et_application_no.getTag().toString() + ""
                    + et_application_no.getText().toString().trim());
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.VERIFY_APP_NO,
                    Constant.URL_VERIFY_APP_NO, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void vResponse(int reqCode, String result) {
        String response, message;
        JSONObject jsonObjectMessage;
        JSONArray jsonArray;
        try {
            JSONObject jObject = new JSONObject(result);
            if (reqCode == Constant.GET_CUSTOMER_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("type_data");
                    int lenth = jsonArray.length() + 1;
                    type_id = new String[lenth];
                    type_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            type_id[0] = "0";
                            type_name[0] = "Select Customer Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            type_id[a] = jsonObjectMessage.getString("type_id");
                            type_name[a] = jsonObjectMessage.getString("type_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_customer_type, type_name);
                    sp_customer_type.setSelection(1);
                }
            } else if (reqCode == Constant.GET_CUSTOMER_CATEGORY) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("category_data");
                    int lenth = jsonArray.length() + 1;
                    category_id = new String[lenth];
                    category_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            category_id[0] = "0";
                            category_name[0] = "Select Customer Category";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            category_id[a] = jsonObjectMessage.getString("category_id");
                            category_name[a] = jsonObjectMessage.getString("category_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_customer_category, category_name);
                    sp_customer_category.setSelection(2);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                            R.style.AlertDialogTheme);
                    builder.setTitle("Timeout");
                    builder.setMessage("You have to refresh this page due to server timeout.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            if (Build.VERSION.SDK_INT >= 26) {
                                ft.setReorderingAllowed(false);
                            }
                            ft.detach(FragmentCustomerRegistration.this)
                                    .attach(FragmentCustomerRegistration.this).commit();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getFragmentManager().popBackStack();
                        }
                    });
                    builder.show();
                }
            } else if (reqCode == Constant.GET_CORPORATE_NAME) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("corporate_data");
                    int lenth = jsonArray.length() + 1;
                    corporate_id = new String[lenth];
                    corporate_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            corporate_id[0] = "0";
                            corporate_name[0] = "Select Corporate Name";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            corporate_id[a] = jsonObjectMessage.getString("corporate_id");
                            corporate_name[a] = jsonObjectMessage.getString("corporate_name");
                        }
                    }
                    ll_corporate.setVisibility(View.VISIBLE);
                    Utility.setSpinnerAdapter(getActivity(), sp_corporate_name, corporate_name);
                }
            } else if (reqCode == Constant.GET_PROPERTY_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("property_data");
                    int lenth = jsonArray.length() + 1;
                    property_id = new String[lenth];
                    property_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            property_id[0] = "0";
                            property_name[0] = "Select Property Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            property_id[a] = jsonObjectMessage.getString("property_id");
                            property_name[a] = jsonObjectMessage.getString("property_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_property_type, property_name);
                    if (indexPropType != 0) {
                        indexPropType = Arrays.asList(property_id).indexOf(property_type);
                        sp_property_type.setSelection(indexPropType);
                    }
                }
            } else if (reqCode == Constant.GET_OWNER_TYPE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("ownership_data");
                    int lenth = jsonArray.length() + 1;
                    ownership_id = new String[lenth];
                    ownership_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            ownership_id[0] = "0";
                            ownership_name[0] = "Select Owner Type";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            ownership_id[a] = jsonObjectMessage.getString("ownership_id");
                            ownership_name[a] = jsonObjectMessage.getString("ownership_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_owner_type, ownership_name);
                    if (indexOwnerType != 0) {
                        indexOwnerType = Arrays.asList(ownership_id).indexOf(ownership_type);
                        sp_owner_type.setSelection(indexOwnerType);
                    }
                }
            } else if (reqCode == Constant.GET_CONTRACTOR) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("contractor_data");
                    int lenth = jsonArray.length() + 1;
                    contractor_id = new String[lenth];
                    contractor_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            contractor_id[0] = "-1";
                            contractor_name[0] = "Select DMA Contractor";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            contractor_id[a] = jsonObjectMessage.getString("contractor_id");
                            contractor_name[a] = jsonObjectMessage.getString("contractor_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_contractor, contractor_name);

                    if (indexDMA != 0) {
                        indexDMA = Arrays.asList(contractor_id).indexOf(dma_contractor);
                        sp_contractor.setSelection(indexDMA);
                    } else
                        sp_contractor.setSelection(1);
                }
            } else if (reqCode == Constant.GET_BILLING_INFO) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("billing_data");
                    int lenth = jsonArray.length() + 1;
                    billing_id = new String[lenth];
                    billing_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            billing_id[0] = "-1";
                            billing_name[0] = "Select Billing To";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            billing_id[a] = jsonObjectMessage.getString("billing_id");
                            billing_name[a] = jsonObjectMessage.getString("billing_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_billing_to, billing_name);
                    sp_billing_to.setSelection(1);
                }
            } else if (reqCode == Constant.GET_STATE) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("state_data");
                    int lenth = jsonArray.length() + 1;
                    state_id = new String[lenth];
                    state_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            state_id[0] = "0";
                            state_name[0] = "Select State";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            state_id[a] = jsonObjectMessage.getString("state_id");
                            state_name[a] = jsonObjectMessage.getString("state_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_state, state_name);
                    if (indexState != 0) {
                        indexState = Arrays.asList(state_id).indexOf(state);
                        sp_state.setSelection(indexState);
                    } else
                        sp_state.setSelection(1);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
                            R.style.AlertDialogTheme);
                    builder.setTitle("Timeout");
                    builder.setMessage("You have to refresh this page due to server timeout.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            if (Build.VERSION.SDK_INT >= 26) {
                                ft.setReorderingAllowed(false);
                            }
                            ft.detach(FragmentCustomerRegistration.this)
                                    .attach(FragmentCustomerRegistration.this).commit();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getFragmentManager().popBackStack();
                        }
                    });
                    builder.show();
                }
            } else if (reqCode == Constant.GET_CITY) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("city_data");
                    int lenth = jsonArray.length() + 1;
                    city_id = new String[lenth];
                    city_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            city_id[0] = "0";
                            city_name[0] = "Select City";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            city_id[a] = jsonObjectMessage.getString("city_id");
                            city_name[a] = jsonObjectMessage.getString("city_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_city, city_name);
                    if (indexCity != 0) {
                        indexCity = Arrays.asList(city_id).indexOf(city);
                        sp_city.setSelection(indexCity);
                    } else
                        sp_city.setSelection(1);
                }
            } else if (reqCode == Constant.GET_AREA) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("area_data");
                    int lenth = jsonArray.length() + 1;
                    area_id = new String[lenth];
                    area_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            area_id[0] = "0";
                            area_name[0] = "Select Area";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            area_id[a] = jsonObjectMessage.getString("area_id");
                            area_name[a] = jsonObjectMessage.getString("area_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_area, area_name);
                    if (indexArea != 0) {
                        indexArea = Arrays.asList(area_id).indexOf(area);
                        sp_area.setSelection(indexArea);
                    } else
                        sp_area.setSelection(1);
                }
            } else if (reqCode == Constant.GET_DOCUMENT_LIST) {
                response = jObject.getString("response");
                if (response.equalsIgnoreCase("true")) {
                    jsonArray = jObject.getJSONArray("doc_data");
                    int lenth = jsonArray.length() + 1;
                    doc_id = new String[lenth];
                    doc_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            doc_id[0] = "0";
                            doc_name[0] = "Select Document";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            doc_id[a] = jsonObjectMessage.getString("doc_id");
                            doc_name[a] = jsonObjectMessage.getString("doc_name");
                        }
                    }
                    Utility.setSpinnerAdapter(getActivity(), sp_doc1, doc_name);
                    Utility.setSpinnerAdapter(getActivity(), sp_doc2, doc_name);
                }
            } else if (reqCode == Constant.VERIFY_APP_NO) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    Utility.toast(message, getActivity());
                    if (!jObject.getString("application_Id")
                            .equalsIgnoreCase("null")) {
                        application_date = jObject.getString("application_date");
                        customer_type = jObject.getString("customer_type");
                        customer_category = jObject.getString("customer_category");
                        corporation_name = jObject.getString("corporation_name");
                        billing_to = jObject.getString("billing_to");
                        first_name = jObject.getString("first_name");
                        middle_name = jObject.getString("middle_name");
                        last_name = jObject.getString("last_name");
                        dob = jObject.getString("dob");
                        adhar_no = jObject.getString("adhar_no");
                        block_no = jObject.getString("block_no");
                        address = jObject.getString("address");
                        society = jObject.getString("society");
                        landmark = jObject.getString("landmark");
                        state = jObject.getString("state");
                        city = jObject.getString("city");
                        area = jObject.getString("area");
                        pincode = jObject.getString("pincode");
                        customer_mobile = jObject.getString("customer_mobile");
                        customer_email = jObject.getString("customer_email");
                        lpg_cust_no = jObject.getString("lpg_cust_no");
                        lpg_distributor = jObject.getString("lpg_distributor");
                        lpg_omc = jObject.getString("lpg_omc");
                        property_type = jObject.getString("property_type");
                        property_name1 = jObject.getString("property_name");
                        ownership_type = jObject.getString("ownership_type");
                        ownership_name1 = jObject.getString("ownership_name");
                        owner_mobile = jObject.getString("owner_mobile");
                        dma_contractor = jObject.getString("dma_contractor");
                        remarks = jObject.getString("remarks");
                        center_code = jObject.getString("center_code");
                        doc1 = jObject.getString("doc1");
                        doc2 = jObject.getString("doc2");
                        customer_phone = jObject.getString("customer_phone");
                        plan_id = jObject.getString("plan_id");

                        et_date.setText(Utility.parseDate(application_date));
                        et_firstname.setText(first_name);
                        et_middlename.setText(middle_name);
                        et_lastname.setText(last_name);
                        if (!dob.equalsIgnoreCase(""))
                            et_dob.setText(Utility.parseDate(dob));
                        et_aadhar_no.setMaskedText(adhar_no);
                        et_block_no.setText(block_no);
                        et_address1.setText(address + " " + society);
                        et_address2.setText(landmark);
                        et_pincode.setText(pincode);
                        et_mobile_no.setText(customer_mobile);
                        et_contact_no.setText("");
                        et_email.setText(customer_email);
                        et_lpg_no.setText(lpg_cust_no);
                        et_lpg_distributor.setText(lpg_distributor);
                        et_lpg_omc.setText(lpg_omc);
                        et_owner_name.setText(ownership_name1);
                        et_owner_contact_no.setText(owner_mobile);
                        et_remarks.setText(remarks);

                        int indexCustType = Arrays.asList(type_id).indexOf(customer_type);
                        int indexCustCat = Arrays.asList(category_id).indexOf(customer_category);
                        int indexBillingTo = Arrays.asList(billing_id).indexOf(billing_to);
                        int indexDoc1 = Arrays.asList(doc_id).indexOf(doc1);
                        int indexDoc2 = Arrays.asList(doc_id).indexOf(doc2);
                        indexPropType = 1;
                        indexOwnerType = 1;
                        indexDMA = 1;
                        indexState = 1;
                        indexCity = 1;
                        indexArea = 1;

                        sp_customer_type.setSelection(indexCustType);
                        sp_customer_category.setSelection(indexCustCat);
                        sp_billing_to.setSelection(indexBillingTo);
                        sp_doc1.setSelection(indexDoc1);
                        sp_doc2.setSelection(indexDoc2);
//                        sp_state.setSelection(indexState);
//                        sp_city.setSelection(indexCity);
//                        sp_area.setSelection(indexArea);
//                        sp_property_type.setSelection(indexPropType);
//                        sp_owner_type.setSelection(indexOwnerType);
//                        sp_contractor.setSelection(indexDMA);

                        flag = true;
                    } else {
                        et_date.setText("");
                        et_firstname.setText("");
                        et_middlename.setText("");
                        et_lastname.setText("");
                        et_dob.setText("");
                        et_aadhar_no.setMaskedText("");
                        et_block_no.setText("");
                        et_address1.setText("");
                        et_address2.setText("");
                        et_pincode.setText("");
                        et_mobile_no.setText("");
                        et_contact_no.setText("");
                        et_email.setText("");
                        et_lpg_no.setText("");
                        et_lpg_distributor.setText("");
                        et_lpg_omc.setText("");
                        et_owner_name.setText("");
                        et_owner_contact_no.setText("");
                        et_remarks.setText("");

                        indexPropType = 0;
                        indexOwnerType = 0;
                        indexDMA = 0;
                        indexState = 0;
                        indexCity = 0;
                        indexArea = 0;

                        sp_customer_type.setSelection(1);
                        sp_customer_category.setSelection(2);
                        sp_billing_to.setSelection(1);
                        sp_doc1.setSelection(0);
                        sp_doc2.setSelection(0);
                        sp_state.setSelection(1);
                        sp_city.setSelection(1);
                        sp_area.setSelection(0);
                        sp_property_type.setSelection(0);
                        sp_owner_type.setSelection(0);
                        sp_contractor.setSelection(1);
                    }

                    /*jsonArray = jObject.getJSONArray("document_data");
                    int lenth = jsonArray.length() + 1;
                    doc_id = new String[lenth];
                    doc_name = new String[lenth];
                    for (int a = 0; a < lenth; a++) {
                        if (a == 0) {
                            doc_id[0] = "0";
                            doc_name[0] = "Select Document";
                        } else {
                            jsonObjectMessage = jsonArray.getJSONObject(a - 1);
                            doc_id[a] = jsonObjectMessage.getString("doc_id");
                            doc_name[a] = jsonObjectMessage.getString("doc_name");
                        }
                    }*/
                } else {
                    Utility.toast(message, getActivity());
                }
            } else if (reqCode == Constant.SAVE_REGISTRATION) {
                response = jObject.getString("response");
                message = jObject.getString("message");
                if (response.equalsIgnoreCase("true")) {
                    if (paymentType.equalsIgnoreCase("cheque")) {
                        Utility.toast(message, getActivity());
                        Intent intent = new Intent(getActivity(), PaymentDetailActivity.class);
                        intent.putExtra("app_no", et_application_no.getTag().toString() + ""
                                + et_application_no.getText().toString().trim());
                        intent.putExtra("cust_name", et_firstname.getText().toString().trim() + " " +
                                et_lastname.getText().toString().trim());
                        startActivity(intent);
                    } else {
                        Dialog dialog1 = new Dialog(getActivity());
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog1.setContentView(R.layout.dialog_sucess);
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.setCancelable(false);

                        Button btn_dashboard = (Button) dialog1.findViewById(R.id.btn_dashboard);
                        TextView tv_success_msg = (TextView) dialog1.findViewById(R.id.tv_success_msg);
                        tv_success_msg.setText("Customer Registration Saved Successfully");

                        btn_dashboard.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                                getActivity().finish();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });

                        Window window1 = dialog1.getWindow();
                        WindowManager.LayoutParams wlp1 = window1.getAttributes();
                        wlp1.width = ActionBar.LayoutParams.MATCH_PARENT;
                        wlp1.height = ActionBar.LayoutParams.MATCH_PARENT;
                        window1.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        window1.setAttributes(wlp1);
                        dialog1.show();
                    }
                } else {
                    Utility.toast(message, getActivity());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void vErrorMsg(int reqCode, String error) {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void saveRegistrationDetails() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();

            JSONObject obj = null;
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < ftpFileName.length; i++) {
                    if (!ftpFileName[i].equalsIgnoreCase("")) {
                        obj = new JSONObject();
                        obj.put("name", ftpFileName[i]);
                        jsonArray.put(obj);
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("user_id", Utility.getAppPrefString(getActivity(), Constant.USER_ID));
            params.put("center_code", Utility.getAppPrefString(getActivity(), "center_code"));
            params.put("application_no", et_application_no.getTag().toString() + ""
                    + et_application_no.getText().toString().trim());
            params.put("application_date", et_date.getText().toString().trim());
            params.put("customer_type", stringTypeId);
            params.put("customer_category", stringCategoryId);
            params.put("corporation_name", stringCorporateId);
            params.put("billing_to", stringBillingTo);
            params.put("first_name", et_firstname.getText().toString());
            params.put("middle_name", et_middlename.getText().toString());
            params.put("last_name", et_lastname.getText().toString());
            params.put("dob", et_dob.getText().toString().trim());
            params.put("adhar_no", et_aadhar_no.getUnmaskedText());
            params.put("block_no", et_block_no.getText().toString().trim());
            params.put("address", "");
            params.put("society", et_address1.getText().toString());
            params.put("landmark", et_address2.getText().toString());
            params.put("state", stringState);
            params.put("city", stringCity);
            params.put("area", stringArea);
            params.put("pincode", et_pincode.getText().toString());
            params.put("customer_phone", et_contact_no.getText().toString().trim());
            params.put("customer_mobile", et_mobile_no.getText().toString().trim());
            params.put("customer_email", et_email.getText().toString().trim());
            params.put("lpg_cust_no", et_lpg_no.getText().toString().trim());
            params.put("lpg_distributor", et_lpg_distributor.getText().toString().trim());
            params.put("lpg_omc", et_lpg_omc.getText().toString().trim());
            params.put("property_type", stringPropertyId);
            params.put("property_name", et_property_name.getText().toString().trim());
            params.put("ownership_type", stringOwnerId);
            params.put("ownership_name", et_owner_name.getText().toString().trim());
            params.put("owner_mobile", et_owner_contact_no.getText().toString().trim());
            params.put("dma_contractor", stringContractorId);
            params.put("remarks", et_remarks.getText().toString());
            params.put("doc1", stringDoc1);
            params.put("doc2", stringDoc2);
            params.put("document_data", jsonArray);
            params.put("payment_mode", paymentType);
            volleyAPIClass.volleyAPICall(getActivity(), FragmentCustomerRegistration.this,
                    Constant.SAVE_REGISTRATION,
                    Constant.URL_SAVE_REGISTRATION, params);
        } else
            Utility.toast("No Internet Connection", getActivity());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (adapterView.getId()) {
            case R.id.sp_cust_type:
                if (!type_id[position].equalsIgnoreCase("0")) {
                    stringTypeId = type_id[position];
                    getCustomerCategory();
                }
                break;
            case R.id.sp_cust_category:
                if (!category_id[position].equalsIgnoreCase("0")) {
                    stringCategoryId = category_id[position];
                    if (category_name[position].equalsIgnoreCase("corporate"))
                        getCorporateName();
                    else
                        ll_corporate.setVisibility(View.GONE);
                    getBillingInfo();
                }
                break;
            case R.id.sp_corporate_name:
                if (!corporate_id[position].equalsIgnoreCase("0")) {
                    stringCorporateId = corporate_id[position];
                }
                break;
            case R.id.sp_property_type:
                if (!property_id[position].equalsIgnoreCase("0")) {
                    stringPropertyId = property_id[position];
                    property_type = stringPropertyId;
                    indexPropType = 1;
                }
                break;
            case R.id.sp_owner_type:
                if (!ownership_id[position].equalsIgnoreCase("0")) {
                    stringOwnerId = ownership_id[position];
                    ownership_type = stringOwnerId;
                    indexOwnerType = 1;
                }
                break;
            case R.id.sp_contractor:
                if (!contractor_id[position].equalsIgnoreCase("-2")) {
                    stringContractorId = contractor_id[position];
                    dma_contractor = stringContractorId;
                    indexDMA = 1;
                }
                break;
            case R.id.sp_reg_state:
                if (!state_id[position].equalsIgnoreCase("0")) {
                    stringState = state_id[position];
                    state = stringState;
                    indexState = 1;
                    getCity();
                }
                break;
            case R.id.sp_reg_city:
                if (!city_id[position].equalsIgnoreCase("0")) {
                    stringCity = city_id[position];
                    city = stringCity;
                    indexCity = 1;
                    getArea();
                }
                break;
            case R.id.sp_reg_area:
                if (!area_id[position].equalsIgnoreCase("0")) {
                    stringArea = area_id[position];
                    area = stringArea;
                    indexArea = 1;
                }
                break;
            case R.id.sp_doc1:
                if (!doc_id[position].equalsIgnoreCase("0")) {
                    stringDoc1 = doc_id[position];
                    if (stringDoc1.equalsIgnoreCase(stringDoc2)) {
                        stringDoc1 = "0";
                        sp_doc1.setSelection(0);
                        Utility.toast("Please select different document", getActivity());
                    }
                }
                break;
            case R.id.sp_doc2:
                if (!doc_id[position].equalsIgnoreCase("0")) {
                    stringDoc2 = doc_id[position];
                    if (stringDoc2.equalsIgnoreCase(stringDoc1)) {
                        stringDoc2 = "0";
                        sp_doc2.setSelection(0);
                        Utility.toast("Please select different document", getActivity());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK) {
                    try {
                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().managedQuery(mCapturedImageURI, projection, null,
                                null, null);
                        int column_index_data = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String capturedImageFilePath = cursor.getString(column_index_data);
                        Bitmap bitmap = BitmapFactory.decodeFile(capturedImageFilePath);
                        File file = new File(filePath);
                        System.out.println(file.getName());

                        File f = new File(getActivity().getCacheDir(), filePath);
                        f.createNewFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                        if (imageType == 1) {
                            if (ll_attachment1.getVisibility() == View.VISIBLE) {
                                ftpFileName[2] = file.getName();
                                fileImage[2] = f;
                                ll_attachment12.setVisibility(View.VISIBLE);
                                tv_attachment_name12.setText(file.getName());
                            } else {
                                ftpFileName[0] = file.getName();
                                fileImage[0] = f;
                                ll_attachment1.setVisibility(View.VISIBLE);
                                tv_attachment_name1.setText(file.getName());
                            }
                        } else if (imageType == 2) {
                            if (ll_attachment2.getVisibility() == View.VISIBLE) {
                                ftpFileName[3] = file.getName();
                                fileImage[3] = f;
                                ll_attachment22.setVisibility(View.VISIBLE);
                                tv_attachment_name22.setText(file.getName());
                            } else {
                                ftpFileName[1] = file.getName();
                                fileImage[1] = f;
                                ll_attachment2.setVisibility(View.VISIBLE);
                                tv_attachment_name2.setText(file.getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 102:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                        String ext = picturePath.substring(picturePath.lastIndexOf("."));
                        String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        filePath = Utility.getTimeStamp() + ext;

                        File file = new File(filePath);
                        System.out.println(file.getName());

                        File f = new File(getActivity().getCacheDir(), filePath);
                        f.createNewFile();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        FileOutputStream fos = new FileOutputStream(f);
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();
                        if (imageType == 1) {
                            if (ll_attachment1.getVisibility() == View.VISIBLE) {
                                ftpFileName[1] = file.getName();
                                fileImage[1] = f;
                                ll_attachment12.setVisibility(View.VISIBLE);
                                tv_attachment_name12.setText(file.getName());
                            } else {
                                ftpFileName[0] = file.getName();
                                fileImage[0] = f;
                                ll_attachment1.setVisibility(View.VISIBLE);
                                tv_attachment_name1.setText(file.getName());
                            }
                        } else if (imageType == 2) {
                            if (ll_attachment2.getVisibility() == View.VISIBLE) {
                                ftpFileName[4] = file.getName();
                                fileImage[4] = f;
                                ll_attachment22.setVisibility(View.VISIBLE);
                                tv_attachment_name22.setText(file.getName());
                            } else {
                                ftpFileName[3] = file.getName();
                                fileImage[3] = f;
                                ll_attachment2.setVisibility(View.VISIBLE);
                                tv_attachment_name2.setText(file.getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!Utility.getAppPrefString(getActivity(), "uid")
                .equalsIgnoreCase("")) {
            et_aadhar_no.setMaskedText(Utility.getAppPrefString(getActivity(), "uid"));
        }
    }

    public class uploadFileFTP extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            if (!mProgressDialog.isShowing())
                mProgressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            try {
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(Constant.FTP_URL);
                ftpClient.login(Constant.FTP_USERNAME, Constant.FTP_PASSWORD);
                ftpClient.changeWorkingDirectory(Constant.FTP_FOLDER + "/Registration_Documents/");
                Log.e("FTP Dir1: ", ftpClient.printWorkingDirectory());
                ftpClient.makeDirectory(ftpDirectory);
                ftpClient.changeWorkingDirectory(ftpDirectory + "/");
                Log.e("FTP Dir2: ", ftpClient.printWorkingDirectory());

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                for (int i = 0; i < fileImage.length; i++) {
                    if (fileImage[i] != null) {
                        BufferedInputStream buffIn = null;
                        System.out.println("Name : " + ftpFileName[i]);
                        buffIn = new BufferedInputStream(new FileInputStream(fileImage[i]));
                        ftpClient.storeFile(ftpFileName[i], buffIn);
                        buffIn.close();
                    }
                }
                ftpClient.logout();
                ftpClient.disconnect();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            Log.v("FTP", "Successfully");
            saveRegistrationDetails();
        }
    }
}