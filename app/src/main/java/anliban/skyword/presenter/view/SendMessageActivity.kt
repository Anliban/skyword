package anliban.skyword.presenter.view

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import anliban.skyword.R
import anliban.skyword.data.source.local.LocalDataSource
import anliban.skyword.data.source.local.PrefModel
import anliban.skyword.data.source.remote.FirebaseDbDataSource
import anliban.skyword.data.source.remote.FirebaseInstanceDataSource
import anliban.skyword.databinding.ActivitySendMessageBinding
import anliban.skyword.presenter.viewmodel.SendMessageViewModel
import anliban.skyword.presenter.viewmodel.SendMessageViewModelFactory
import anliban.skyword.repository.SendMessageRepository
import anliban.skyword.util.log
import anliban.skyword.util.toast

class SendMessageActivity : AppCompatActivity(), IDataBinding, SensorEventListener {

    override val resourceId = R.layout.activity_send_message
    private val binding by lazy { bind<ActivitySendMessageBinding>(this) }

    private val instanceDataSource = FirebaseInstanceDataSource()
    private val dbDataSource = FirebaseDbDataSource()
    private val localDataSource by lazy { LocalDataSource(PrefModel(this)) }
    private val repository by lazy {
        SendMessageRepository(instanceDataSource, dbDataSource, localDataSource)
    }
    private val viewModel by lazy {
        ViewModelProviders.of(
            this, SendMessageViewModelFactory(repository)
        )[SendMessageViewModel::class.java]
    }
    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }
    private val accelerometer by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        viewModel.sync()
        viewModel.syncResult.observe(this, Observer { isSuccess ->
            log(if (isSuccess) "sync success" else "sync fail")
        })
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sendsor: Sensor?, acc: Int) {
        log("$sendsor, $acc")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        log("${event?.sensor}")
    }
}