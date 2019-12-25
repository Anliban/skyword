package anliban.skyword.presenter.view

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

interface IDataBinding {

    val resourceId: Int

    fun <T : ViewDataBinding> bind(activity: Activity): T =
        DataBindingUtil.setContentView(activity, resourceId)
}