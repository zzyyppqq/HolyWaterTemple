package com.holywatertemple.ui.fragment.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holywatertemple.R;
import com.holywatertemple.db.model.PersonData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private OnItemClickListener listener;
    private String like;

    public void addItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PersonViewHolder) {
            PersonViewHolder personViewHolder = (PersonViewHolder) holder;
            final PersonData personData = mList.get(position);
            StringBuffer sb = new StringBuffer();
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
                    if (listener != null) {
                        listener.onItemClick(holder.itemView, position, personData);
                    }
                }
            });
        }
    }

    public static SpannableString getHighLightKeyWord(int color, String text,String keyword) {
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
        @BindView(R.id.tv_content)
        TextView tvContent;

        public PersonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
