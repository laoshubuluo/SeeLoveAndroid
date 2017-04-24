package com.tianyu.seelove.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tianyu.seelove.R;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.model.entity.user.SLUserDetail;
import com.tianyu.seelove.model.enums.SexType;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.ui.activity.video.VideoPlayActivity;
import com.tianyu.seelove.utils.ImageLoaderUtil;
import com.tianyu.seelove.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频显示自定义adapter
 * @author shisheng.zhao
 * @date 2017-03-31 17:50
 */
public class FindUserListAdapter extends BaseAdapter {
    String videoUrl = "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4";
    private Context mContext;
    private List<SLUserDetail> slUserDetailList;

    public FindUserListAdapter(Context context, List<SLUserDetail> slUserDetailList) {
        if (null == slUserDetailList) {
            slUserDetailList = new ArrayList<>();
        }
        this.mContext = context;
        this.slUserDetailList = slUserDetailList;
    }

    public void updateData(List<SLUserDetail> slUserDetails, boolean isClean) {
        if (null == slUserDetails) {
            slUserDetails = new ArrayList<>();
        }
        if (isClean) {
            slUserDetailList.clear();
            slUserDetailList = slUserDetails;
        } else {
            slUserDetailList.addAll(slUserDetails);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return slUserDetailList.size();
    }

    @Override
    public Object getItem(int index) {
        return slUserDetailList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_find_user, null);
            viewHolder = new ViewHolder();
            viewHolder.itemLayout = (LinearLayout) convertView.findViewById(R.id.itemLayout);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.cityName);
            viewHolder.userAge = (TextView) convertView.findViewById(R.id.userAge);
            viewHolder.sexImg = (ImageView) convertView.findViewById(R.id.sexImg);
            viewHolder.videoView = convertView.findViewById(R.id.item_video_view);
            viewHolder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            viewHolder.playBtn = (ImageView) convertView.findViewById(R.id.play_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SLUserDetail userDetailInfo = slUserDetailList.get(position);
        SLUser userInfo = userDetailInfo.getUser();
        viewHolder.userName.setText(userInfo.getNickName());
        viewHolder.userAge.setText(userInfo.getAge() + "岁");
        viewHolder.cityName.setText(StringUtils.isNotBlank(userInfo.getCityName()) ? userInfo.getCityName() : "/北京");
        if (SexType.SEX_BOY.getResultCode().equals(userInfo.getSex())) {
            viewHolder.sexImg.setBackgroundResource(R.mipmap.man_icon);
        } else if (SexType.SEX_GIRL.getResultCode().equals(userInfo.getSex())) {
            viewHolder.sexImg.setBackgroundResource(R.mipmap.women_icon);
        }
        ImageLoader.getInstance().displayImage(userInfo.getHeadUrl(), viewHolder.videoImg, ImageLoaderUtil.getSmallImageOptions());
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("user", slUserDetailList.get(position).getUser());
                mContext.startActivity(intent);
            }
        });
        viewHolder.playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtra("videoPath", videoUrl);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public LinearLayout itemLayout;
        public TextView userName;
        public TextView cityName;
        public TextView userAge;
        public ImageView sexImg;
        public View videoView;
        public ImageView playBtn;
        public ImageView videoImg;
    }
}