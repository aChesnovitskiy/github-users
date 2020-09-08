package com.achesnovitskiy.githubusers.ui.users

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.app.App.Companion.appComponent
import com.achesnovitskiy.githubusers.ui.base.BaseFragment
import com.achesnovitskiy.githubusers.ui.users.di.DaggerUsersComponent
import com.achesnovitskiy.githubusers.ui.users.di.UsersModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_users.*
import javax.inject.Inject

class UsersFragment : BaseFragment(R.layout.fragment_users) {

    @Inject
    lateinit var viewModel: UsersViewModel

    private val usersAdapter: UsersAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UsersAdapter { userItem ->
//            this.findNavController()
//                .navigate(
//                    UsersFragmentDirections.actionUsersFragmentToUserInfoFragment(userItem.name)
//                )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerUsersComponent
            .builder()
            .appComponent(appComponent)
            .usersModule(
                UsersModule(viewModelStoreOwner = this)
            )
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

        with(users_recycler_view) {
            adapter = usersAdapter

            layoutManager = LinearLayoutManager(context)

            addItemDecoration(divider)
        }

        users_swipe_refresh_layout.setOnRefreshListener {
            viewModel.refreshObserver.onNext(Unit)

            users_swipe_refresh_layout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()

        disposable = CompositeDisposable(
            viewModel.usersObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { users ->
//                    repos_list_is_empty_text_view.isVisible = repos.isNullOrEmpty()

                        usersAdapter.submitList(users)
                    },
                    {
                    }
                )

//            reposViewModel.loadingStateObservable
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { loadingState ->
//                    repos_progress_bar.isVisible = loadingState.isLoading
//
//                    if (loadingState.errorRes != null) {
//                        snackbar = Snackbar.make(
//                            requireView(),
//                            getString(loadingState.errorRes),
//                            Snackbar.LENGTH_INDEFINITE
//                        ).apply {
//                            setAction(getString(R.string.repeat)) {
//                                reposViewModel.refreshObserver.onNext(Unit)
//                            }
//                            show()
//                        }
//
//                        isSnackbarInitialized = true
//                    } else {
//                        if (isSnackbarInitialized) {
//                            snackbar.dismiss()
//
//                            isSnackbarInitialized = false
//                        }
//                    }
//                }
        )
    }
}