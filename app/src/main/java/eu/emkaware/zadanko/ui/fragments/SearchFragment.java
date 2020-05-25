package eu.emkaware.zadanko.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialContainerTransform;

import java.util.Calendar;

import eu.emkaware.zadanko.R;
import eu.emkaware.zadanko.databinding.FragmentSearchBinding;
import eu.emkaware.zadanko.ui.adapters.SearchAdapter;
import eu.emkaware.zadanko.viewmodel.MainViewModel;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private MainViewModel mainViewModel;
    private boolean isFirstAddress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSharedElementEnterTransition(new MaterialContainerTransform());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            SearchFragmentArgs safeArgs = SearchFragmentArgs.fromBundle(args);
            binding.getRoot().setTransitionName(safeArgs.getTransition());
            isFirstAddress = safeArgs.getIsFirstAddress();
        }

        SearchAdapter adapter = new SearchAdapter(address -> {
            address.setTimestamp(Calendar.getInstance().getTimeInMillis());
            mainViewModel.addAddressToHistory(address);
            if (isFirstAddress) {
                mainViewModel.setFirstAddress(address);
            } else {
                mainViewModel.setSecondAddress(address);
            }
            Navigation.findNavController(view).popBackStack();
        });

        binding.laySearch.setStartIconOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        ViewModelStoreOwner store =
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph);
        mainViewModel = new ViewModelProvider(store, getDefaultViewModelProviderFactory()).get(MainViewModel.class);
        mainViewModel.loadAddresses("");
        mainViewModel.getAddresses().observe(getViewLifecycleOwner(), addressResponses -> {
            adapter.submitList(addressResponses);
            if (addressResponses.isEmpty()) {
                if (binding.inputSearch.getText() == null || binding.inputSearch.getText().toString().isEmpty()) {
                    binding.labelEmptyList.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info_outline, 0, 0);
                    binding.labelEmptyList.setText(R.string.no_history_results);
                } else {
                    binding.labelEmptyList.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_error_outline, 0, 0);
                    binding.labelEmptyList.setText(R.string.no_search_results);
                }
                binding.labelEmptyList.setVisibility(View.VISIBLE);
            } else {
                binding.labelEmptyList.setVisibility(View.INVISIBLE);
            }
            hideIndeterminateProgress();
        });

        binding.listResult.setAdapter(adapter);
        binding.listResult.setHasFixedSize(true);

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mainViewModel.loadAddresses(s.toString()))
                    showIndeterminateProgress();
            }
        });
    }

    private void showIndeterminateProgress() {
        TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
        binding.listResult.setVisibility(View.INVISIBLE);
        binding.progressCircular.setVisibility(View.VISIBLE);
        binding.labelEmptyList.setVisibility(View.INVISIBLE);
    }

    private void hideIndeterminateProgress() {
        TransitionManager.beginDelayedTransition((ViewGroup) binding.getRoot());
        binding.listResult.setVisibility(View.VISIBLE);
        binding.progressCircular.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
