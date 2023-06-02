package com.imf.so.assets.load;

import androidx.annotation.Nullable;

import com.imf.so.assets.load.bean.SoFileInfo;

import java.io.File;
import java.util.List;

public abstract class SimpleNeedDownloadSoListener implements NeedDownloadSoListener {

    @Override
    public void onNeedDownloadSoInfo(File saveLibsDir, @Nullable List<SoFileInfo> list) {

    }

    @Override
    public void onConfigEmpty() {

    }

    @Override
    public void onLibsEmpty() {

    }
}
