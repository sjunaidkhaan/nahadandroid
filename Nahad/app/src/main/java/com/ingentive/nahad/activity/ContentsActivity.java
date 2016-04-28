package com.ingentive.nahad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.adapter.ContentsAdapter;
import com.ingentive.nahad.model.ContentsChildModel;
import com.ingentive.nahad.model.ContentsParentModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContentsActivity extends Activity {

    private TextView tvContentsHeader;
    private ExpandableListView expandableListView;
    private ArrayList<ContentsParentModel> deptList = new ArrayList<ContentsParentModel>();
    ContentsAdapter adapter;
    List<ContentsParentModel> parentModelsList = new ArrayList<ContentsParentModel>();
    List<ContentsChildModel> childModelsList = new ArrayList<ContentsChildModel>();
    //ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ContentsParentModel contentsParentModel;
    ContentsChildModel contentsChildModel;
    private File[] filelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        initialize();
    }

    protected void initialize() {
        tvContentsHeader = (TextView) findViewById(R.id.tv_contents_header);
        expandableListView = (ExpandableListView) findViewById(R.id.exp_lv);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        if (b != null) {
            String itemname = b.get("itemname").toString();
            tvContentsHeader.setText("" + itemname);
        }
//        list.setGroupIndicator(null);
//        list.setChildIndicator(null);
        String[] titles = {"A", "B", "C"};
        String[] fruits = {"a1", "a2"};
        String[] veggies = {"b1", "b2", "b3"};
        String[] meats = {"c1", "c2"};
        // String[][] contents = {fruits, veggies, meats};

        for (int i = 0; i < titles.length; i++) {
            contentsParentModel = new ContentsParentModel();
            contentsParentModel.setParentItemText(titles[i].toString());
            int c = 0;
            if (i == 0) {
                childModelsList = new ArrayList<ContentsChildModel>();
                for (int j = 0; j < fruits.length; j++) {
                    c++;
                    contentsChildModel = new ContentsChildModel();
                    contentsChildModel.setSequence("" + c);
                    contentsChildModel.setChildItemText(fruits[j]);
                    childModelsList.add(contentsChildModel);
                }
                //childModelsList.add(contentsChildModel);
                contentsParentModel.setArrayChildren(childModelsList);
            }
            if (i == 1) {
                int cd = 0;
                childModelsList = new ArrayList<ContentsChildModel>();
                for (int j = 0; j < veggies.length; j++) {
                    cd++;
                    contentsChildModel = new ContentsChildModel();
                    contentsChildModel.setSequence("" + cd);
                    contentsChildModel.setChildItemText(veggies[j]);
                    childModelsList.add(contentsChildModel);

                }
                childModelsList.add(contentsChildModel);
                contentsParentModel.setArrayChildren(childModelsList);
            }
            if (i == 2) {
                childModelsList = new ArrayList<ContentsChildModel>();
                int cdd = 0;
                for (int j = 0; j < meats.length; j++) {
                    contentsChildModel = new ContentsChildModel();
                    cdd++;
                    contentsChildModel.setSequence("" + cdd);
                    contentsChildModel.setChildItemText(meats[j]);
                    childModelsList.add(contentsChildModel);
                }
            }
            contentsParentModel.setArrayChildren(childModelsList);
            parentModelsList.add(contentsParentModel);
        }
        adapter = new ContentsAdapter(this, parentModelsList);

        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
        expandAll();

    }

    private void expandAll() {
        for (int i = 0; i < adapter.getGroupCount(); i++)
            expandableListView.expandGroup(i);
    }
}
