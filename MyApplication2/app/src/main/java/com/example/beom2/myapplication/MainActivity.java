package com.example.beom2.myapplication;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.util.ArrayList;

//activity_Main에서 adapterViewFlipper부분 background에 칼라넣는 방법
//아니면 item.xml에서 android:scale.fitparent인가 그걸하면 됨
public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    ArrayList<Integer> galleryId = new ArrayList<Integer>();
    ArrayList<ImageView> indexes = new ArrayList<ImageView>();
    ArrayList<String>textIndex = new ArrayList<String>();
    AdapterViewFlipper avf;
    ToggleButton tb;
    Button bt1;//previous
    Button bt2;//next
    ImageView mImageView;
    TextView textView;
    float xAtDown;
    float xAtUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // inJustDecodeBounds 설정을 해주지 않으면 이부분에서 큰 이미지가 들어올 경우 outofmemory Exception이 발생한다.
        BitmapFactory.decodeResource(getResources(), R.drawable.kangdong, options);
        BitmapFactory.decodeResource(getResources(), R.drawable.nowon, options);
        BitmapFactory.decodeResource(getResources(), R.drawable.gangnam, options);
        BitmapFactory.decodeResource(getResources(), R.drawable.seongbuk, options);
        BitmapFactory.decodeResource(getResources(), R.drawable.yongsan, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;


        indexes.add((ImageView) findViewById(R.id.indexLight1));
        indexes.add((ImageView) findViewById(R.id.indexLight2));
        indexes.add((ImageView) findViewById(R.id.indexLight3));
        indexes.add((ImageView) findViewById(R.id.indexLight4));
        indexes.add((ImageView) findViewById(R.id.indexLight5));

        textIndex.add("Seongbuk-gu");
        textIndex.add("Nowon-gu");
        textIndex.add("Kangdong-gu");
        textIndex.add("Yongsan-gu");
        textIndex.add("gangnam-gu");


        galleryId.add(getResources().getIdentifier("seongbuk", "drawable", this.getPackageName()));
        galleryId.add(getResources().getIdentifier("nowon", "drawable", this.getPackageName()));
        galleryId.add(getResources().getIdentifier("kangdong", "drawable", this.getPackageName()));
        galleryId.add(getResources().getIdentifier("yongsan", "drawable", this.getPackageName()));
        galleryId.add(getResources().getIdentifier("gangnam", "drawable", this.getPackageName()));


        avf = (AdapterViewFlipper) findViewById(R.id.adapterViewFlipper1);
        avf.setAdapter(new galleryAdapter(this)); //갤러리 어뎁터에 연결
        avf.setOnTouchListener(this);//터치리스너에 연결
        textView=(TextView)findViewById(R.id.infoText);
        textView.setText(textIndex.get(0).toString());

        bt1 = (Button) findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //왼쪽에서 오른쪽으로 슬라이드(슬라이드 되는 부분 메서드화 시키면 더 코드 깔끔할 듯.)
                avf.setInAnimation(view.getContext(), R.animator.left_in);
                avf.setOutAnimation(view.getContext(), R.animator.right_out);
                avf.showPrevious();
            }
        });
        bt2 = (Button) findViewById(R.id.button2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //오른쪽에서 왼쪽으로 슬라이드
                avf.setInAnimation(view.getContext(), R.animator.right_in);
                avf.setOutAnimation(view.getContext(), R.animator.left_out);
                avf.showNext();
            }
        });

    }

    //터치로 이미지 넘기는 메서드 onTouch를 오버라이딩 해야함.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != avf) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            xAtDown = event.getX();
        }
        //else if 부분이 중요함.
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            xAtUp = event.getX();
            if (xAtDown > xAtUp)
            {
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
                avf.showNext();
            }
            else if (xAtDown < xAtUp)
            {
                avf.setInAnimation(v.getContext(), R.animator.left_in);
                avf.setOutAnimation(v.getContext(), R.animator.right_out);
                avf.showPrevious();
                //다시 오른쪽->왼쪽으로 슬라이드 되도록 원상복구 시켜놓음.
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
            }
        }
        return true;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //액티비티가 처음 시작하거나 다시 시작될 때 실행되는 메서드
    @Override
    protected void onResume() {
        super.onResume();

    }

    //**어뎁터 클래스 정의
    public class galleryAdapter extends BaseAdapter {
        private final Context mContext;
        LayoutInflater inflater;

        public galleryAdapter(Context mContext) {
            this.mContext = mContext;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return galleryId.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            //늘 이 부분이 궁금했음..
            if (converView == null) {
                converView = inflater.inflate(R.layout.item, parent, false);
            }
            //bitmap팩토리 쓰기 전 코드.
            //ImageView imageView = (ImageView)converView.findViewById(R.id.imageView1);
            //imageView.setImageResource(galleryId.get(position));
         /*   textView=(TextView)converView.findViewById(R.id.infoText);
            textView.setText(textIndex.get(2).toString());*/

            mImageView = (ImageView) converView.findViewById(R.id.imageView1);
            mImageView.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), galleryId.get(position), 300, 100));

            //사진이 전환되면서 버튼 위 indexLight도 변하게끔 하는 부분.
            for (int i = 0; i < indexes.size(); i++) {
                ImageView index = indexes.get(i);
                if (i == position) {
                    index.setImageResource(R.drawable.green);
                } else {
                    index.setImageResource(R.drawable.white2);
                }
            }
            return converView;
        }

    }
}
