package com.achesnovitskiy.githubusers.ui.userinfo

import android.content.Context
import android.os.Bundle
import android.view.View
import com.achesnovitskiy.githubusers.R
import com.achesnovitskiy.githubusers.app.App.Companion.appComponent
import com.achesnovitskiy.githubusers.ui.base.BaseFragment
import com.achesnovitskiy.githubusers.ui.userinfo.di.DaggerUserInfoComponent
import com.achesnovitskiy.githubusers.ui.userinfo.di.UserInfoModule
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
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()

        disposable = CompositeDisposable(
            viewModel.getUserInfoObservable(userName)
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