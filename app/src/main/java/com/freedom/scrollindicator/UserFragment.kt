package com.freedom.scrollindicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * @Description:
 * @Author: TST
 * @CreateDate: 3/3/21$ 4:11 PM$
 * @UpdateUser:
 * @UpdateDate: 3/3/21$ 4:11 PM$
 * @UpdateRemark:
 * @Version: 1.0
 */
class UserFragment: Fragment() {

    companion object {
        fun newInstance(content: String): Fragment {
            val fragment = UserFragment()
            val bundle = Bundle()
            bundle.putString("content", content)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val content = it.getString("content", "")
            mTvContent.text = content
        }
    }
}