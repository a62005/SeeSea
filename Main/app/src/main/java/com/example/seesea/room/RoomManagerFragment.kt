package com.example.seesea.room

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.activityViewModels
import com.example.seesea.LOGOUT
import com.example.seesea.MainActivity
import com.example.seesea.R
import com.example.seesea.databinding.FragmentRoomManagerBinding
import com.example.seesea.participateBox.ParticipateBoxFragment
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.search.SearchFragment
import com.example.seesea.user.HOST
import com.example.seesea.user.UserDetailFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class RoomManagerFragment : Fragment() {
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentRoomManagerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_manager, container, false)
        binding = FragmentRoomManagerBinding.bind(view)
        return view
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            layoutMainItem.visibility = View.GONE
            val pageAdapter = RoomManagerFragmentAdapter(childFragmentManager, lifecycle)
            viewPage2RoomList.adapter = pageAdapter
            val title:ArrayList<String> = arrayListOf("活動列表","推薦","關注清單")
            val icon: ArrayList<Drawable> = arrayListOf(
                resources.getDrawable(R.drawable.article,null),
                resources.getDrawable(R.drawable.recommend,null),
                resources.getDrawable(R.drawable.activity_heart_unclick,null))
            TabLayoutMediator(tabLayoutToolBar,viewPage2RoomList){  tab, position->
                tab.icon = icon[position]
            }.attach()


            tabLayoutToolBar.addOnTabSelectedListener(object : AdapterView.OnItemSelectedListener,
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if(tab!!.isSelected){
                        roomListTitle.text = title[tab.position]
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            })

            mainItemButton.setOnClickListener {
                when(mainItemButton.visibility){
                    View.VISIBLE -> {
                        mainItemButton.visibility = View.INVISIBLE
                        mainItemDown.visibility = View.VISIBLE
                        layoutMainItem.visibility = View.VISIBLE
                    }
                    View.INVISIBLE ->{
                        mainItemButton.visibility = View.VISIBLE
                        mainItemDown.visibility = View.INVISIBLE
                        layoutMainItem.visibility = View.INVISIBLE
                    }
                    View.GONE -> {
                        TODO()
                    }
                }

            }
            fragmentRoomManager.setOnClickListener {
                layoutMainItem.visibility = View.INVISIBLE
                mainItemButton.visibility = View.VISIBLE
                mainItemDown.visibility = View.INVISIBLE
            }
            mainUserRoomListButton.setOnClickListener {
                layoutMainItem.visibility = View.GONE
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_main, ParticipateBoxFragment(), ParticipateBoxFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
            mainUserDataButton.setOnClickListener {
                layoutMainItem.visibility = View.GONE
                val userDetailFragment = UserDetailFragment.newInstance(HOST)
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_main, userDetailFragment, UserDetailFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
            mainLogoutButton.setOnClickListener {
                layoutMainItem.visibility = View.GONE
                val mActivity = context as MainActivity
                mActivity.logout(LOGOUT)
                layoutMainItem.visibility = View.GONE
            }

            mainSearchButton.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_main, SearchFragment(), SearchFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    interface Logout{
        fun logout(keyWord:String)
    }

}