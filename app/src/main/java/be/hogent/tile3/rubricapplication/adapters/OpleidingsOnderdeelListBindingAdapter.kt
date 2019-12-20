package be.hogent.tile3.rubricapplication.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.ui.ApiStatus
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("opleidingsOnderdeelNaam")
fun TextView.setOpleidingsOnderdeelNaam(item: OpleidingsOnderdeel?){
    item?.let{
        text = item.naam
    }
}

@BindingAdapter("apiStatusRV")
fun bindStatus(recyclerView: RecyclerView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            recyclerView.visibility = View.GONE
        }
        ApiStatus.ERROR -> {
            recyclerView.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            recyclerView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("loadingAnimation")
fun bindStatus(statusImageView: LottieAnimationView, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setAnimation(R.raw.progress_bar)
            statusImageView.playAnimation()
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}