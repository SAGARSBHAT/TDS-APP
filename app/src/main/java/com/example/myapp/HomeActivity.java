package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private FloatingActionButton fabbtn;
    private RecyclerView recycle;

    private String postkey;
    private String name;
    private String detail;

    //Firebase
    private FirebaseAuth mauth;
    private DatabaseReference mdata;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        mauth=FirebaseAuth.getInstance();
        FirebaseUser muser=mauth.getCurrentUser();
        String uid=muser.getUid();

        mdata= FirebaseDatabase.getInstance().getReference().child("All data").child(uid);


        recycle=findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));



        
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TDS APP");

        fabbtn=findViewById(R.id.fabadd);
        fabbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adddata();

            }
        });
        
        
        
        
    }

private void Adddata() {
    AlertDialog.Builder mydia = new AlertDialog.Builder(this);
    LayoutInflater inflater = LayoutInflater.from(this);
    View myview = inflater.inflate(R.layout.input, null);

    mydia.setView(myview);
    final AlertDialog dialog = mydia.create();
    dialog.setCancelable(false);
    final EditText name = myview.findViewById(R.id.name);
    final EditText details = myview.findViewById(R.id.detail);
    Button btncan = myview.findViewById(R.id.btncancel);
    Button btnsave = myview.findViewById(R.id.btnsave);
    btncan.setOnClickListener((new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();

        }
    }));
    btnsave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mn = name.getText().toString().trim();
            String md = details.getText().toString().trim();
            if (TextUtils.isEmpty(mn)) {
                name.setError("Required Field");
                return;
            }
            if (TextUtils.isEmpty(md)) {
                details.setError("Required Field");
                return;
            }

            String mid = mdata.push().getKey();
            String mdate = DateFormat.getDateInstance().format(new Date());

            Data data = new Data(mn, md, mid, mdate);

            mdata.child(mid).setValue(data);
            Toast.makeText(getApplicationContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();


            dialog.dismiss();

        }
    });
    dialog.show();

}


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,MyViewHolder>adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(
                Data.class,
                R.layout.itemlayout,
                MyViewHolder.class,
                mdata

        ) {
            @Override
            protected void populateViewHolder(MyViewHolder myViewHolder, final Data data, final int i) {

        myViewHolder.setName(data.getName());
        myViewHolder.setDetails(data.getDetail());
        myViewHolder.setdate(data.getDate());

        myViewHolder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postkey=getRef(i).getKey();
                name=data.getName();
                detail=data.getDetail();
                updateData();



            }
        });
            }
        };


        recycle.setAdapter(adapter);
    }



    public  static class MyViewHolder extends RecyclerView.ViewHolder {


        View mview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }


        public void setName(String name) {
            TextView mname = mview.findViewById(R.id.nameid);
            mname.setText(name);

        }

        public void setDetails(String details) {
            TextView mdetails = mview.findViewById(R.id.detailid);
            mdetails.setText(details);

        }

        public void setdate(String date) {
            TextView mdate = mview.findViewById(R.id.date);
            mdate.setText(date);
        }

    }

    public void updateData(){
        AlertDialog.Builder mydia=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View myview=inflater.inflate(R.layout.updatedata,null);
        mydia.setView(myview);
        final AlertDialog dialog=mydia.create();
        dialog.show();

         final EditText mn=myview.findViewById(R.id.name);
        final EditText md=myview.findViewById(R.id.detail);



        mn.setText(name);
        mn.setSelection(name.length());

        md.setText(detail);
        md.setSelection(detail.length());


        Button del=myview.findViewById(R.id.btndelete);
        Button update=myview.findViewById(R.id.btnupdate);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdata.child(postkey).removeValue();
                dialog.dismiss();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name= mn.getText().toString().trim();
                detail= md.getText().toString().trim();

                String mdate=DateFormat.getDateInstance().format(new Date());
                Data data=new Data(name,detail,postkey,mdate);

                mdata.child(postkey).setValue(data);


                dialog.dismiss();

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
            mauth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
















