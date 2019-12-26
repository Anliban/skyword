package anliban.skyword.presenter.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import anliban.skyword.R
import anliban.skyword.data.User
import anliban.skyword.data.source.local.LocalDataSource
import anliban.skyword.data.source.local.PrefModel
import anliban.skyword.data.source.remote.FirebaseAuthDataSource
import anliban.skyword.databinding.ActivityLoginBinding
import anliban.skyword.presenter.viewmodel.LoginViewModel
import anliban.skyword.presenter.viewmodel.LoginViewModelFactory
import anliban.skyword.repository.LoginRepository
import anliban.skyword.util.toast

class LoginActivity : AppCompatActivity(), IDataBinding {

    override val resourceId = R.layout.activity_login
    private val binding by lazy { bind<ActivityLoginBinding>(this) }

    private val localDataSource by lazy { LocalDataSource(PrefModel(this)) }
    private val authDataSource = FirebaseAuthDataSource()
    private val repository by lazy { LoginRepository(authDataSource, localDataSource) }

    private val viewModel by lazy {
        ViewModelProviders.of(this, LoginViewModelFactory(repository))[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdit.text.toString()
            val password = binding.pwEdit.text.toString()
            viewModel.login(User(email, password))
        }
        binding.joinBtn.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
            finish()
        }
        viewModel.login.observe(this, Observer {
            startActivity(Intent(this, SendMessageActivity::class.java))
            finish()
        })
        viewModel.message.observe(this, Observer {
            toast(it)
        })
    }
}