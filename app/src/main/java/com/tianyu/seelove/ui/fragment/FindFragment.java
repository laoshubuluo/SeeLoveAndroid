package com.tianyu.seelove.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tianyu.seelove.R;
import com.tianyu.seelove.adapter.FindUserAdapter;
import com.tianyu.seelove.model.entity.user.SLUser;
import com.tianyu.seelove.ui.activity.user.UserInfoActivity;
import com.tianyu.seelove.utils.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmengt(发现)
 * @author shisheng.zhao
 * @date 2017-03-29 15:15
 */
public class FindFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FindUserAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtil.d("FindFragment____onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("FindFragment____onCreate");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("FindFragment____onCreateView");
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.titleView);
        titleView.setText(R.string.find);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FindUserAdapter(getActivity(), buildData());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FindUserAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SLUser data) {
                Intent intent = new Intent();
                intent.putExtra("userName",data.getNickName());
                intent.setClass(view.getContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    // 测试数据
    private List<SLUser> buildData() {
        String[] names = {"邓紫棋", "范冰冰", "杨幂", "Angelababy", "唐嫣", "柳岩"};
        String[] imgUrs = {"https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg",
                "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477122795&di=f740bd484870f9bcb0cafe454a6465a2&src=http://tpic.home.news.cn/xhCloudNewsPic/xhpic1501/M08/28/06/wKhTlVfs1h2EBoQfAAAAAF479OI749.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=673651839,1464649612&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=fd90a83e900a304e4d22a7fae1c9a7c3/d01373f082025aafa480a2f1fcedab64034f1a5d.jpg",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1849074283,1272897972&fm=111&gp=0.jpg",
                "https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/image/h%3D200/sign=005560fc8b5494ee982208191df4e0e1/c2fdfc039245d68827b453e7a3c27d1ed21b243b.jpg",
        };
        List<SLUser> userInfoList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            SLUser userInfo = new SLUser();
            userInfo.setHeadUrl(imgUrs[i]);
            userInfo.setNickName(names[i]);
            userInfoList.add(userInfo);
        }
        return userInfoList;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d("FindFragment____onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d("FindFragment____onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d("FindFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("FindFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("FindFragment____onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("FindFragment____onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("FindFragment____onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d("FindFragment____onDetach");
    }
}