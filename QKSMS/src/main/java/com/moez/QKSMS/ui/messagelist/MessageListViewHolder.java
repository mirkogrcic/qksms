package com.moez.QKSMS.ui.messagelist;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.moez.QKSMS.R;
import com.moez.QKSMS.common.LiveViewManager;
import com.moez.QKSMS.common.google.ItemLoadedCallback;
import com.moez.QKSMS.common.google.ThumbnailManager;
import com.moez.QKSMS.enums.QKPreference;
import com.moez.QKSMS.interfaces.LiveView;
import com.moez.QKSMS.interfaces.SlideViewInterface;
import com.moez.QKSMS.ui.base.ClickyViewHolder;
import com.moez.QKSMS.ui.base.QKActivity;
import com.moez.QKSMS.ui.mms.Presenter;
import com.moez.QKSMS.ui.view.AvatarView;
import com.moez.QKSMS.ui.view.QKTextView;

import java.util.Map;

public class MessageListViewHolder extends ClickyViewHolder<MessageItem> implements SlideViewInterface {
    private final String TAG = "MessageListViewHolder";

    // Views
    @Bind(R.id.text_view) QKTextView mBodyTextView;
    @Bind(R.id.date_view) QKTextView mDateView;
    @Bind(R.id.locked_indicator) ImageView mLockedIndicator;
    @Bind(R.id.delivered_indicator) ImageView mDeliveredIndicator;
    @Bind(R.id.details_indicator) ImageView mDetailsIndicator;
    @Bind(R.id.avatar) AvatarView mAvatarView;
    @Bind(R.id.message_block) LinearLayout mMessageBlock;
    @Bind(R.id.space) View mSpace;
    @Bind(R.id.mms_view) FrameLayout mMmsView;
    @Bind(R.id.image_view) ImageView mImageView;
    @Bind(R.id.play_slideshow_button) ImageButton mSlideShowButton;

    protected Button mDownloadButton;
    protected QKTextView mDownloadingLabel;

    protected ImageLoadedCallback mImageLoadedCallback;
    protected Presenter mPresenter;

    public MessageListViewHolder(QKActivity context, View view) {
        super(context, view);

        ButterKnife.bind(this, view);
        mLockedIndicator = (ImageView) view.findViewById(R.id.locked_indicator);
        mDeliveredIndicator = (ImageView) view.findViewById(R.id.delivered_indicator);
        mDetailsIndicator = (ImageView) view.findViewById(R.id.details_indicator);
        mAvatarView = (AvatarView) view.findViewById(R.id.avatar);
        mMessageBlock = (LinearLayout) view.findViewById(R.id.message_block);
        mSpace = view.findViewById(R.id.space);
        mMmsView = (FrameLayout) view.findViewById(R.id.mms_view);
        mImageView = (ImageView) view.findViewById(R.id.image_view);
        mSlideShowButton = (ImageButton) view.findViewById(R.id.play_slideshow_button);
    }

    protected void showMmsView(boolean visible) {
        mMmsView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mImageView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    protected void inflateDownloadControls() {
        if (mDownloadButton == null) {
            itemView.findViewById(R.id.mms_downloading_view_stub).setVisibility(View.VISIBLE);
            mDownloadButton = (Button) itemView.findViewById(R.id.btn_download_msg);
            mDownloadingLabel = (QKTextView) itemView.findViewById(R.id.label_downloading);
        }
    }

    protected void setLiveViewCallback(LiveView liveViewCallback) {
        LiveViewManager.registerView(QKPreference.THEME, this, liveViewCallback);
    }

    @Override
    public void setImage(String name, Bitmap bitmap) {
        if (bitmap == null) {
            showMmsView(false);
        } else {
            showMmsView(true);

            try {
                mImageView.setImageBitmap(bitmap);
                mImageView.setVisibility(View.VISIBLE);
            } catch (java.lang.OutOfMemoryError e) {
                Log.e(TAG, "setImage: out of memory: ", e);
            }
        }
    }

    @Override
    public void setImageRegionFit(String fit) {

    }

    @Override
    public void setImageVisibility(boolean visible) {

    }

    @Override
    public void setVideo(String name, Uri video) {

    }

    @Override
    public void setVideoThumbnail(String name, Bitmap bitmap) {
        showMmsView(true);

        try {
            mImageView.setImageBitmap(bitmap);
            mImageView.setVisibility(View.VISIBLE);
        } catch (java.lang.OutOfMemoryError e) {
            Log.e(TAG, "setVideo: out of memory: ", e);
        }
    }

    @Override
    public void setVideoVisibility(boolean visible) {

    }

    @Override
    public void startVideo() {

    }

    @Override
    public void stopVideo() {

    }

    @Override
    public void pauseVideo() {

    }

    @Override
    public void seekVideo(int seekTo) {

    }

    @Override
    public void setAudio(Uri audio, String name, Map<String, ?> extras) {

    }

    @Override
    public void startAudio() {

    }

    @Override
    public void stopAudio() {

    }

    @Override
    public void pauseAudio() {

    }

    @Override
    public void seekAudio(int seekTo) {

    }

    @Override
    public void setText(String name, String text) {

    }

    @Override
    public void setTextVisibility(boolean visible) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setVisibility(boolean visible) {

    }

    static protected class ImageLoadedCallback implements ItemLoadedCallback<ThumbnailManager.ImageLoaded> {
        private long mMessageId;
        private final MessageListViewHolder mListItem;

        public ImageLoadedCallback(MessageListViewHolder listItem) {
            mListItem = listItem;
            mListItem.setImage(null, null);
            mMessageId = listItem.mData.getMessageId();
        }

        public void reset(MessageListViewHolder listItem) {
            mMessageId = listItem.mData.getMessageId();
        }

        public void onItemLoaded(ThumbnailManager.ImageLoaded imageLoaded, Throwable exception) {
            // Make sure we're still pointing to the same message. The list item could have // been recycled.
            MessageItem msgItem = mListItem.mData;
            if (msgItem != null && msgItem.getMessageId() == mMessageId) {
                if (imageLoaded.mIsVideo) {
                    mListItem.setVideoThumbnail(null, imageLoaded.mBitmap);
                } else {
                    mListItem.setImage(null, imageLoaded.mBitmap);
                }
            }
        }
    }
}
