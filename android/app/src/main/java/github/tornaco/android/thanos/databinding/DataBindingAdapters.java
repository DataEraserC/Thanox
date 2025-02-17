package github.tornaco.android.thanos.databinding;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericTransitionOptions;

import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.pm.AppInfo;
import github.tornaco.android.thanos.core.process.RunningState;
import github.tornaco.android.thanos.power.StandbyRule;
import github.tornaco.android.thanos.start.StartRule;
import github.tornaco.android.thanos.theme.AppThemePreferences;
import github.tornaco.android.thanos.util.GlideApp;
import github.tornaco.android.thanos.util.GlideRequest;
import util.Consumer;

public class DataBindingAdapters {

    @BindingAdapter("android:startRules")
    public static void setStartRules(RecyclerView recyclerView, ObservableList<StartRule> rules) {
        @SuppressWarnings("unchecked")
        Consumer<List<StartRule>> adapter = (Consumer<List<StartRule>>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.accept(rules);
        }
    }

    @BindingAdapter("android:standByRules")
    public static void setStandByRules(RecyclerView recyclerView, ObservableList<StandbyRule> rules) {
        @SuppressWarnings("unchecked")
        Consumer<List<StandbyRule>> adapter = (Consumer<List<StandbyRule>>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.accept(rules);
        }
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @BindingAdapter({"android:mergedRunningStateItems"})
    public static void setProcessModels(RecyclerView view, List<RunningState.MergedItem> processModels) {
        Consumer<List<RunningState.MergedItem>> consumer = (Consumer<List<RunningState.MergedItem>>) view.getAdapter();
        consumer.accept(processModels);
    }

    @BindingAdapter("android:runningItemIcon")
    public static void setRunningItemIcon(ImageView imageView, RunningState.MergedItem mergedItem) {
        AppInfo appInfo = new AppInfo();
        appInfo.setPkgName(mergedItem.mPackageInfo.packageName);
        GlideRequest request = GlideApp.with(imageView)
                .load(appInfo)
                .error(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .fallback(github.tornaco.android.thanos.module.common.R.mipmap.ic_fallback_app_icon)
                .transition(GenericTransitionOptions.with(github.tornaco.android.thanos.module.common.R.anim.grow_fade_in));
        if (AppThemePreferences.getInstance().useRoundIcon(imageView.getContext())) {
            request = request.circleCrop();
        }
        request.into(imageView);
    }

    @BindingAdapter("android:mergeItemDesc")
    public static void setMergeItemDesc(TextView textView, RunningState.MergedItem mergeItem) {
        if (mergeItem.mBackground) {
            textView.setText(R.string.title_cached_background_process);
        } else {
            int resid = R.string.running_processes_item_description_s_s;
            if (mergeItem.mLastNumProcesses != 1) {
                resid = mergeItem.mLastNumServices != 1
                        ? R.string.running_processes_item_description_p_p
                        : R.string.running_processes_item_description_p_s;
            } else if (mergeItem.mLastNumServices != 1) {
                resid = R.string.running_processes_item_description_s_p;
            }
            String description = textView.getContext().getResources().getString(resid,
                    mergeItem.mLastNumProcesses,
                    mergeItem.mLastNumServices);
            textView.setText(description);
        }
    }
}
