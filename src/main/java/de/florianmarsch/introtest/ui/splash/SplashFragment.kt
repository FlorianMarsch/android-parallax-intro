package de.florianmarsch.introtest.ui.splash


import android.content.Intent
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import de.florianmarsch.introtest.MainActivity
import de.florianmarsch.introtest.R
import kotlinx.android.synthetic.main.splash_fragment.*




class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val zoomed = ConstraintSet()
        zoomed.clone(this.context, R.layout.splash_fragment_zoomed)



       Glide.with(this).load("http://www.artofmtg.com/wp-content/uploads/2019/05/Secluded-Steppe-Modern-Horizons-MtG-Art.jpg").listener(object : RequestListener<Drawable> {
           override fun onLoadFailed(
               e: GlideException?,
               model: Any?,
               target: Target<Drawable>?,
               isFirstResource: Boolean
           ): Boolean {
               //TODO: something on exception
               return false
           }
           override fun onResourceReady(
               resource: Drawable?,
               model: Any?,
               target: com.bumptech.glide.request.target.Target<Drawable>?,
               dataSource: DataSource?,
               isFirstResource: Boolean
           ): Boolean {
               imageLayerLeft1.setImageDrawable(resource?.getConstantState()?.newDrawable())
               imageLayerLeft2.setImageDrawable(resource?.getConstantState()?.newDrawable())
               imageLayerRight1.setImageDrawable(resource?.getConstantState()?.newDrawable())
               imageLayerRight1.imageMatrix = anchorRight(imageLayerRight1)

               imageLayerRight2.setImageDrawable(resource?.getConstantState()?.newDrawable())
               imageLayerRight2.imageMatrix = anchorRight(imageLayerRight2)
               animateCurtain(zoomed)
               return false
           }
       }).into(imageView)


    }

    private fun anchorRight(image: ImageView): Matrix? {
        return Matrix().apply {
            val dWidth = image.drawable.intrinsicWidth
            val dHeight = image.drawable.intrinsicHeight

            val vWidth = image.measuredWidth
            val vHeight = image.measuredHeight
            setTranslate(
                (vWidth - dWidth).toFloat(),
                (vHeight - dHeight).toFloat()
            )
        }
    }

    private fun animateCurtain(zoomed: ConstraintSet) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            val animZoomIn = AnimationUtils.loadAnimation(this.context, R.anim.zoom_in)
            imageView.startAnimation(animZoomIn)


            val transition = ChangeBounds()
            transition.interpolator = AnticipateOvershootInterpolator(1.0f)
            transition.duration = 2200
            transition.doOnEnd {
                startMainActivity()
            }

            TransitionManager.beginDelayedTransition(splash, transition)
            zoomed.applyTo(splash)

        } else {
            startMainActivity()
        }
    }

    fun startMainActivity(){
        val intent = Intent(this.context, MainActivity::class.java)
        startActivity(intent)
    }


}
