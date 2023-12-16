package com.example.user.vvpwithcorrectswipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private ViewPager pager = null;
    private NewPagerAdapter pagerAdapter = null;
    LayoutInflater inflater;
    int a=0;
    View v3;

    //-----------------------------------------------------------------------------
    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activitywithviewpager);

        pagerAdapter = new NewPagerAdapter();
        pager = (ViewPager) findViewById (R.id.viewPager);
        pager.setOffscreenPageLimit(1);
        pager.setAdapter (pagerAdapter);


        // Create an initial view to display; must be a subclass of FrameLayout.
        //
        /*for(int p=0;p<9;p++){
            v3 = createPage(inflater);
            pagerAdapter.addView (v3, 0);//добавление каждый раз в начало списка
            a++;
        }
*/
        pagerAdapter.notifyDataSetChanged();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            if(position==5){
                Toast.makeText(MainActivity.this, "last position", Toast.LENGTH_SHORT).show();
                /*View v = (View) inflater.inflate (R.layout.activity_main, null);
                TextView textView = (TextView)v.findViewById(R.id.text_view);
                textView.setText("1 position="+String.valueOf(pager.getCurrentItem()));*/
                //не работало из-за неправильного использования инфлейтера
                //просто определить с двух смен позиции в какую сторону листинг - и от этого строить обновленные данные
                /*pagerAdapter.addView (v3,0);*/
                int a = pager.getCurrentItem();
                pagerAdapter.removeView(pager,0);
                pagerAdapter.notifyDataSetChanged();
                pager.setCurrentItem(a-1);
            }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

   /* public View createPage(LayoutInflater inflater){
        inflater= getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_main,null);
        TextView textView = (TextView)view.findViewById(R.id.text_view);
        textView.setText("number="+String.valueOf(a));
        return view;

    }*/


    /*
    //-----------------------------------------------------------------------------
    // Here's what the app should do to add a view to the ViewPager.
    public void addView (View newPage)
    {
        int pageIndex = pagerAdapter.addView (newPage);
        // You might want to make "newPage" the currently displayed page:
        pager.setCurrentItem (pageIndex, true);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to remove a view from the ViewPager.
    public void removeView (View defunctPage)
    {
        int pageIndex = pagerAdapter.removeView (pager, defunctPage);
        // You might want to choose what page to display, if the current page was "defunctPage".
        if (pageIndex == pagerAdapter.getCount())
            pageIndex--;
        pager.setCurrentItem (pageIndex);
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to get the currently displayed page.
    public View getCurrentPage ()
    {
        return pagerAdapter.getView (pager.getCurrentItem());
    }

    //-----------------------------------------------------------------------------
    // Here's what the app should do to set the currently displayed page.  "pageToShow" must
    // currently be in the adapter, or this will crash.
    public void setCurrentPage (View pageToShow)
    {
        pager.setCurrentItem (pagerAdapter.getItemPosition (pageToShow), true);
    }*/
}

                    //в массив pages добавились вьюхи с уже усстановленными значпниями
                    //скорее проблема в классе кастомного адаптера,когда ему скармливаетбся массив,
                    // он с ним рабоотает,а потом в новопереданном массиве несовпадение индексов
                    //нужно два массива,один статический(для vp),другой динамический - данные для массива,окторый первый
                    //решена пролема - в памяти viewPagera хранилось больше,чем нужно
                    //новая проблема -в зависимости от position==(num) удаляется элемент массива,идущий после num
                    //+часть логики в адаптер
                    //работает только с статическим числом количества элементов,установленным при инициализации массива pages
                    //чтоб сделать одновременно два VP,нужен подходящий адаптер для первого(ImageSliderAdapter)
                    //____работы с фабрикой
