package com.example.seesea.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.seesea.R
import com.example.seesea.databinding.FragmentMapsBinding
import com.example.seesea.databinding.ItemSearchBinding
import com.example.seesea.map.mapSearch.MapSearchAdapter
import com.example.seesea.map.mapSearch.MapSearchFragment
import com.example.seesea.roomDB.RoomDataViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("CAST_NEVER_SUCCEEDS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment(){

    private var mMarker: Marker? = null
    private var binding : FragmentMapsBinding? = null
    private val viewModel: RoomDataViewModel by activityViewModels()
    private var searchMarker:ItemSearchBinding? = null

    //關於google maps操作上的邏輯，如marker等
    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.mapPage.displayDivingPoint.observe(viewLifecycleOwner) { list ->
            googleMap.clear()
            list.forEach {
                val point = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions().position(point).title(it.divingPointName))
            }
            if (list.isEmpty()) return@observe
            if (list.size > 100) {
                val startPoint = LatLng(24.176281637578253, 120.62097516880185)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,7.5f))
            }else{
                val startPoint = LatLng(list[0].latitude, list[0].longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint,9.5f))
            }
        }

        googleMap.setOnInfoWindowClickListener {
            binding?.recyclerViewDivingPoint?.visibility = View.VISIBLE
        }

        googleMap.setOnMarkerClickListener { marker -> //點擊MarkerInfo會讓recyclerView跳轉到當前的marker資訊
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.position))
            if(mMarker == marker && binding?.recyclerViewDivingPoint?.visibility == View.VISIBLE){
                binding?.recyclerViewDivingPoint?.visibility = View.INVISIBLE
            }
            if (mMarker == null || marker != mMarker) {
                mMarker = marker
                val id = marker.title
                viewModel.mapPage.setOnClickMarkerNumber(id)

                binding?.recyclerViewDivingPoint?.visibility = View.VISIBLE
                marker.showInfoWindow()
            }
            true
        }
        googleMap.setOnMapClickListener {
            binding?.recyclerViewDivingPoint?.visibility = View.INVISIBLE
        }

        viewModel.mapPage.onMarkerClick.observe(viewLifecycleOwner){
            binding?.recyclerViewDivingPoint?.scrollToPosition(it[0].toInt())
            val point = LatLng(it[1],it[2])
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,10.5f))
        }

        searchMarker!!.itemSearchCancel.setOnClickListener {
            searchMarker!!.itemSearch.visibility = View.GONE
            resetDP(googleMap)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        binding = FragmentMapsBinding.bind(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding?.apply {
            searchMarker = includeSearchMarker
            searchMarker!!.itemSearch.visibility = View.GONE
            val divingPointAdapter = DivingPointAdapter(viewModel,activity as FragmentActivity)
            val recyclerViewManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(recyclerViewDivingPoint)
            recyclerViewDivingPoint.adapter = divingPointAdapter
            recyclerViewDivingPoint.layoutManager = recyclerViewManager
            viewModel.mapPage.displayDivingPoint.observe(viewLifecycleOwner){
                divingPointAdapter.submitList(it)
            }


            val mapSearchAdapter = MapSearchAdapter(viewModel,recyclerViewMapSearch,mapSearchHint)
            recyclerViewMapSearch.adapter = mapSearchAdapter
            viewModel.mapPage.searchDivingPointName.observe(viewLifecycleOwner){
                mapSearchAdapter.submitList(it)
            }

            //使用關鍵字搜尋，馬上使用輸入的字filter並顯示
            mapSearchHint.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}//輸入後的監聽
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}//輸入後的監聽
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.mapPage.mapSearchByKey(s.toString())
                }
            })

            //不滑動時才讀圖，為避免在滑動且讀圖時會有畫面卡頓
            recyclerViewDivingPoint.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
//                    if(newState == 1 || newState == 2)divingPointAdapter.setScrolling(true) else divingPointAdapter.setScrolling(false)
                }
            })

            viewModel.mapPage.searchHint.observe(viewLifecycleOwner){
                searchMarker!!.itemSearchName.text = it
                searchMarker!!.itemSearch.visibility = View.VISIBLE
            }

            filter.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_maps, MapSearchFragment(), MapSearchFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }


    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun resetDP(googleMap: GoogleMap){
        googleMap.clear()
        viewModel.mapPage.resetDivingPoint()
    }

}