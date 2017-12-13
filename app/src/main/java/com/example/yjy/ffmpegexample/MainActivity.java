package com.example.yjy.ffmpegexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Button btn;
    public static final String SRC = "/mnt/sdcard/Pictures/22.pdf";
    public static final String DEST = "/mnt/sdcard/Pictures/33.pdf";
    public static final String IMG = "/mnt/sdcard/Pictures/11.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
        // Example of a call to a native method
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setPdf(SRC,DEST);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    //绘画pdf到新的pdf
    //dest<-src
    public void setPdf(String src, String dest) throws IOException, DocumentException {
        PdfReader pdfreader = new PdfReader(src);
        //追加用的方法，里面初始化了pdf的数据
        PdfStamper stamper = new PdfStamper(pdfreader, new FileOutputStream(dest));
        //获取imgae实例
        Image image = Image.getInstance(IMG);
        //转化为pdfimage
        PdfImage imagestream = new PdfImage(image, "", null);
        imagestream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
        //pdf追加
        PdfIndirectObject ref = stamper.getWriter().addToBody(imagestream);
        //设置位置
        image.setDirectReference(ref.getIndirectReference());
        image.setAbsolutePosition(36, 400);
        //画在哪一页
        PdfContentByte over = stamper.getOverContent(3);
        //追加图片
        over.addImage(image);
        stamper.close();
        pdfreader.close();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
