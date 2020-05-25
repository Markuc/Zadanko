package eu.emkaware.zadanko.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import eu.emkaware.zadanko.databinding.SearchListItemBinding;
import eu.emkaware.zadanko.network.pojo.Address;

public class SearchAdapter extends ListAdapter<Address, SearchAdapter.SearchViewHolder> {

    private OnItemSelectedListener listener;

    public SearchAdapter(OnItemSelectedListener listener) {
        super(new SearchDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Address address);
    }

    static class SearchDiffCallback extends DiffUtil.ItemCallback<Address> {
        @Override
        public boolean areItemsTheSame(@NonNull Address oldItem, @NonNull Address newItem) {
            return oldItem.getLat() == newItem.getLat() && oldItem.getLng() == newItem.getLng();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Address oldItem, @NonNull Address newItem) {
            return oldItem.getTimestamp() == newItem.getTimestamp();
        }
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private SearchListItemBinding binding;

        SearchViewHolder(@NonNull SearchListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Address address) {
            binding.addressText.setText(address.getDisplayName());
            if (address.getTimestamp() != 0)
                binding.imgHistory.setVisibility(View.VISIBLE);
            else
                binding.imgHistory.setVisibility(View.INVISIBLE);
            binding.getRoot().setOnClickListener(v -> listener.onItemSelected(address));
        }
    }
}
