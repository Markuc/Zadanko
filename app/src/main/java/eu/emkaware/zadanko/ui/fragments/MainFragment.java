package eu.emkaware.zadanko.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.transition.Hold;

import eu.emkaware.zadanko.R;
import eu.emkaware.zadanko.databinding.FragmentMainBinding;
import eu.emkaware.zadanko.viewmodel.MainViewModel;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setExitTransition(new Hold());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelStoreOwner store =
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph);
        mainViewModel = new ViewModelProvider(store, getDefaultViewModelProviderFactory()).get(MainViewModel.class);

        mainViewModel.getFirstAddress().observe(getViewLifecycleOwner(), address -> {
            binding.imgSearchFirst.setVisibility(View.INVISIBLE);
            binding.labelSearchFirst.setText(address.getDisplayName());
        });
        mainViewModel.getSecondAddress().observe(getViewLifecycleOwner(), address -> {
            binding.imgSearchSecond.setVisibility(View.INVISIBLE);
            binding.labelSearchSecond.setText(address.getDisplayName());
        });

        binding.cardFirstAddress.setOnClickListener(v -> {
            MainFragmentDirections.ActionMainFragmentToSearchFragment action = MainFragmentDirections.actionMainFragmentToSearchFragment("start");
            action.setIsFirstAddress(true);
            Navigation.findNavController(v).navigate(action, new FragmentNavigator.Extras.Builder().addSharedElement(v, "start").build());
        });

        binding.cardSecondAddress.setOnClickListener(v -> {
            MainFragmentDirections.ActionMainFragmentToSearchFragment action = MainFragmentDirections.actionMainFragmentToSearchFragment("end");
            action.setIsFirstAddress(false);
            Navigation.findNavController(v).navigate(action, new FragmentNavigator.Extras.Builder().addSharedElement(v, "end").build());
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        float distance = mainViewModel.calculateDistance();
        if (distance == -1f) {
            binding.labelDistance.setText(R.string.distance_none);
        } else {
            binding.labelDistance.setText(getString(R.string.distance_between, distance, distance / 1000));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
