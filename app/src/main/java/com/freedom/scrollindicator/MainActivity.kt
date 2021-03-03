package com.freedom.scrollindicator

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import java.lang.ref.WeakReference
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    //当前选中的位置
    private var mSelectedIndex = 0
    private var mDirection = 0

    private val indicatorList = mutableListOf<String>()

    private val fragmentList = mutableListOf<Fragment>()

    private lateinit var mHandler: MyHandler

    companion object {
        //向右滑 向左滚动
        const val DIRECTION_LEFT = 1

        //向左滑 向右滚动
        const val DIRECTION_RIGHT = 2

        const val UPDATE_LEFT_MARGIN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mHandler = MyHandler(this, Looper.myLooper()!!)
        initData()
    }

    private fun initData() {
        for (i in 0..10) {
            indicatorList.add("标题$i")
            fragmentList.add(UserFragment.newInstance("这是内容$i"))
        }
        initMagic()
    }

    private fun initMagic() {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return indicatorList.size
            }

            override fun getTitleView(context: Context, i: Int): IPagerTitleView {
                val titleView = ColorTransitionPagerTitleView(context)
                titleView.normalColor = Color.DKGRAY
                titleView.selectedColor = Color.BLUE
                titleView.text = indicatorList[i]
                titleView.setOnClickListener { mViewPager.currentItem = i }
                return titleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val myIndicator = LinePagerIndicator(context)
                myIndicator.lineWidth = 0f
                myIndicator.lineHeight = 0f
                return myIndicator
            }
        }
        mMagicIndicator.navigator = commonNavigator
        ViewPager2Helper.bind(mMagicIndicator, mViewPager)
        initScrollIndicator()
        setViewPagerAdapter()
    }

    private fun initScrollIndicator() {
        val totalOffset = SizeUtils.dp2px(54f) - SizeUtils.dp2px(18f)
        val offset = if (indicatorList.size == 1) {
            0
        } else {
            totalOffset / (indicatorList.size - 1)
        }
        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                mMagicIndicator.onPageScrolled(
                    position,
                    positionOffset,
                    positionOffsetPixels
                )
                //偏移量为0 说明运动停止
                if (positionOffset == 0f) {
                    mSelectedIndex = position
                }
                //向左滑，指示器向右移动
                if (position + positionOffset - mSelectedIndex > 0) {
                    mDirection = DIRECTION_RIGHT
                    //向左快速滑动 偏移量不归0 但是position发生了改变 需要更新当前索引
                    if (mDirection == DIRECTION_RIGHT && position + positionOffset > mSelectedIndex + 1) {
                        mSelectedIndex = position
                    } else {
                        val msg = mHandler.obtainMessage()
                        msg.what = UPDATE_LEFT_MARGIN
                        msg.arg1 = (offset * (position + positionOffset)).roundToInt()
                        mHandler.sendMessageDelayed(msg, 0)
                    }
                } else if (position + positionOffset - mSelectedIndex < 0) { //向右滑，指示器向左移动
                    mDirection = DIRECTION_LEFT
                    //向右快速滑动
                    if (mDirection == DIRECTION_LEFT && position + positionOffset < mSelectedIndex - 1) {
                        mSelectedIndex = position
                    } else {
                        val msg = mHandler.obtainMessage()
                        msg.what = UPDATE_LEFT_MARGIN
                        msg.arg1 = ((position + positionOffset) * offset).roundToInt()
                        mHandler.sendMessageDelayed(msg, 0)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mMagicIndicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                mMagicIndicator.onPageScrollStateChanged(state)
            }
        })
    }

    private fun setViewPagerAdapter() {
        val mFragmentAdapter = CommonFragmentAdapter2(supportFragmentManager, lifecycle, fragmentList)
        mViewPager.isUserInputEnabled = true
        mViewPager.adapter = mFragmentAdapter
        mViewPager.offscreenPageLimit = fragmentList.size
        mViewPager.currentItem = 0
    }

    /**
     * 更新view的margin值
     */
    fun updateParam(margin: Int) {
        val params = mLineView.layoutParams as FrameLayout.LayoutParams
        params.leftMargin = margin
        mLineView.layoutParams = params
    }


    class MyHandler(activity: MainActivity, looper: Looper) : Handler(looper) {

        private var mActivity: WeakReference<MainActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_LEFT_MARGIN -> {
                    mActivity.get()?.updateParam(msg.arg1)
                }
            }
        }
    }
}