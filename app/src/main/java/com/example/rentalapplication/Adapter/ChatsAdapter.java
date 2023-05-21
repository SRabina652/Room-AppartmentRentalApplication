package com.example.rentalapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rentalapplication.Fragment.CallFragment;
import com.example.rentalapplication.Fragment.ChatFragment;

public class ChatsAdapter  extends FragmentPagerAdapter {

    public ChatsAdapter(@androidx.annotation.NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
//        if (position==0){
//            return new ChatFragment();
//        }else{
            return  new ChatFragment();
//        }
    }

    @Override
    public int getCount() {
        return 1;//number of tabs
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        if(position == 0){
//            return "Chats";
//        }
        return "Chats";
    }
}
