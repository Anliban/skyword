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
import anliban.skyword.data.source.remote.FirebaseDbDataSource
import anliban.skyword.databinding.ActivityJoinBinding
import anliban.skyword.presenter.viewmodel.JoinViewModel
import anliban.skyword.presenter.viewmodel.JoinViewModelFactory
import anliban.skyword.repository.JoinRepository
import anliban.skyword.util.toast

class JoinActivity : AppCompatActivity(), IDataBinding {

    override val resourceId = R.layout.activity_login
    private val binding by lazy { bind<ActivityJoinBinding>(this) }

    private val localDataSource by lazy { LocalDataSource(PrefModel(this)) }
    private val authDataSource = FirebaseAuthDataSource()
    private val dbDataSource = FirebaseDbDataSource()
    private val repository by lazy { JoinRepository(authDataSource, dbDataSource, localDataSource) }

    private val viewModel by lazy {
        ViewModelProviders.of(this, JoinViewModelFactory(repository))[JoinViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.text.text = "회원가입"
        viewModel.join(User("aa@naver.com", "bbaasd"))
        viewModel.success.observe(this, Observer {
            startActivity(Intent(this, SendMessageActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        })
        viewModel.message.observe(this, Observer {
            toast(it)
        })
    }
}