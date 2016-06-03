package com.ingentive.nahad.adapter;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingentive.nahad.R;
import com.ingentive.nahad.model.AddFilesModel;
import com.ingentive.nahad.model.GlossaryModel;

import java.util.List;

/**
 * Created by PC on 09-05-2016.
 */
public class GlossaryAdapter extends BaseAdapter {

    private ProgressDialog pDialog;
    private List<GlossaryModel> itemList;
    private int res;
    private Context mContext;
    private static LayoutInflater inflater = null;
    private String alphabet;

    public GlossaryAdapter(Context context, List<GlossaryModel> txtData, int rowId,String alphabet) {
        this.mContext = context;
        this.res = rowId;
        this.itemList = txtData;
        this.alphabet=alphabet;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return this.itemList.size();
    }
    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(final int postion, View rowView, ViewGroup parent) {

        View vi = rowView;
        ViewHolder vh = new ViewHolder();

        if (vi == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.custom_row_glossary, parent, false);
            vh.tvWord = (TextView) vi.findViewById(R.id.tv_word);
            vh.tvDefinition = (TextView) vi.findViewById(R.id.tv_definition);
            vh.row_layout = (LinearLayout) vi.findViewById(R.id.row_layout);
            int id = vi.generateViewId();
            vi.setId(id);
            vi.setTag(vh);
        } else {
            vh = (ViewHolder) vi.getTag();
        }
        final TextView tvItem;
        tvItem = vh.tvWord;
        if(postion%2==0){
            vh.row_layout.setBackgroundColor(Color.WHITE);
        }else {
            vh.row_layout.setBackgroundColor(Color.LTGRAY); //Lavender
        }
        vh.tvWord.setText(itemList.get(postion).getWord());
        vh.tvDefinition.setText(itemList.get(postion).getDefinition());

//        if(itemList.get(postion).getAlphabet().equals(alphabet) && alphabet.equals("A")) {
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet) && alphabet.equals("B")) {
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("C")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("D")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("E")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("F")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("G")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("H")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("I")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("J")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("K")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("L")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("M")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("N")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("O")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("P")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("Q")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("R")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("S")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("T")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("U")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("V")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("W")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("X")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("Y")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }else if(itemList.get(postion).getAlphabet().equals(alphabet)&&alphabet.equals("Z")){
//            vh.tvWord.setText(itemList.get(postion).getWord());
//            vh.tvDefinition.setText(itemList.get(postion).getDefinition());
//        }




        return vi;
    }

    public class ViewHolder {
        TextView tvWord,tvDefinition;
        LinearLayout row_layout;
    }
}
