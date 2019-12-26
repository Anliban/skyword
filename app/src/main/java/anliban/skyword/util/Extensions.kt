package anliban.skyword.util

import android.content.Context
import android.util.Log
import android.widget.Toast

infix fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

infix fun Any.log(msg: String) = Log.i(this::class.java.simpleName, msg)