package com.example.gzy.test3.bardata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;

import com.example.gzy.test3.R;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.IncapableCause;

import com.zhihu.matisse.internal.entity.Item;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * created by gzy on 2019/4/6.
 * Describle;
 */
public class GifSizeFilter extends Filter {

    @Override
    protected Set<MimeType> constraintTypes() {
        return new HashSet<MimeType>() {{
            add(MimeType.GIF);
            add(MimeType.MP4);
        }};
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;

        Point size = PhotoMetadataUtils.getBitmapBound(context.getContentResolver(), item.getContentUri());
        if (size.x < 320 || size.y < 320 || item.size > 5 * Filter.K * Filter.K) {
            return new IncapableCause(IncapableCause.DIALOG, context.getString(R.string.error_gif, 320,
                    String.valueOf(PhotoMetadataUtils.getSizeInMB(5 * Filter.K * Filter.K))));
        }
        return null;
    }

}

