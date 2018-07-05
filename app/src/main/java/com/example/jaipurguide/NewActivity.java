package com.example.jaipurguide;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewActivity extends AppCompatActivity {
    Animation slideUpAnimation;

    CardView cardAnim;
    TextView textView1;
    TextView textView2;
    Boolean b;

    private BroadcastReceiver broadcastReceiver;


    String value;
    private ViewPager viewPager1;


    private ImageView _btn1, _btn2, _btn3;

    private TabLayout tabLayout;
    private int[] tabIcons = {R.drawable.drive, R.drawable.cycle, R.drawable.walk,R.drawable.transit};
    private ViewPager viewPager2;

    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activty);

        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up_animation);
        cardAnim= (CardView)  findViewById(R.id.card_view3);
        textView1=(TextView)  findViewById(R.id.textview1);
        textView2=(TextView)  findViewById(R.id.textview2);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

                    if (b) {
                       // Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(context, "Internet Connection lost", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Exception caught in new activity", Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

            if (b) {
               // Toast.makeText(this, "Internet Connected", Toast.LENGTH_LONG).show();

                value = getIntent().getStringExtra("1");
                int pos = getIntent().getIntExtra("pos", 0);

               // Toast.makeText(this, value,Toast.LENGTH_LONG).show();
                Log.i("position1" , String.valueOf(pos) );
                Log.i("value in new" , String.valueOf(value) );


                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00897b")));
                getSupportActionBar().setTitle(value);
                t = (TextView) findViewById(R.id.detail);

                if (value.equals("ajmeriGate")) {
                    t.setText(R.string.a);
                    // Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                } else if (value.equals("birlaTemple")) {
                    t.setText(R.string.b);
                    //Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                } else if (value.equals("mubarakMahal")) {
                    t.setText(R.string.m);
                    //  Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                } else if (value.equals("jantarMantar")) {
                    t.setText(R.string.j);
                    //  Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                } else if (value.equals("statueGarden")) {
                    t.setText(R.string.s);
                    // Toast.makeText(this, value, Toast.LENGTH_LONG).show();
                }


                viewPager1 = (ViewPager) findViewById(R.id.viewPager1);

                ViewPagerAdapter1 viewPagerAdapter = new ViewPagerAdapter1(this, pos);
                viewPager1.setAdapter(viewPagerAdapter);
                _btn1 = (ImageView) findViewById(R.id.btn1);
                _btn2 = (ImageView) findViewById(R.id.btn2);
                _btn3 = (ImageView) findViewById(R.id.btn3);
                 setupViewPager1();
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new MyTimerTask(), 7000, 8000);
                viewPager1.setAdapter(viewPagerAdapter);

                viewPager2 = (ViewPager) findViewById(R.id.viewpager2);
                setupViewPager2(viewPager2);

                tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(viewPager2);
                setupTabIcons();
            }
            else
            {
                startActivity(new Intent(this,ConnectionLost.class));
               // Toast.makeText(this, "Internet Connection lost", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            startActivity(new Intent(this,ConnectionLost.class));
            Toast.makeText(this, "Exception caught:" +e, Toast.LENGTH_LONG).show();
        }


        //for map movement on fragment
        final ScrollView mainScrollView = (ScrollView) findViewById(R.id.activity_main);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:return true;
                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void setupViewPager1()
    {

        _btn1.setImageResource(R.drawable.fill_circle);

        viewPager1.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

               Log.d("view1","viewpager1 onpage");

                _btn1.setImageResource(R.drawable.holo_circle);
                _btn2.setImageResource(R.drawable.holo_circle);
                _btn3.setImageResource(R.drawable.holo_circle);
                 btnAction(position);
            }});}


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            NewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (viewPager1.getCurrentItem() == 0) {

                        viewPager1.setCurrentItem(1);
                    } else if (viewPager1.getCurrentItem() == 1) {

                        viewPager1.setCurrentItem(2);
                    } else {

                        viewPager1.setCurrentItem(0);
                    }
                }
            });
        }}

    private void btnAction(int action) {
        switch (action) {
            case 0:
                _btn1.setImageResource(R.drawable.fill_circle);
                break;

            case 1:
                _btn2.setImageResource(R.drawable.fill_circle);
                break;
            case 2:
                _btn3.setImageResource(R.drawable.fill_circle);
                break;
        }}



    private void setupTabIcons ()
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager2(final ViewPager viewPager2)
    {
        final ViewPagerAdapter2 adapter = new ViewPagerAdapter2(getSupportFragmentManager());

        adapter.addFrag(new OneFragment(), null);
        adapter.addFrag( new TwoFragment(), null);
        adapter.addFrag(new ThreeFragment(), null);
        adapter.addFrag( new FourFragment(), null);




        viewPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {

              //  Toast.makeText(NewActivity.this, "onPageselected", Toast.LENGTH_LONG).show();
                 textView1.setText("- - -");
                 textView2.setText("- - -");

            }});

        viewPager2.setAdapter(adapter);


    }



    private  class ViewPagerAdapter2 extends FragmentPagerAdapter
    {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final FragmentManager mFragmentManager;


        public ViewPagerAdapter2(FragmentManager manager)
        {
            super(manager);
            mFragmentManager = manager;
        }



        @Override
        public Fragment getItem(int position) {
             return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
        return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title)
        {
         mFragmentList.add(fragment);
         mFragmentTitleList.add(title);
        }



        @Override
        public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
        }
        }


public String  getData()
        {
        return value;
        }

public void startSlideUpAnimation(String distance,String duration)
        {

           /* if(counter%2==0)
            {*/
                textView1.setText(distance);
                textView2.setText(duration);
                cardAnim.startAnimation(slideUpAnimation);
             //   cardAnim.setVisibility(View.GONE);
           // }
           // Toast.makeText(this, "counter in slide:"+counter++, Toast.LENGTH_LONG).show();
        }






@Override
public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
        }

@Override
public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        }
}
