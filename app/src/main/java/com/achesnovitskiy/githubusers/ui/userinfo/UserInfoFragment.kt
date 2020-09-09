package com.achesnovitskiy.githubusers.ui.userinfo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.app.App.Companion.appComponent
import com.achesnovitskiy.githubusers.ui.base.BaseFragment
import com.achesnovitskiy.githubusers.ui.userinfo.di.DaggerUserInfoComponent
import com.achesnovitskiy.githubusers.ui.userinfo.di.UserInfoModule
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class UserInfoFragment : BaseFragment(R.layout.fragment_user_info) {

    @Inject
    lateinit var viewModel: UserInfoViewModel

    private val userName: String
        get() = arguments?.get("user_name") as String

    override fun onAttach(context: Context) {
        super.onAttach(context)

        DaggerUserInfoComponent
            .builder()
            .appComponent(appComponent)
            .userInfoModule(
                UserInfoModule(viewModelStoreOwner = this)
            )
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user_info_back_button_image_view.setOnClickListener {
            if (isSnackbarInitialized) {
                snackbar.dismiss()

                isSnackbarInitialized = false
            }

            requireActivity().onBackPressed()
        }

        viewModel.loadUserInfoObserver.onNext(userName)
    }

    override fun onResume() {
        super.onResume()

        disposable = CompositeDisposable(
            viewModel.userInfoObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { userInfo ->
                        Picasso.get()
                            .load(userInfo.avatarUrl)
                            .error(R.drawable.ic_error_black_48)
                            .resize(500, 500)
                            .centerCrop()
                            .into(user_info_avatar_image_view)

                        user_info_nickname_value.text = userInfo.name

                        user_info_webpage_value.text = userInfo.webpage

                        user_info_location_value.text = userInfo.location
                    },
                    {
                    }
                ),

            viewModel.loadingStateObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { loadingState ->
                    user_info_progress_bar.isVisible = loadingState.isLoading

                    if (loadingState.errorRes != null) {
                        snackbar = Snackbar.make(
                            requireView(),
                            getString(loadingState.errorRes),
                            Snackbar.LENGTH_INDEFINITE
                        ).apply {
                            setAction(getString(R.string.repeat)) {
                                viewModel.loadUserInfoObserver.onNext(userName)
                            }

                            show()
                        }

                        isSnackbarInitialized = true
                    } else {
                        if (isSnackbarInitialized) {
                            snackbar.dismiss()

                            isSnackbarInitialized = false
                        }
                    }
                }
        )
    }
}