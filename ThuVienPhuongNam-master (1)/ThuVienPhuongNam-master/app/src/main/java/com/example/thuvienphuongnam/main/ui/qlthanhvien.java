package com.example.thuvienphuongnam.main.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.thuvienphuongnam.DAO.PhieuMuonDAO;
import com.example.thuvienphuongnam.DAO.ThanhVienDAO;
import com.example.thuvienphuongnam.DAO.ThuThuDAO;
import com.example.thuvienphuongnam.R;
import com.example.thuvienphuongnam.adapter.AdapterThanhVien;
import com.example.thuvienphuongnam.adapter.AdapterThuThu;
import com.example.thuvienphuongnam.databinding.FragmentQlthanhvienBinding;
import com.example.thuvienphuongnam.model.PhieuMuon;
import com.example.thuvienphuongnam.model.ThanhVien;
import com.example.thuvienphuongnam.model.ThuThu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class qlthanhvien extends Fragment {

    private FragmentQlthanhvienBinding binding;

    FloatingActionButton fab;
    ThanhVienDAO dao;
    ListView listView;
    List<ThanhVien> list;
    ThanhVien thanhVien;
    AdapterThanhVien adapterThanhVien;
    int a;
    int temp=0;

    EditText txtnameuser, txtname, txtpass;
    TextInputLayout tilusername, tilname,tilpass;

    List<PhieuMuon> phieuMuonList;
    PhieuMuonDAO phieuMuonDAO;


    public View onCreateView(LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentQlthanhvienBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        fab = root.findViewById(R.id.qlthanhvien_fab);
        listView = root.findViewById(R.id.qlthanhvien_listview);

        phieuMuonList = new ArrayList<>();
        phieuMuonDAO = new PhieuMuonDAO(getActivity());

        loadTable();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=-1;
                openDialog(Gravity.CENTER);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                a = i;
                openDialog(Gravity.CENTER);
            }
        });

    return root;
    }

    private void loadTable(){
        dao = new ThanhVienDAO(getActivity());
        list = dao.getAll();
        adapterThanhVien = new AdapterThanhVien(getActivity(),R.layout.item_lv_addtt,list);
        listView.setAdapter(adapterThanhVien);
    }


    private void openDialog(int gravity){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_themtt);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        TextView tvTile = (TextView) dialog.findViewById(R.id.item_tvtile);

        txtnameuser = dialog.findViewById(R.id.item_txtnameuser);
        txtname = dialog.findViewById(R.id.item_txtname);
        txtpass = dialog.findViewById(R.id.item_txtpass);

        tilusername = dialog.findViewById(R.id.add_til_username);
        tilname = dialog.findViewById(R.id.add_til_name);
        tilpass = dialog.findViewById(R.id.add_til_pass);

        Button btnadd = dialog.findViewById(R.id.dialog_add_add);
        Button btncancel = dialog.findViewById(R.id.dialog_add_cancel);

        dao = new ThanhVienDAO(getActivity());
        txtpass.setInputType(InputType.TYPE_CLASS_NUMBER);

        if (a==-1){
            tvTile.setText("TH??M TH??NH VI??N");

            tilusername.setHint("M?? Th??nh Vi??n");
            tilname.setHint("T??n Th??nh Vi??n");
            tilpass.setHint("N??m Sinh Th??nh Vi??n");

            txtnameuser.setEnabled(false);

            if (list.size()==0){
                txtnameuser.setText("1");
            }else {
                thanhVien = dao.getAll().get(list.size() - 1);
                txtnameuser.setText(String.valueOf(thanhVien.maTV + 1));
            }

            btnadd.setOnClickListener(new View.OnClickListener() {
                ThanhVien thanhVien = new ThanhVien();
                @Override
                public void onClick(View view) {
                    validate();
                    if (temp==0){
                        thanhVien.hoTen = txtname.getText().toString();
                        thanhVien.namSinh = txtpass.getText().toString();
                        if (dao.insert(thanhVien)>0){
                            Toast.makeText(getActivity(), "Th??m th??nh c??ng", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadTable();
                        }else{
                            Toast.makeText(getActivity(), "Th??m th???t b???i", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        temp=0;
                    }

                }
            });
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Hu??? th??m", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }else{
            tvTile.setText("S???a/X??a th??nh vi??n");

            tilusername.setHint("M?? Th??nh Vi??n");
            tilname.setHint("T??n Th??nh Vi??n");
            tilpass.setHint("N??m Sinh Th??nh Vi??n");

            btnadd.setText("S???a");
            btncancel.setText("Xo??");

            thanhVien = dao.getAll().get(a);

            txtnameuser.setText(String.valueOf(thanhVien.maTV));
            txtnameuser.setEnabled(false);
            txtname.setText(thanhVien.hoTen);
            txtpass.setText(thanhVien.namSinh);

            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validate();
                    if (temp==0){
                        thanhVien = new ThanhVien();
                        thanhVien.maTV = Integer.parseInt(txtnameuser.getText().toString());
                        thanhVien.hoTen = txtname.getText().toString();
                        thanhVien.namSinh = txtpass.getText().toString();
                        if (dao.update(thanhVien)<0){
                            Toast.makeText(getActivity(), "S???a th???t b???i", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "S???a th??nh c??ng", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadTable();
                        }
                    }else {
                        temp=0;
                    }
                }
            });
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    phieuMuonList = phieuMuonDAO.getAll();

                    for (int i = 0; i <phieuMuonList.size(); i++) {
                        if (phieuMuonList.get(i).maTV == thanhVien.maTV){
                            temp++;
                            Toast.makeText(getActivity(), "Kh??ng th??? xo?? th??nh vi??n c?? trong phi???u m?????n", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (temp==0){
                        if (dao.delete(String.valueOf(thanhVien.maTV))<0){
                            Toast.makeText(getActivity(), "Xo?? th???t b???i", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Xo?? th??nh c??ng", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadTable();
                        }
                    }
                }
            });
        }
        dialog.show();
    }

    private void validate(){
        if(txtname.getText().length()==0){
            tilname.setError("T??n th??nh vi??n kh??ng ???????c ????? tr???ng");
            temp++;
        }else{
            tilname.setError("");
        }
        if(txtpass.getText().length()==0){
            tilpass.setError("N??m Sinh kh??ng ???????c ????? tr???ng");
            temp++;
        }else{
            tilpass.setError("");
        }
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}