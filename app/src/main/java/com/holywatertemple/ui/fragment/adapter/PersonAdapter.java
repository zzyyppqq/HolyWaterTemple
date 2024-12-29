package com.holywatertemple.ui.fragment.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.holywatertemple.R;
import com.holywatertemple.db.model.PersonData;
import com.holywatertemple.java_lib.ExcelUtil;
import com.holywatertemple.util.AppSharePref;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mBtnClickListener;
    private String like;

    public void addItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
    public void addBtnClickListener(OnItemClickListener listener) {
        this.mBtnClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, PersonData personData);
    }

    private List<PersonData> mList = new ArrayList<>();

    public void setDatas(List<PersonData> datas, String like) {
        this.like = like;
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    public List<PersonData> getDatas() {
        return mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_persion, parent, false);
        return new PersonViewHolder(view);
    }

    private static final long ONE_YEAR_MS = 360 * 24 * 60 * 60 * 1000;
    private static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PersonViewHolder) {
            PersonViewHolder personViewHolder = (PersonViewHolder) holder;
            final PersonData personData = mList.get(position);
            StringBuffer sb = new StringBuffer();

            final int remainDay = personData.getRemainDay();

            if (TextUtils.isEmpty(personData.getName())) {
                personViewHolder.tvRemainDay.setText("未供养");
                personViewHolder.btSendSms.setVisibility(View.GONE);
            } else {
                personViewHolder.tvRemainDay.setText(remainDay + "天");
                if (remainDay < AppSharePref.getInstance().getSendSmsDay()) {
                    personViewHolder.btSendSms.setVisibility(View.VISIBLE);
                }else {
                    personViewHolder.btSendSms.setVisibility(View.GONE);
                }
            }


            sb.append("序         号：" + personData.getJossId()).append("\r\n")
                    .append("姓         名：" + personData.getName()).append("\r\n")
                    .append("供养时间：" + personData.getFendTime()).append("\r\n")
                    .append("续费时间：" + personData.getExtendTime()).append("\r\n")
                    .append("佛像种类：" + personData.getJossType()).append("\r\n")
                    .append("手机号码：" + personData.getPhoneNum()).append("\r\n")
                    .append("供养金额：" + personData.getFendPrice());


            SpannableString highLightKeyWord = getHighLightKeyWord(Color.parseColor("#FF4081"), sb.toString(), like);

            personViewHolder.tvContent.setText(highLightKeyWord);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(holder.itemView, position, personData);
                    }
                }
            });

            personViewHolder.btSendSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBtnClickListener != null){
                        mBtnClickListener.onItemClick(holder.itemView, position, personData);
                    }
                }
            });
        }
    }

    public static SpannableString getHighLightKeyWord(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(keyword);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvRemainDay;
        Button btSendSms;

        public PersonViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvRemainDay = itemView.findViewById(R.id.tv_remain_day);
            btSendSms = itemView.findViewById(R.id.bt_send_sms);

        }
    }
}
