package com.example.seesea.room.roomList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.R
import com.example.seesea.create.CreateRoomActivity
import com.example.seesea.databinding.FragmentRoomListBinding
import com.example.seesea.roomDB.RoomDataViewModel
import com.example.seesea.search.SearchAdapter
import com.example.seesea.user.LowExperienceFragment
import com.example.seesea.user.SIGN_UP
import com.example.seesea.user.USER_EXPERIENCE
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

val USER_TYPE_LIST = listOf("HOST","PARTICIPANT","APPLICANT","GUEST")
const val HOLDING = 1
const val FULL = 2
const val DECLARE_FULL = 3
const val ENDING = 4
const val FAIL = 5

class RoomListFragment : Fragment(){
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var binding : FragmentRoomListBinding? = null
    private var roomListAdapter :RoomListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        binding = FragmentRoomListBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.roomListPage.loadList()
        binding?.apply {
            activity.apply {
                roomListAdapter = RoomListAdapter(this,viewLifecycleOwner,this!!.application,viewModel)
                recyclerViewMain.adapter = roomListAdapter
                viewModel.roomListPage.allRoomList.observe(this){
                    if(it.isNotEmpty()){
                        mainHint.visibility = View.INVISIBLE
                    }else{
                        mainHint.visibility = View.VISIBLE
                    }
                    roomListAdapter!!.submitList(it)
                }
                recyclerViewMain.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                    }
//
//                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                        super.onScrolled(recyclerView, dx, dy)
//                        val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount === 0) 0 else recyclerView.getChildAt(0).top
//                        srlLayout.isEnabled = topRowVerticalPosition >= 0
//                    }
                })



                val searchAdapter = SearchAdapter(viewModel)
                val layoutManager = FlexboxLayoutManager(requireContext())
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.flexWrap = FlexWrap.WRAP
                layoutManager.justifyContent = JustifyContent.FLEX_START
                recyclerViewSearch.adapter = searchAdapter
                recyclerViewSearch.layoutManager = layoutManager
                viewModel.roomListPage.searchChip.observe(this){
                    if(it.isEmpty()){
                        viewModel.roomListPage.loadList()
                    }
                    searchAdapter.submitList(it)
                }

                srlLayout.setOnRefreshListener {
                    viewModel.roomListPage.loadList() //更新內容
                    srlLayout.isRefreshing = false
                }

                mainCreateRoomButton.setOnClickListener{
                    var hasExperience = false
                    viewModel.user.observe(viewLifecycleOwner) {
                        if(!it.isAcccountActive){
                            layoutNotVerifiedYet.visibility = View.VISIBLE
                            hasExperience = true
                            return@observe
                        }
                        if (it.userExperience == USER_EXPERIENCE[0]) {
                            val transaction = childFragmentManager.beginTransaction()
                            val fragment = LowExperienceFragment.newInstance(SIGN_UP)
                            transaction.replace(R.id.layout_check, fragment, LowExperienceFragment::class.java.simpleName).addToBackStack(null).commit()
                            hasExperience = true
                        }
                    }
                    if(hasExperience){
                        return@setOnClickListener
                    }
                    intent.setClass(this, CreateRoomActivity::class.java)
                    startActivityForResult(intent, AppCompatActivity.RESULT_FIRST_USER)
                    this.startActivity(intent)
                }
            }
            roomListNotVerifiedYet.okay.setOnClickListener {
                layoutNotVerifiedYet.visibility = View.GONE
            }

        }

    }

    override fun onStart() {
        roomListAdapter?.notifyDataSetChanged()
        super.onStart()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


}