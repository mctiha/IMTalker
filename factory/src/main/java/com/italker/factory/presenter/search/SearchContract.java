package com.italker.factory.presenter.search;

import com.italker.common.presenter.BaseContract;
import com.italker.factory.model.card.GroupCard;
import com.italker.factory.model.card.UserCard;

import java.util.List;

/**
 * Created by mth on 2018/3/21.
 */

public interface SearchContract {

    interface Presenter extends BaseContract.Presenter {
        // 搜索内容
        void search(String content);
    }

    // 搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    // 搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }


}
