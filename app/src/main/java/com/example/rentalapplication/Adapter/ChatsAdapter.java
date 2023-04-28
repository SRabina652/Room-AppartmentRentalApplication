package com.example.rentalapplication.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.rentalapplication.Fragment.CallFragment;
import com.example.rentalapplication.Fragment.ChatFragment;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ChatsAdapter  extends FragmentPagerAdapter {
    int countTabs;
    public ChatsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        countTabs=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ChatFragment();

            case 1:
                return new CallFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTabs;
    }
}
