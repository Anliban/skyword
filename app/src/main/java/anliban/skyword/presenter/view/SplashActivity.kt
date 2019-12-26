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
import anliban.skyword.databinding.ActivitySplashBinding
import anliban.skyword.presenter.viewmodel.SplashViewModel
import anliban.skyword.presenter.viewmodel.SplashViewModelFactory
import anliban.skyword.repository.SplashRepository

class SplashActivity : AppCompatActivity(), IDataBinding {

    override val resourceId = R.layout.activity_splash
    private val binding by lazy { bind<ActivitySplashBinding>(this) }
    private val prefModel by lazy { PrefModel(this) }
    private val authDataSource = FirebaseAuthDataSource()
    private val localDataSource by lazy { LocalDataSource(prefModel) }
    private val repository by lazy { SplashRepository(authDataSource, localDataSource) }
    private val viewModel by lazy {
        ViewModelProviders.of(this, SplashViewModelFactory(repository))[SplashViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding
        viewModel.login()
        viewModel.login.observe(this, Observer {
            startActivity(Intent(this, SendMessageActivity::class.java))
            finish()
        })
        viewModel.loginFailure.observe(this, Observer {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        })
    }
}