package com.judd.trump.widget.pickerview.lib;

// Referenced classes of package com.qingchifan.view:
//            LoopView, OnItemSelectedListener

final class OnItemSelectedRunnable implements Runnable {
    final LoopView loopView;

    OnItemSelectedRunnable(LoopView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getSelectedItem());
    }
}