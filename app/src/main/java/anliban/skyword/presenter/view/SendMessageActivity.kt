package anliban.skyword.presenter.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import anliban.skyword.R
import anliban.skyword.databinding.ActivitySendMessageBinding

class SendMessageActivity : AppCompatActivity(), IDataBinding {

    override val resourceId = R.layout.activity_send_message
    private val binding by lazy { bind<ActivitySendMessageBinding>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.text.text = "send message"
    }
}