package anliban.skyword.util

import android.content.Context
import android.widget.Toast

infix fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()