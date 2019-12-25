package anliban.skyword.presenter.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import anliban.skyword.R
import anliban.skyword.data.source.local.LocalDataSource
import anliban.skyword.data.source.local.PrefModel
import anliban.skyword.data.source.remote.FirebaseAuthDataSource
import anliban.skyword.data.source.remote.FirebaseDbDataSource
import anliban.skyword.data.source.remote.FirebaseInstanceDataSource
import anliban.skyword.databinding.ActivitySplashBinding
import anliban.skyword.presenter.viewmodel.SplashViewModel
import anliban.skyword.presenter.viewmodel.SplashViewModelFactory
import anliban.skyword.repository.SplashRepository

class SplashActivity : AppCompatActivity(), IDataBinding {

    override val resourceId = R.layout.activity_splash
    private val binding by lazy { bind<ActivitySplashBinding>(this) }
    private val prefModel by lazy { PrefModel(this) }
    private val instanceDataSource = FirebaseInstanceDataSource()
    private val authDataSource = FirebaseAuthDataSource()
    private val dbDataSource = FirebaseDbDataSource()
    private val localDataSource by lazy { LocalDataSource(prefModel) }
    private val repository by lazy {
        SplashRepository(instanceDataSource, authDataSource, dbDataSource, localDataSource)
    }
    private val viewModel by lazy {
        ViewModelProviders.of(this, SplashViewModelFactory(repository))[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.firebaseInit()
        viewModel.login.observe(this, Observer {
        })
        viewModel.failLogin.observe(this, Observer {
        })
    }
}