package com.example.baoxie.tips;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private String[][] drinkInfo;
    private String type;


    public CustomAdapter(ArrayList<String> list, Context context,String[][] drinkInfo,String type) {
        this.list = list;
        this.context = context;
        this.drinkInfo = drinkInfo;
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, null);
        }

        //Handle TextView and display string from your list
        EditText listItemText = (EditText)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));
        listItemText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                list.set(position,arg0.toString());
            }
        });

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button moreBtn = (Button)view.findViewById(R.id.more_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String drink_name = (String) list.get(position);
                for(int i =0 ; i< drinkInfo.length ; i++){
                    if(drinkInfo[i][0] == drink_name){
                        Intent in = new Intent(context, StaticAddRecipeActivity.class);
                        in.putExtra("instructions", drinkInfo[i]);
                        in.putExtra("type",type);
                        context.startActivity(in);
                        return;
                    }
                }
                String[] temp = {drink_name,"","",""};
                Intent in = new Intent(context, StaticAddRecipeActivity.class);
                in.putExtra("instructions", temp);
                in.putExtra("type",type);
                context.startActivity(in);
            }
        });

        return view;
    }
}
