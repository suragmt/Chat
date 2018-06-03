package com.example.surag.chat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                requestFragment requestfragment=new requestFragment();
                return requestfragment;
            case 1:
                chatFragment chatfragment=new chatFragment();
                return chatfragment;
            case 2:
                friendsFragment friendsfragment=new friendsFragment();
                return friendsfragment;
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:return "REQUESTS";
            case 1:return "CHATS";
            case 2:return "CONTACTS";
            default:return null;
        }

    }
}
