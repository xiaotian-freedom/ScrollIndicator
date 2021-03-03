package com.freedom.scrollindicator

import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import net.lucode.hackware.magicindicator.MagicIndicator


/**
 * @Description:绑定ViewPager2和MagicIndicator
 * @Author: TST
 * @CreateDate: 12/22/20$ 11:11 PM$
 * @UpdateUser:
 * @UpdateDate: 12/22/20$ 11:11 PM$
 * @UpdateRemark:
 * @Version: 1.0
 */
object ViewPager2Helper {
    fun bind(magicIndicator: MagicIndicator, viewPager: ViewPager2) {
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                magicIndicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                magicIndicator.onPageScrollStateChanged(state)
            }
        })
    }
}