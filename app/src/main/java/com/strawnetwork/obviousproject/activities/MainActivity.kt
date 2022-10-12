package com.strawnetwork.obviousproject.activities




import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.strawnetwork.obviousproject.R
import com.strawnetwork.obviousproject.adapters.NasaImagesAdapter
import com.strawnetwork.obviousproject.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(),View.OnClickListener {
   // private var mSwipeRefresh: SwipeRefreshLayout? = null
    private var mMainActivityViewModel: MainActivityViewModel? = null
    private lateinit var recyclerLayoutManager: GridLayoutManager
    private lateinit var nasaImagesAdapter: NasaImagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //pass refrence of activity to viewmodel
        mMainActivityViewModel!!.NasaImagesData(this@MainActivity)
        setUpClickListener()
        setUpNavigation()
        observeData()
    }

    private fun setUpClickListener() {
        iv_grid.setOnClickListener(this)
        iv_list.setOnClickListener(this)
    }

    private fun setUpNavigation() {
        val res = this.resources
        val profile1 = ProfileDrawerItem().withName("Kashif Ansari").withEmail("@mhd.kashif1")
            .withIcon(ResourcesCompat.getDrawable(res, R.drawable.main_profile_2, null))
        val headerResult = AccountHeaderBuilder()
            .withActivity(this)
            .withCompactStyle(false)
            .withHeaderBackground(R.drawable.header)
            .withAccountHeader(R.layout.custom_header)
            .addProfiles( // Adding the account profiles to the Account Header
                profile1
            )
            .withOnAccountHeaderListener { view: View?, profile: IProfile<*>, currentProfile: Boolean ->
                if (profile === profile1) {
                    togglePic.setImageResource(R.drawable.main_profile_2)
                }
                true
            }
            .build()
        val result = DrawerBuilder()
            .withAccountHeader(headerResult)
            .withActivity(this)
            .withToolbar(toolbar)
            .withTranslucentStatusBar(true)
            .withActionBarDrawerToggle(false)
            .addDrawerItems( // Adding different options to th side navigation drawer
                PrimaryDrawerItem().withIdentifier(2).withName(R.string.item_profile)
                    .withIcon(FontAwesome.Icon.faw_user).withSelectable(false),
                PrimaryDrawerItem().withIdentifier(3).withName(R.string.item_lists)
                    .withIcon(FontAwesome.Icon.faw_list_alt).withSelectable(false),
                PrimaryDrawerItem().withIdentifier(4).withName(R.string.item_moments)
                    .withIcon(FontAwesome.Icon.faw_bolt).withSelectable(false),
                PrimaryDrawerItem().withIdentifier(5).withName(R.string.item_highlights)
                    .withIcon(FontAwesome.Icon.faw_clone).withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem().withIdentifier(6).withName(R.string.item_sap)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(7).withName(R.string.item_help)
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener { view: View?, position: Int, drawerItem: IDrawerItem<*, *>? -> true }
            .addStickyDrawerItems( // Adding options to the footer of the side navigation drawer
                SecondaryDrawerItem().withName(R.string.item_night)
                    .withIcon(FontAwesome.Icon.faw_moon).withIconColorRes(R.color.colorAccent)
                    .withTextColorRes(R.color.colorAccent),
                SecondaryDrawerItem().withName(R.string.item_qr)
                    .withIcon(FontAwesome.Icon.faw_qrcode).withIconColorRes(R.color.colorAccent)
                    .withTextColorRes(R.color.colorAccent)
            )
            .withSelectedItem(-1)
            .build()
        togglePic.setOnClickListener(View.OnClickListener { view: View? ->
            if (result.isDrawerOpen) {
                result.closeDrawer()
            } else {
                result.openDrawer()
            }
        })
        screen_title.setText(getString(R.string.home))
        search_bar.setVisibility(View.GONE)

        // Logic for the navigation tabs
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val home = tabLayout.newTab().setIcon(
            IconicsDrawable(this).icon(FontAwesome.Icon.faw_home).colorRes(R.color.colorAccent)
        )
        val search = tabLayout.newTab().setIcon(
            IconicsDrawable(this).icon(Ionicons.Icon.ion_ios_search)
                .colorRes(R.color.draw_description)
        )
        val notif = tabLayout.newTab().setIcon(
            IconicsDrawable(this).icon(FontAwesome.Icon.faw_bell).colorRes(R.color.draw_description)
        )
        val msg = tabLayout.newTab().setIcon(
            IconicsDrawable(this).icon(FontAwesome.Icon.faw_envelope)
                .colorRes(R.color.draw_description)
        )
        tabLayout.addTab(home)
        tabLayout.addTab(search)
        tabLayout.addTab(notif)
        tabLayout.addTab(msg)

        // Handling the click events on the tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tabLayout.selectedTabPosition == 0) {
                    screen_title.setText(getString(R.string.home))
                    changeLayout.visibility=View.VISIBLE
                    screen_title.setVisibility(View.VISIBLE)
                    search_bar.setVisibility(View.GONE)
                    swiperefresh?.setVisibility(View.VISIBLE)
                    body_msg.setVisibility(View.INVISIBLE)
                    home.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_home)
                        .colorRes(R.color.colorAccent)
                    search.icon =
                        IconicsDrawable(applicationContext).icon(Ionicons.Icon.ion_ios_search)
                            .colorRes(R.color.draw_description)
                    notif.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_bell)
                        .colorRes(R.color.draw_description)
                    msg.icon =
                        IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_envelope)
                            .colorRes(R.color.draw_description)
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        search_bar.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                } else if (tabLayout.selectedTabPosition == 1) {
                    swiperefresh?.setVisibility(View.INVISIBLE)
                    changeLayout.visibility=View.GONE
                    body_msg.setVisibility(View.INVISIBLE)
                    screen_title.setVisibility(View.GONE)
                    search_bar.setVisibility(View.VISIBLE)
                    home.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_home)
                        .colorRes(R.color.draw_description)
                    search.icon =
                        IconicsDrawable(applicationContext).icon(Ionicons.Icon.ion_ios_search)
                            .colorRes(R.color.colorAccent)
                    notif.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_bell)
                        .colorRes(R.color.draw_description)
                    msg.icon =
                        IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_envelope)
                            .colorRes(R.color.draw_description)
                } else if (tabLayout.selectedTabPosition == 2) {
                    swiperefresh?.setVisibility(View.INVISIBLE)
                    body_msg.setVisibility(View.INVISIBLE)
                    changeLayout.visibility=View.GONE
                    screen_title.setText(getString(R.string.notification))
                    screen_title.setVisibility(View.VISIBLE)
                    search_bar.setVisibility(View.GONE)
                    home.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_home)
                        .colorRes(R.color.draw_description)
                    search.icon =
                        IconicsDrawable(applicationContext).icon(Ionicons.Icon.ion_ios_search)
                            .colorRes(R.color.draw_description)
                    notif.icon =
                        IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_bell1)
                            .colorRes(R.color.colorAccent)
                    msg.icon =
                        IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_envelope)
                            .colorRes(R.color.draw_description)
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        search_bar.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                } else if (tabLayout.selectedTabPosition == 3) {
                    swiperefresh?.setVisibility(View.INVISIBLE)
                    body_msg.setVisibility(View.VISIBLE)
                    changeLayout.visibility=View.GONE
                    screen_title.setText(getString(R.string.message))
                    screen_title.setVisibility(View.VISIBLE)
                    search_bar.setVisibility(View.GONE)
                    home.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_home)
                        .colorRes(R.color.draw_description)
                    search.icon =
                        IconicsDrawable(applicationContext).icon(Ionicons.Icon.ion_ios_search)
                            .colorRes(R.color.draw_description)
                    notif.icon = IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_bell)
                        .colorRes(R.color.draw_description)
                    msg.icon =
                        IconicsDrawable(applicationContext).icon(FontAwesome.Icon.faw_envelope1)
                            .colorRes(R.color.colorAccent)
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(
                        search_bar.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Logic for swipe down to refresh
        swiperefresh?.setColorSchemeResources(R.color.colorAccent)
        swiperefresh?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            Handler().postDelayed({
                swiperefresh?.setRefreshing(
                    false
                )
            }, 5000)
        })
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
    var toggleGrid:Boolean=true
    var toggleLinear:Boolean=false
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_grid->{
                if(toggleGrid==false)
                {
                    iv_grid.setImageDrawable(resources?.getDrawable(R.drawable.green_grid))
                    iv_grid.setColorFilter(resources.getColor(R.color.colorAccent));
                    iv_list.setImageDrawable(resources?.getDrawable(R.drawable.list_grey))
                    toggleLinear=false
                    toggleGrid=true
                    setToggle()
                }

            }
            R.id.iv_list->{
                if(toggleLinear==false)
                {
                    iv_grid.setImageDrawable(resources?.getDrawable(R.drawable.grid_grey))
                    iv_list.setImageDrawable(resources?.getDrawable(R.drawable.list_green))
                    iv_list.setColorFilter(resources.getColor(R.color.colorAccent));
                    toggleLinear=true
                    toggleGrid=false
                    setToggle()
                }

            }
            else -> {
                // else condition
            }
        }
    }

    fun setToggle()
    {
        if(recyclerLayoutManager?.spanCount==1)
            recyclerLayoutManager.spanCount=2
        else
            recyclerLayoutManager.spanCount=1
        nasaImagesAdapter?.notifyItemRangeChanged(0, nasaImagesAdapter?.itemCount ?: 0)
    }
    private fun observeData() {
        //observe data from viewmodel when live data have data(means when livedata post the value)
        mMainActivityViewModel!!.getNasaData?.observe(this) { nicePlaces ->
            recyclerView.apply {
                setHasFixedSize(true)
                //set grid layout manager to recyclerview with count 2
                recyclerLayoutManager=GridLayoutManager(this@MainActivity,2)
                layoutManager= recyclerLayoutManager
                //add decoration to recyclerview horizxonral line between 2 cards
                addItemDecoration(
                    DividerItemDecoration(recyclerView.context,
                        DividerItemDecoration.HORIZONTAL)
                )
                addItemDecoration(
                    DividerItemDecoration(recyclerView.context,
                        DividerItemDecoration.VERTICAL)
                )
                //initilize the adapter with arraylist of data and set to recyclerview

            }
            val sortedList=nicePlaces.sortedBy { it.date }
            nasaImagesAdapter= NasaImagesAdapter(this@MainActivity,sortedList,recyclerLayoutManager)
            recyclerView.adapter=nasaImagesAdapter
        }
    }
}