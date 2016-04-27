package com.ingentive.nahad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.MenuAdapter;
import com.ingentive.nahad.model.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends Activity {

    private RelativeLayout tv_institute_handbook_layout, fabb_layout, resources_layout,
            white_papers_layout, glossary_lauout, visit_website_layout;
    private TextView tvSelectedLayoutName;
    private ListView listView;

    List<MenuModel> itemList;// {"Milk", "Butter", "Yogurt", "Toothpaste", "Ice Cream"};
    MenuModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initialize();
    }

    public void initialize() {
        tv_institute_handbook_layout = (RelativeLayout) findViewById(R.id.tv_institute_handbook_layout);
        fabb_layout = (RelativeLayout) findViewById(R.id.fabb_layout);
        resources_layout = (RelativeLayout) findViewById(R.id.resources_layout);
        white_papers_layout = (RelativeLayout) findViewById(R.id.white_papers_layout);
        glossary_lauout = (RelativeLayout) findViewById(R.id.glossary_lauout);
        visit_website_layout = (RelativeLayout) findViewById(R.id.visit_website_layout);

        tvSelectedLayoutName = (TextView) findViewById(R.id.tv_selected);
        listView = (ListView) findViewById(R.id.listview);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, itemList);
//        listView.setAdapter(adapter);

        tv_institute_handbook_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("Institute Handbook");

                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                model.setItem("Institute Handbook");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);

            }
        });
        fabb_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("Fabrication Guides");
                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                model.setItem("Fabrication Guides");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);
            }
        });
        resources_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("Resourcess");

                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                model.setItem("Resourcess");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);
            }
        });
        white_papers_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("White Papers");
                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                model.setItem("White Papers");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);
            }
        });
        glossary_lauout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("Glossary");
                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                model.setItem("Glossary");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);
            }
        });
        visit_website_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelectedLayoutName.setText("Visit Website");
                model = new MenuModel();
                itemList = new ArrayList<MenuModel>();
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                model.setItem("Visit Website");
                itemList.add(model);
                MenuAdapter mAdapter = new MenuAdapter(MenuActivity.this, itemList, R.layout.custom_row_menu);
                listView.setAdapter(mAdapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MenuActivity.this, ContentsActivity.class);
                i.putExtra("itemname", itemList.get(position).getItem());
                startActivity(i);
            }
        });
    }
}
