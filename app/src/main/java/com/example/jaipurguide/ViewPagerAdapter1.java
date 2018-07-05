package com.example.jaipurguide;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


public class ViewPagerAdapter1 extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    int pos;
    private int[] images;

    public ViewPagerAdapter1(Context context, int pos)
    {
        this.context = context;
        this.pos=pos;
        switch(pos)
        {
            case 0: images = new int[]{R.drawable.a1, R.drawable.a2, R.drawable.a3}; break;
            case 1:  images = new int[]{R.drawable.b1, R.drawable.b2, R.drawable.b3}; break;
            case 2: images = new int[]{R.drawable.j1, R.drawable.j2, R.drawable.j3}; break;
            case 3:images = new int[]{R.drawable.m1, R.drawable.m2, R.drawable.m3}; break;
            case 4:images = new int[]{R.drawable.s1, R.drawable.s2, R.drawable.s3}; break;
        }
//        Log.i("position3:", String.valueOf(pos));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount()
    {
        return images.length;               //always return the size of image-array otherwise array index error shows.
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                   // Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                  //  Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }


}
