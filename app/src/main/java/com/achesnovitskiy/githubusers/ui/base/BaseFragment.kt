package com.achesnovitskiy.githubusers.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

abstract class BaseFragment(@LayoutRes contentLayoutId: Int): Fragment(contentLayoutId) {

    open var disposable: Disposable = Disposables.disposed()

    lateinit var snackbar: Snackbar

    var isSnackbarInitialized: Boolean = false

    override fun onPause() {
        disposable.dispose()

        super.onPause()
    }

    override fun onDestroy() {
        disposable.dispose()

        super.onDestroy()
    }
}