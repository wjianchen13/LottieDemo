package com.example.lottiedemo.rich.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

public class ImageSpanC extends ImageSpan {

    private Rect rect = new Rect();
    private int xiuAlign;

    public ImageSpanC(Context context, Bitmap b, int align) {
        super(context, b, ImageSpan.ALIGN_BASELINE);
        xiuAlign =align;
    }

    public ImageSpanC(Drawable d, int align) {
        super(d, ImageSpan.ALIGN_BASELINE);
        xiuAlign = align;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        if (d != null) {
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;  //字体高度
                int drHeight = rect.bottom - rect.top; //图片高度
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 3;
                fm.ascent = -bottom; //字体的高度 descent - leading
                fm.top = -bottom;  //
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }
        return super.getSize(paint, text, start, end, fm);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        Drawable b = getDrawable();

        if (b == null){
            super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            return;
        }

        int transY = 0;

        Paint.FontMetrics fm = paint.getFontMetrics();
        paint.getTextBounds("我", 0, 1, rect);
        if (xiuAlign == ImageSpanAlign.XIU_ALIGN_CENTER) { // 居中对齐
            transY = (int) ((y + fm.descent + y + fm.ascent) / 2 - b.getBounds().bottom / 2);
        } else if (xiuAlign == ImageSpanAlign.XIU_ALIGN_BOTTOM) { //  底部对齐
            transY = y - b.getBounds().bottom + rect.bottom - b.getBounds().top;
        }/* else if (xiuAlign == ImageSpanAlign.XIU_ALIGN_TILING){
            transY = 0;
        }*/

        canvas.save();  //
        canvas.translate(x, transY);

      /*  Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(b.getBounds(), p);*/
        b.draw(canvas);
        canvas.restore();
    }

        /*@Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = this.getDrawable();
            Rect rect = drawable.getBounds();
            if(fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;
                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        }
        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = this.getDrawable();
            canvas.save();
            int transY = (bottom - top - drawable.getBounds().bottom) / 2 + top - lineSpace;
            canvas.translate(x, (float)transY);
            drawable.draw(canvas);
            canvas.restore();
        }*/


}