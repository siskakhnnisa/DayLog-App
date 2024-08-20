package com.powerpuff.daylog.countdown.ui.list

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.powerpuff.daylog.R
import com.powerpuff.daylog.countdown.ViewModelFactory
import com.powerpuff.daylog.countdown.data.Habit
import com.powerpuff.daylog.countdown.ui.add.AddHabitActivity
import com.powerpuff.daylog.countdown.ui.detail.DetailHabitActivity
import com.powerpuff.daylog.utils.Event
import com.powerpuff.daylog.utils.HABIT_ID
import com.powerpuff.daylog.utils.HabitSortType

class HomeFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: HabitListViewModel
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set FloatingActionButton
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val addIntent = Intent(requireContext(), AddHabitActivity::class.java)
            startActivity(addIntent)
        }

        recycler = view.findViewById(R.id.rv_habit)
        recycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
        }

        initAction()

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(HabitListViewModel::class.java)

        viewModel.habits.observe(viewLifecycleOwner, Observer(this::showRecyclerView))

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            it?.let { showRecyclerView(it) }
        })
        viewModel.snackbarText.observe(viewLifecycleOwner, Observer(this::showSnackBar))

        // SEARCH HABIT
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    viewModel.searchHabitsByTitle(newText.orEmpty())
                }
                handler.postDelayed(searchRunnable!!, 300) // 300ms delay for debouncing
                return true
            }
        })

        // Set OnClickListener untuk action_sort
        view.findViewById<View>(R.id.action_sort).setOnClickListener {
            showFilteringPopUpMenu(it)
        }
    }

    // UPDATE VIEW AFTER USER SEARCH HABIT
    private fun showRecyclerView(habit: PagedList<Habit>) {
        val adapterH = HabitAdapter { dataHabit ->
            val intent = Intent(requireContext(), DetailHabitActivity::class.java)
            intent.putExtra(HABIT_ID, dataHabit.id)
            startActivity(intent)
        }

        adapterH.submitList(habit)
        recycler.adapter = adapterH
    }

    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getContentIfNotHandled() ?: return
        Snackbar.make(
            requireView().findViewById(R.id.coordinator_layout),
            getString(message),
            Snackbar.LENGTH_SHORT
        ).setAction("Undo") {
            viewModel.insert(viewModel.undo.value?.getContentIfNotHandled() as Habit)
        }.show()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_main, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> {
//                val intent = Intent(context, SettingActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun showFilteringPopUpMenu(view: View) {
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.sort_habits, menu)
            setOnMenuItemClickListener {
                viewModel.filter(
                    when (it.itemId) {
                        R.id.minutes_focus -> HabitSortType.MINUTES_FOCUS
                        R.id.title_name -> HabitSortType.TITLE_NAME
                        else -> HabitSortType.START_TIME
                    }
                )
                true
            }
            show()
        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val habit = (viewHolder as HabitAdapter.HabitViewHolder).getHabit
                viewModel.deleteHabit(habit)
            }
        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}

